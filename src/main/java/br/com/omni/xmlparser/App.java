package br.com.omni.xmlparser;

import br.com.omni.xmlparser.converter.XmlToCsvConverter;
import java.io.File;

public class App {
    public static void main( String[] args ) {
        if (args.length == 0 || args[0].equals("--help") || args[0].equals("-h")) {
            imprimirAjuda();
            return;
        }

        if (args.length < 1) {
            System.err.println("Erro: Parâmetros insuficientes.");
            System.err.println("Use --help para ver as instruções de uso.");
            System.exit(1);
        }

        String inputFile = args[0];

        File input = new File(inputFile);
        if (!input.exists()) {
            System.err.println("Erro: Arquivo de entrada não encontrado: " + inputFile);
            System.exit(1);
        }

        System.out.println("\nIniciando conversão com os seguintes parâmetros:");
        System.out.println("Arquivo de entrada: " + inputFile);

        try {
            XmlToCsvConverter converter = new XmlToCsvConverter();
            converter.convert(inputFile);
        } catch (Exception e) {
            System.err.println("Erro durante a conversão: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void imprimirAjuda() {
        System.out.println("\nTotalizadorXml - Totalizador por modalidade e vencimento");
        System.out.println("\nUso:");
        System.out.println("  java -jar totalizador-xml.jar <arquivo_entrada>");
        System.out.println("\nParâmetros:");
        System.out.println("  arquivo_entrada      Caminho do arquivo XML de entrada (obrigatório)");
        System.out.println("\nExemplos:");
        System.out.println("  java -jar totalizador-xml.jar input.xml");
        System.out.println("  java -jar totalizador-xml.jar --help");
    }
}
