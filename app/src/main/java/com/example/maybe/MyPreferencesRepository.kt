package com.example.maybe

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class MyPreferencesRepository private constructor(private val dataStore: DataStore<Preferences>) {

    val redValueFlow: Flow<String> = dataStore.data.map { prefs ->
        prefs[KEY_RED_VALUE] ?: "0"
    }.distinctUntilChanged()

    val greenValueFlow: Flow<String> = dataStore.data.map { prefs ->
        prefs[KEY_GREEN_VALUE] ?: "0"
    }.distinctUntilChanged()

    val blueValueFlow: Flow<String> = dataStore.data.map { prefs ->
        prefs[KEY_BLUE_VALUE] ?: "0"
    }.distinctUntilChanged()

    private suspend fun saveStringValue(value: String, key: Preferences.Key<String>) {
        dataStore.edit { prefs ->
            prefs[key] = value
        }
    }

    suspend fun saveInput(value: String, index: Int) {
        val key: Preferences.Key<String> = when (index) {
            1 -> KEY_RED_VALUE
            2 -> KEY_GREEN_VALUE
            3 -> KEY_BLUE_VALUE
            else -> throw IllegalArgumentException("Invalid input index: $index")
        }
        saveStringValue(value, key)
    }


    companion object {
        private const val DATA_STORE_FILE_NAME = "myColors"
        private val KEY_RED_VALUE = stringPreferencesKey("redValue")
        private val KEY_GREEN_VALUE = stringPreferencesKey("greenValue")
        private val KEY_BLUE_VALUE = stringPreferencesKey("blueValue")
        private var INSTANCE: MyPreferencesRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                val dataStore = PreferenceDataStoreFactory.create {
                    context.preferencesDataStoreFile(DATA_STORE_FILE_NAME)
                }
                INSTANCE = MyPreferencesRepository(dataStore)
            }
        }

        fun get(): MyPreferencesRepository {
            return INSTANCE ?: throw IllegalStateException("MyPreferencesRepository must be initialized first.")
        }
    }
}
