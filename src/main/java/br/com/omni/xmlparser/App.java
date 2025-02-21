package br.com.omni.xmlparser;

import br.com.omni.xmlparser.converter.XmlToCsvConverter;
import java.io.File;

public class App {
    public static void main( String[] args ) {
        if (args.length == 0 || args[0].equals("--help") || args[0].equals("-h")) {
            imprimirAjuda();
            return;
        }

        if (args.length < 2) {
            System.err.println("Erro: Parâmetros insuficientes.");
            System.err.println("Use --help para ver as instruções de uso.");
            System.exit(1);
        }

        String inputFile = args[0];
        String outputFile = args[1];
        int intervaloRelatorio = args.length > 2 ? Integer.parseInt(args[2]) : 500;

        File input = new File(inputFile);
        if (!input.exists()) {
            System.err.println("Erro: Arquivo de entrada não encontrado: " + inputFile);
            System.exit(1);
        }

        System.out.println("\nIniciando conversão com os seguintes parâmetros:");
        System.out.println("Arquivo de entrada: " + inputFile);
        System.out.println("Arquivo de saída: " + outputFile);
        System.out.println("Intervalo de relatório: " + intervaloRelatorio + " clientes");
        System.out.println("Tamanho do arquivo: " + (input.length() / (1024 * 1024)) + " MB\n");

        try {
            XmlToCsvConverter converter = new XmlToCsvConverter();
            converter.convert(inputFile, outputFile, intervaloRelatorio);
        } catch (Exception e) {
            System.err.println("Erro durante a conversão: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void imprimirAjuda() {
        System.out.println("\nTotalizadorXml - Conversor de XML para CSV com totalizador por modalidade e vencimento");
        System.out.println("\nUso:");
        System.out.println("  java -jar totalizador-xml.jar <arquivo_entrada> <arquivo_saida> <intervalo_relatorio>");
        System.out.println("\nParâmetros:");
        System.out.println("  arquivo_entrada      Caminho do arquivo XML de entrada (obrigatório)");
        System.out.println("  arquivo_saida        Caminho do arquivo CSV de saída (obrigatório)");
        System.out.println("  intervalo_relatorio  Número de clientes processados entre cada relatório (opcional, padrão: 500)");
        System.out.println("\nExemplos:");
        System.out.println("  java -jar totalizador-xml.jar input.xml output.csv");
        System.out.println("  java -jar totalizador-xml.jar input.xml output.csv 1000");
        System.out.println("  java -jar totalizador-xml.jar --help");
        System.out.println("\nColunas do CSV gerado:");
        System.out.println("  - DtBase  (Data base do documento)");
        System.out.println("  - CNPJ    (CNPJ da instituição)");
        System.out.println("  - Cd      (Código do cliente)");
        System.out.println("  - Mod     (Modalidade da operação)");
        System.out.println("  - Venc    (Código do vencimento)");
        System.out.println("  - Valor   (Valor do vencimento)");
    }
}
