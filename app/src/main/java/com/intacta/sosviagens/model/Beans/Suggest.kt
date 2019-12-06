package com.intacta.sosviagens.model.Beans

class Suggest : Road {

    private var dia: String? = null

    constructor() {}


    fun setDia(dia: String) {
        this.dia = dia
    }

    constructor(rodovia: String, concessionaria: String, telefone: String, id: String, dia: String) : super(rodovia, concessionaria, telefone, id) {
        this.dia = dia
    }
}

