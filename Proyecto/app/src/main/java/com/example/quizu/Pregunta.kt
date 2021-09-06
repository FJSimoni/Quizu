package com.example.quizu

data class Pregunta (

    val idPregunta: String,
    val enunciado: String,
    val numeroRespuestas: String,
    val opcion1: String,
    val opcion2: String,
    val opcion3: String,
    val opcion4: String,
    val correcta: Int
)