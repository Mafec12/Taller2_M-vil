package com.example.taller2movil.datos

data class Foto(
    val nombre: String,
    val uri: String,
    val latitud: Double? = null,
    val longitud: Double? = null
)
