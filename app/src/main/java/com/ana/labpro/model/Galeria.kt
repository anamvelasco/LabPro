package com.ana.labpro.model

data class Galeria(
    var id: String? = null,
    var name: String? = null,
    var ownerUid: String? = null,
    var ownerRole: String = "user",
    var urlPicture: String? = null
)