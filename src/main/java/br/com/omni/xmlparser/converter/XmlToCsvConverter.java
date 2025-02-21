package br.com.omni.xmlparser.converter;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

public class XmlToCsvConverter {

    private int clientesProcessados = 0;
    private final Map<String, Map<String, Double>> totais = new HashMap<>();

    public void convert(String inputXmlPath) {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);

        try (FileInputStream fis = new FileInputStream(inputXmlPath)) {

            XMLStreamReader reader = factory.createXMLStreamReader(fis);
            String mod = null;

            while (reader.hasNext()) {
                int event = reader.next();

                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        String elementName = reader.getLocalName();

                        switch (elementName) {
                            case "Op":
                                mod = reader.getAttributeValue(null, "Mod");
                                break;

                            case "Venc":
                                for (int i = 0; i < reader.getAttributeCount(); i++) {
                                    String attrName = reader.getAttributeLocalName(i);
                                    String attrValue = reader.getAttributeValue(i);
                                    adicionarValor(mod, attrName, Double.parseDouble(attrValue));
                                }
                                break;
                        }
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        if ("Cli".equals(reader.getLocalName())) {
                            clientesProcessados++;
                        }
                        break;
                }
            }
            imprimirTotais();
            reader.close();
        } catch (Exception e) {
            System.err.println("Erro ao processar arquivo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void adicionarValor(String mod, String venc, Double valor) {
        totais.computeIfAbsent(mod, k -> new HashMap<>());
        totais.get(mod).merge(venc, valor, Double::sum);
    }

    private void imprimirTotais() {
        System.out.println("\n=== " + clientesProcessados + " clientes processados ===");
        totais.keySet().stream().sorted().forEach(
                mod ->  {
                    System.out.println("\nModalidade: " + mod);
                    Map<String, Double> valorPorMod = totais.get(mod);
                    valorPorMod.keySet().stream().sorted().forEach(
                            vencimento -> {
                                System.out.printf("  %s: R$ %,.2f%n", vencimento, valorPorMod.get(vencimento));
                            });
                });
        System.out.println("=======================================\n");
    }
}
