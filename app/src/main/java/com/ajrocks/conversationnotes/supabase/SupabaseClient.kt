package com.ajrocks.conversationnotes.supabase

import com.ajrocks.conversationnotes.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

@Module
@InstallIn(SingletonComponent::class)
object CustomSupabaseClient {

    @Provides
    fun provideSupabaseClient(): SupabaseClient{
      return createSupabaseClient(
          supabaseKey = BuildConfig.SUPABASE_KEY,
          supabaseUrl = BuildConfig.SUPABASE_URL
      ) {
          install(Postgrest)
      }
    }
}