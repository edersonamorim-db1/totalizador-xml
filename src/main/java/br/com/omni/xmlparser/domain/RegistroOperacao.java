package br.com.omni.xmlparser.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroOperacao {

    String dtBase;
    String cnpj;
    String cdCli;
    String mod;
    Map<String, String> vencimentos = new HashMap<>();

    public void addVencimento(String vencimento, String valor) {
        vencimentos.put(vencimento, valor);
    }

    public List<String> getVencimentosKeys() {
        List<String> keys = new ArrayList<>(vencimentos.keySet());
        Collections.sort(keys);
        return keys;
    }

    public String getValor(String vencimento) {
        return vencimentos.get(vencimento);
    }
}
