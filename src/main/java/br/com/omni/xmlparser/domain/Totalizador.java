package br.com.omni.xmlparser.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class Totalizador {

    private int clientesProcessados = 0;
    private final Map<String, Map<String, Double>> totalPorModVenc = new HashMap<>();
    private final int intervaloRelatorio;

    public void adicionarValores(String mod, Map<String, String> vencimentos) {
        totalPorModVenc.computeIfAbsent(mod, k -> new HashMap<>());

        for (Map.Entry<String, String> venc : vencimentos.entrySet()) {
            Double valor = Double.parseDouble(venc.getValue());
            totalPorModVenc.get(mod).merge(venc.getKey(), valor, Double::sum);
        }
    }

    public void incrementarClientes() {
        if (clientesProcessados > 0 && clientesProcessados % intervaloRelatorio == 0) {
            imprimirTotais();
        }
        clientesProcessados++;
    }

    public void imprimirTotais() {
        System.out.println("\n=== Totais após " + clientesProcessados + " clientes processados ===");
        totalPorModVenc.keySet().stream().sorted().forEach(
                mod ->  {
            System.out.println("\nModalidade: " + mod);
            Map<String, Double> valorPorMod = totalPorModVenc.get(mod);
            valorPorMod.keySet().stream().sorted().forEach(
                    vencimento -> {
                System.out.printf("  %s: R$ %,.2f%n", vencimento, valorPorMod.get(vencimento));
            });
        });
        System.out.println("=======================================\n");
    }
}
