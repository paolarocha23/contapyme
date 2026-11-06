package com.example.contapyme.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "contapyme_session")

class SessionManager(private val context: Context) {
    companion object {
        private val KEY_TOKEN = stringPreferencesKey("auth_token")
        private val KEY_USER_NAME = stringPreferencesKey("user_name")
        private val KEY_USER_EMAIL = stringPreferencesKey("user_email")
        private val KEY_ROL_NAME = stringPreferencesKey("rol_name")
        private val KEY_BASE_URL = stringPreferencesKey("base_url")
        private const val DEFAULT_API_ORIGIN = "http://10.0.2.2:5000"

        fun normalizeApiOrigin(input: String): String {
            var url = input.trim()
            // Remove trailing /api or /api/ if user included it
            if (url.endsWith("/api/", ignoreCase = true)) {
                url = url.dropLast(5)
            } else if (url.endsWith("/api", ignoreCase = true)) {
                url = url.dropLast(4)
            }
            // Remove trailing slash for clean origin
            while (url.endsWith("/")) {
                url = url.dropLast(1)
            }
            return url
        }

        fun buildApiBaseUrl(origin: String): String {
            val clean = normalizeApiOrigin(origin)
            return "$clean/api/"
        }
    }

    val token: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[KEY_TOKEN]
    }

    /** The API origin (before /api), e.g. http://10.0.2.2:5000 */
    val apiOrigin: Flow<String> = context.dataStore.data.map { preferences ->
        val stored = preferences[KEY_BASE_URL] ?: DEFAULT_API_ORIGIN
        normalizeApiOrigin(stored)
    }

    /** Full Retrofit base URL including /api/, e.g. http://10.0.2.2:5000/api/ */
    val apiBaseUrl: Flow<String> = context.dataStore.data.map { preferences ->
        val stored = preferences[KEY_BASE_URL] ?: DEFAULT_API_ORIGIN
        buildApiBaseUrl(stored)
    }

    val userName: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[KEY_USER_NAME]
    }

    val userEmail: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[KEY_USER_EMAIL]
    }

    val rolName: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[KEY_ROL_NAME]
    }

    suspend fun saveSession(token: String, userName: String? = null, email: String? = null, rolName: String? = null) {
        context.dataStore.edit { preferences ->
            preferences[KEY_TOKEN] = token
            userName?.let { preferences[KEY_USER_NAME] = it }
            email?.let { preferences[KEY_USER_EMAIL] = it }
            rolName?.let { preferences[KEY_ROL_NAME] = it }
        }
    }

    suspend fun setApiOrigin(url: String) {
        val normalized = normalizeApiOrigin(url)
        context.dataStore.edit { preferences ->
            preferences[KEY_BASE_URL] = normalized
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.remove(KEY_TOKEN)
            preferences.remove(KEY_USER_NAME)
            preferences.remove(KEY_USER_EMAIL)
            preferences.remove(KEY_ROL_NAME)
        }
    }
}
