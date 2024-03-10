package com.posite.clean.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "access_data_store")

class DataStoreUtil @Inject constructor(private val context: Context) {
    suspend fun saveAccessToken(accessToken: String) {

        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
        }
    }

    suspend fun fetchAccessToken(): String {
        val preferences = context.dataStore.data.first()
        return preferences[ACCESS_TOKEN_KEY] ?: ""

    }

    suspend fun saveRefreshToken(accessToken: String) {

        context.dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = accessToken
        }
    }

    suspend fun fetchRefreshToken(): String {
        val preferences = context.dataStore.data.first()
        return preferences[REFRESH_TOKEN_KEY] ?: ""

    }

    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }

}