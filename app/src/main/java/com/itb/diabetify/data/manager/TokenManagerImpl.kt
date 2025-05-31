package com.itb.diabetify.data.manager

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.itb.diabetify.domain.manager.TokenManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : TokenManager {
    private val masterKeyAlias = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
    .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "auth_token",
        masterKeyAlias,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override suspend fun saveToken(token: String) = withContext(Dispatchers.IO) {
        sharedPreferences.edit().putString(KEY_AUTH_TOKEN, token).apply()
    }

    override suspend fun getToken(): String? = withContext(Dispatchers.IO) {
        sharedPreferences.getString(KEY_AUTH_TOKEN, null)
    }

    override suspend fun clearToken() = withContext(Dispatchers.IO) {
        sharedPreferences.edit().remove(KEY_AUTH_TOKEN).apply()
    }

    override suspend fun isLoggedIn(): Boolean = withContext(Dispatchers.IO) {
        !getToken().isNullOrEmpty()
    }

    companion object {
        private const val KEY_AUTH_TOKEN = "auth_token"
    }
}