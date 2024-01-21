package com.ajrocks.conversationnotes.login.api.interfaces

interface LoginApi {
    suspend fun login(payload: LoginRequestPayload): LoginSuccessResponseDto
}