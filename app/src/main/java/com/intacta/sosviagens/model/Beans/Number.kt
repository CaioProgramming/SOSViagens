package com.intacta.sosviagens.model.Beans

class Number {
    var ident: String? = null
    var tipo: String? = null
    var telefone: String? = null

    constructor(identification: String, tipo: String, number: String) {
        this.ident = identification
        this.tipo = tipo
        this.telefone = number
    }

    constructor() {}
}
