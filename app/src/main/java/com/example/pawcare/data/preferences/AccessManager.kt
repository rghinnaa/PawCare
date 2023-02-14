package com.example.pawcare.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.pawcare.utils.Const.AUTH_PREFIX
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class AccessManager(private val context: Context) {
    suspend fun setAccess(access: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.accessKey] = "$AUTH_PREFIX $access"
        }
    }

    fun setAccess(tokenAccess: String, scope: CoroutineScope) = scope.launch {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.accessKey] = "$AUTH_PREFIX $tokenAccess"
        }
    }

    suspend fun clearAccess() {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.accessKey] = ""
        }
    }

    fun clearAccess(scope: CoroutineScope) = scope.launch {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.accessKey] = ""
        }
    }

    val access: Flow<String> = context.dataStore.data
            .catch { throwable ->
                emit(emptyPreferences())
                Timber.e(throwable)
            }.map { preferences ->
                preferences[PreferencesKey.accessKey] ?: ""
            }

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = PreferencesKey.AUTH_PREFERENCES_KEY.uppercase(Locale.ROOT)
        )
    }

    private object PreferencesKey {
        const val AUTH_PREFERENCES_KEY = "auth_preferences"
        const val TOKEN_ACCESS_REF = "token_access_key"

        val accessKey = stringPreferencesKey(TOKEN_ACCESS_REF)
    }
}
