package com.intacta.sosviagens.model.Beans

open class Road {
    var rodovia: String? = null
    var concessionaria: String? = null
    var telefone: String? = null
    open var id: String? = null

    constructor() {}

    constructor(rodovia: String, concessionaria: String, telefone: String, id: String) {
        this.rodovia = rodovia
        this.concessionaria = concessionaria
        this.telefone = telefone
        this.id = id
    }
}
