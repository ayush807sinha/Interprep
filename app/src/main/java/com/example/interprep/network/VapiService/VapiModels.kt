package com.example.interprep.network.VapiService

data class VapiTtsRequest(val text: String, val voice: String? = null)
data class VapiTtsResponse(val audioUrl: String?) // change if Vapi returns bytes/base64 or stream
