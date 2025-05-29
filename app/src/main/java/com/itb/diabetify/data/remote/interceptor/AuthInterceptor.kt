package com.itb.diabetify.data.remote.interceptor

import android.util.Log
import com.itb.diabetify.domain.manager.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            tokenManager.getToken()
        }

        val request = chain.request().newBuilder()
        token?.let {
            request.addHeader("Authorization", "Bearer $it")
            Log.d("AuthInterceptor", "Added Bearer token to request: ${chain.request().url}")
        } ?: Log.d("AuthInterceptor", "No token available for request: ${chain.request().url}")

        return chain.proceed(request.build())
    }
} 