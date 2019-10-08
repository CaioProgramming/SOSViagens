package com.intacta.sosviagens.Beans;

public class Number {
    private String ident,tipo, telefone;

    public Number(String identification, String tipo, String number) {
        this.ident = identification;
        this.tipo = tipo;
        this.telefone = number;
    }

    public Number() { }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
