package com.intacta.sosviagens.Beans;

public class Suggest extends Road {

    public Suggest() {
    }


    public void setDia(String dia) {
        this.dia = dia;
    }

    private String dia;

    public Suggest(String rodovia, String concessionaria, String telefone, String id,String dia) {
        super(rodovia, concessionaria, telefone, id);
        this.dia = dia;
    }
}

