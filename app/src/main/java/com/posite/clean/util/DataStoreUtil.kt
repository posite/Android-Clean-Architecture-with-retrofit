package com.posite.clean.util

import android.content.Context
import android.util.Log
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
        val (iv, encryptedData) = DataEncryptUtil.encrypt(accessToken)
        context.dataStore.edit { preferences ->
            Log.d("token save", encryptedData.toBase64() + ":" + iv.toBase64())
            preferences[ACCESS_TOKEN_KEY] = encryptedData.toBase64() + ":" + iv.toBase64()
        }
    }

    suspend fun loadAccessToken(): String {
        val preferences = context.dataStore.data.first()
        val encrypted = preferences[ACCESS_TOKEN_KEY] ?: return ""
        Log.d("token fetch", encrypted)
        val (encryptedData, iv) = encrypted.split(":").map { it.fromBase64() }
        return DataEncryptUtil.decrypt(iv, encryptedData)
    }

    suspend fun saveRefreshToken(accessToken: String) {
        val (iv, encryptedData) = DataEncryptUtil.encrypt(accessToken)
        context.dataStore.edit { preferences ->
            Log.d("refresh save", encryptedData.toBase64() + ":" + iv.toBase64())
            preferences[REFRESH_TOKEN_KEY] = encryptedData.toBase64() + ":" + iv.toBase64()
        }
    }

    suspend fun loadRefreshToken(): String {
        val preferences = context.dataStore.data.first()
        val encrypted = preferences[REFRESH_TOKEN_KEY] ?: return ""
        Log.d("refresh fetch", encrypted)
        val (encryptedData, iv) = encrypted.split(":").map { it.fromBase64() }
        return DataEncryptUtil.decrypt(iv, encryptedData)
    }

    suspend fun saveUserNickName(nickname: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NICKNAME_KEY] = nickname
        }
    }

    suspend fun loadUserNickName(): String {
        val preferences = context.dataStore.data.first()
        return preferences[USER_NICKNAME_KEY] ?: return ""
    }

    suspend fun saveUserProfile(profile: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_PROFILE_KEY] = profile
        }
    }

    suspend fun loadUserProfile(): String {
        val preferences = context.dataStore.data.first()
        return preferences[USER_PROFILE_KEY] ?: return ""
    }

    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val USER_NICKNAME_KEY = stringPreferencesKey("user_nickname")
        private val USER_PROFILE_KEY = stringPreferencesKey("user_profile")
    }

    private fun ByteArray.toBase64(): String =
        android.util.Base64.encodeToString(this, android.util.Base64.NO_WRAP)

    private fun String.fromBase64(): ByteArray =
        android.util.Base64.decode(this, android.util.Base64.NO_WRAP)
}