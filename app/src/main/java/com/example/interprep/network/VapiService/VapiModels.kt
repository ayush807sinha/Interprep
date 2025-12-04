package com.example.interprep.network.VapiService

data class VapiTtsRequest(val text: String, val voice: String? = null)
data class VapiTtsResponse(val audioUrl: String?) 
