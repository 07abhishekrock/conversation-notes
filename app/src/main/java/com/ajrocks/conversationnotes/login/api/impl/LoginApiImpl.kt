package com.ajrocks.conversationnotes.login.api.impl

import com.ajrocks.conversationnotes.login.api.interfaces.LoginApi
import com.ajrocks.conversationnotes.login.api.interfaces.LoginRequestPayload
import com.ajrocks.conversationnotes.login.api.interfaces.LoginSuccessResponseDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import javax.inject.Inject

class LoginApiImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
): LoginApi {

    override suspend fun login(payload: LoginRequestPayload): LoginSuccessResponseDto {
        return supabaseClient.from("user").select(
            columns = Columns.list("email", "username")
        ).decodeSingle<LoginSuccessResponseDto>()
    }

}