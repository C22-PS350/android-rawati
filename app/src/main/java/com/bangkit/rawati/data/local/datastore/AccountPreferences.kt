package com.bangkit.rawati.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AccountPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    fun getUser(): Flow<Account> {
        return dataStore.data.map {
            Account(
                it[NAME] ?: "",
                it[EMAIL] ?: "",
                it[PASSWORD] ?: "",
                it[USERID] ?: "",
                it[TOKEN] ?: "",
                it[STATE] ?: false
            )
        }
    }

    suspend fun saveUser(user: Account) {
        dataStore.edit {
            it[NAME] = user.name
            it[EMAIL] = user.email
            it[PASSWORD] = user.password
            it[USERID] = user.userId
            it[TOKEN] = user.token
            it[STATE] = user.isLogin
        }
    }

    suspend fun signout() {
        dataStore.edit {
            it.clear()
        }
    }

    private val REMEMBER_KEY = booleanPreferencesKey("remember_setting")

    fun getRemember(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[REMEMBER_KEY] ?: false
        }
    }

    suspend fun saveRemember(isRememberActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[REMEMBER_KEY] = isRememberActive
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AccountPreferences? = null

        private val NAME = stringPreferencesKey("name")
        private val EMAIL = stringPreferencesKey("email")
        private val PASSWORD = stringPreferencesKey("password")
        private val USERID = stringPreferencesKey("userId")
        private val TOKEN = stringPreferencesKey("token")
        private val STATE = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>): AccountPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = AccountPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}