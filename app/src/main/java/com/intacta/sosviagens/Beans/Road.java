package com.intacta.sosviagens.Beans;

public class Road {
    private String rodovia,concessionaria,telefone,id;

    public Road() {
    }

    public Road(String rodovia, String concessionaria, String telefone, String id) {
        this.rodovia = rodovia;
        this.concessionaria = concessionaria;
        this.telefone = telefone;
        this.id = id;
    }

    public String getRodovia() {
        return rodovia;
    }

    public void setRodovia(String rodovia) {
        this.rodovia = rodovia;
    }

    public String getConcessionaria() {
        return concessionaria;
    }

    public void setConcessionaria(String concessionaria) {
        this.concessionaria = concessionaria;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
