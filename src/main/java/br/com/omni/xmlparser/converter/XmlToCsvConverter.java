package br.com.omni.xmlparser.converter;

import br.com.omni.xmlparser.domain.RegistroOperacao;
import br.com.omni.xmlparser.domain.Totalizador;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

public class XmlToCsvConverter {

    public void convert(String inputXmlPath, String outputCsvPath, int intervaloRelatorio) {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);

        Totalizador totalizador = new Totalizador(intervaloRelatorio);

        try (FileInputStream fis = new FileInputStream(inputXmlPath);
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputCsvPath))) {

            writer.write("DtBase;CNPJ;Cd;Mod;Venc;Valor");
            writer.newLine();

            XMLStreamReader reader = factory.createXMLStreamReader(fis);
            RegistroOperacao registro = null;
            String clienteAtual = null;

            while (reader.hasNext()) {
                int event = reader.next();

                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        String elementName = reader.getLocalName();

                        switch (elementName) {
                            case "Doc3040":
                                registro = new RegistroOperacao();
                                registro.setDtBase(reader.getAttributeValue(null, "DtBase"));
                                registro.setCnpj(reader.getAttributeValue(null, "CNPJ"));
                                break;

                            case "Cli":
                                if (registro != null) {
                                    String novoCli = reader.getAttributeValue(null, "Cd");
                                    if (!novoCli.equals(clienteAtual)) {
                                        clienteAtual = novoCli;
                                        totalizador.incrementarClientes();
                                    }
                                    registro.setCdCli(novoCli);
                                }
                                break;

                            case "Op":
                                if (registro != null) {
                                    registro.setMod(reader.getAttributeValue(null, "Mod"));
                                }
                                break;

                            case "Venc":
                                if (registro != null) {
                                    for (int i = 0; i < reader.getAttributeCount(); i++) {
                                        String attrName = reader.getAttributeLocalName(i);
                                        String attrValue = reader.getAttributeValue(i);
                                        registro.addVencimento(attrName, attrValue);
                                    }

                                    totalizador.adicionarValores(registro.getMod(), registro.getVencimentos());

                                    for (String  venc : registro.getVencimentosKeys()) {
                                        writer.write(String.format("%s;%s;%s;%s;%s;%s%n",
                                                registro.getDtBase(),
                                                registro.getCnpj(),
                                                registro.getCdCli(),
                                                registro.getMod(),
                                                venc,
                                                registro.getValor(venc)
                                        ));
                                    }
                                }
                                break;
                        }
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        if ("Doc3040".equals(reader.getLocalName())) {
                            registro = null;
                        }
                        break;
                }
            }

            // Imprime totais finais
            System.out.println("\n=== TOTAIS FINAIS ===");
            totalizador.imprimirTotais();

            reader.close();

        } catch (Exception e) {
            System.err.println("Erro ao processar arquivo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
