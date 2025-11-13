package com.example.interprep.nav

import kotlinx.serialization.Serializable


sealed class Routes{

    @Serializable
    object Auth

    @Serializable
    object Home

    @Serializable
    object AiBot

    @Serializable
    object Qestiongenerator

    @Serializable
    object resumebuilder

    @Serializable
    object ATSchecker

    @Serializable
    object CareerCoach

}

