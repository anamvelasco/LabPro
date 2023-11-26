package com.ana.labpro.model

data class User(
    var uid: String? = null,
    var namer: String? = null,
    var lastnamer: String? = null,
    var identir: String? = null,
    var programar: String? = null,
    var email: String? = null,
    var numReservas: Int = 0,
    var role: String = "user"
)
