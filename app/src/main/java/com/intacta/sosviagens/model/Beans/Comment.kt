package com.intacta.sosviagens.model.Beans

class Comment : Road {


    var comment: String? = null
    var user: String? = null
    var data: String? = null
    var cortesy: String? = null

    var time: String? = null

    var opinion: String? = null

    override var id: String? = null


    constructor(comment: String, user: String, data: String, cortesy: String, time: String, opinion: String, id: String) {
        this.comment = comment
        this.user = user
        this.data = data
        this.cortesy = cortesy
        this.time = time
        this.opinion = opinion
        this.id = id
    }


    constructor() {}


}
