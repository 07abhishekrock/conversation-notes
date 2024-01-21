package com.ajrocks.conversationnotes.login.api.interfaces

import kotlinx.serialization.Serializable

data class LoginRequestPayload(val emailId: String)

@Serializable
data class LoginSuccessResponseDto(val email: String, val username: String)

