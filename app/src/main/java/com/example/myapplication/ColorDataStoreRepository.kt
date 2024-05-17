import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class MyPreferenceRepository private constructor(private val dataStore: DataStore<Preferences>) {
    val redValue: Flow<Float> = dataStore.data.map { prefs ->
        prefs[KEY_RED_VALUE]?.toFloat() ?: 0f
    }.distinctUntilChanged()

    val greenValue: Flow<Float> = dataStore.data.map { prefs ->
        prefs[KEY_GREEN_VALUE]?.toFloat() ?: 0f
    }.distinctUntilChanged()

    val blueValue: Flow<Float> = dataStore.data.map { prefs ->
        prefs[KEY_BLUE_VALUE]?.toFloat() ?: 0f
    }.distinctUntilChanged()


    val redProgress: Flow<Int> = dataStore.data.map { prefs ->
        prefs[KEY_RED_PROGRESS] ?: 0
    }.distinctUntilChanged()

    val greenProgress: Flow<Int> = dataStore.data.map { prefs ->
        prefs[KEY_GREEN_PROGRESS] ?: 0
    }.distinctUntilChanged()

    val blueProgress: Flow<Int> = dataStore.data.map { prefs ->
        prefs[KEY_BLUE_PROGRESS] ?: 0
    }.distinctUntilChanged()

  //  val colorValue: Flow<Int> = dataStore.data.map { prefs ->
   //     prefs[KEY_COLOR_VALUE] ?: 0
   // }.distinctUntilChanged()

    val editTextValue1: Flow<String> = dataStore.data.map { prefs ->
        prefs[KEY_EDIT_TEXT_VALUE_1] ?: ""
    }.distinctUntilChanged()

    val editTextValue2: Flow<String> = dataStore.data.map { prefs ->
        prefs[KEY_EDIT_TEXT_VALUE_2] ?: ""
    }.distinctUntilChanged()

    val editTextValue3: Flow<String> = dataStore.data.map { prefs ->
        prefs[KEY_EDIT_TEXT_VALUE_3] ?: ""
    }.distinctUntilChanged()

    val switchState1: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_SWITCH_STATE_1] ?: false
    }.distinctUntilChanged()

    val switchState2: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_SWITCH_STATE_2] ?: false
    }.distinctUntilChanged()

    val switchState3: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_SWITCH_STATE_3] ?: false
    }.distinctUntilChanged()


    private suspend fun saveStringValue(value: String, key: Preferences.Key<String>) {
        dataStore.edit { prefs ->
            prefs[key] = value
        }
    }
    suspend fun saveColor(color: Int) {
        saveIntValue(color, KEY_COLOR_VALUE)
    }
    private suspend fun saveIntValue(value: Int, key: Preferences.Key<Int>) {
        dataStore.edit { prefs ->
            prefs[key] = value
        }
    }

    private suspend fun saveFloatValue(value: Float, key: Preferences.Key<Float>) {
        dataStore.edit { prefs ->
            prefs[key] = value
        }
    }
    private suspend fun saveBooleanValue(value: Boolean, key: Preferences.Key<Boolean>) {
        dataStore.edit { prefs ->
            prefs[key] = value
        }
    }

    suspend fun saveSwitchState(state: Boolean, index: Int) {
        val key: Preferences.Key<Boolean> = when (index) {
            1 -> KEY_SWITCH_STATE_1
            2 -> KEY_SWITCH_STATE_2
            3 -> KEY_SWITCH_STATE_3
            else -> throw NoSuchFieldException("Invalid switch index: $index")
        }
        saveBooleanValue(state, key)
    }

    suspend fun saveEditTextValue(value: String, index: Int) {
        val key: Preferences.Key<String> = when (index) {
            1 -> KEY_EDIT_TEXT_VALUE_1
            2 -> KEY_EDIT_TEXT_VALUE_2
            3 -> KEY_EDIT_TEXT_VALUE_3
            else -> throw NoSuchFieldException("Invalid EditText index: $index")
        }
        saveStringValue(value, key)
    }

    suspend fun saveProgressValue(value: Int, index: Int) {
        val key: Preferences.Key<Int> = when (index) {
            1 -> KEY_RED_PROGRESS
            2 -> KEY_GREEN_PROGRESS
            3 -> KEY_BLUE_PROGRESS
           // 4 -> KEY_VIEW_PROGRESS
            else -> throw NoSuchFieldException("Invalid progress index: $index")
        }
        this.saveIntValue(value, key)
    }

    suspend fun saveValue(value: Float, index: Int) {
        val key: Preferences.Key<Float> = when (index) {
            1 -> KEY_RED_VALUE
            2 -> KEY_GREEN_VALUE
            3 -> KEY_BLUE_VALUE
            else -> throw NoSuchFieldException("Invalid value index: $index")
        }
        this.saveFloatValue(value, key)
    }

    companion object {
        private const val DATA_STORE_NAME = "my_color_data_store"
        private val KEY_RED_VALUE = floatPreferencesKey("red_value")
        private val KEY_GREEN_VALUE = floatPreferencesKey("green_value")
        private val KEY_BLUE_VALUE = floatPreferencesKey("blue_value")
        private val KEY_RED_PROGRESS = intPreferencesKey("red_progress")
        private val KEY_GREEN_PROGRESS = intPreferencesKey("green_progress")
        private val KEY_BLUE_PROGRESS = intPreferencesKey("blue_progress")
        //private val KEY_VIEW_PROGRESS = intPreferencesKey("view_progress")
        //private const val DEFAULT_COLOR = android.graphics.Color.BLACK
        private val KEY_COLOR_VALUE = intPreferencesKey("colorValue")
        private val KEY_EDIT_TEXT_VALUE_1 = stringPreferencesKey("editTextValue1")
        private val KEY_EDIT_TEXT_VALUE_2 = stringPreferencesKey("editTextValue2")
        private val KEY_EDIT_TEXT_VALUE_3 = stringPreferencesKey("editTextValue3")

        private val KEY_SWITCH_STATE_1 = booleanPreferencesKey("switchState1")
        private val KEY_SWITCH_STATE_2 = booleanPreferencesKey("switchState2")
        private val KEY_SWITCH_STATE_3 = booleanPreferencesKey("switchState3")



        private var INSTANCE: MyPreferenceRepository? = null

        fun initialize(context: Context){
            if( INSTANCE == null){
                val  dataStore = PreferenceDataStoreFactory.create {
                    context.preferencesDataStoreFile(DATA_STORE_NAME)
                }
                INSTANCE = MyPreferenceRepository(dataStore)
            }
        }

        fun get(): MyPreferenceRepository {
            return INSTANCE ?: throw IllegalStateException("My Data hasnt been initialized yet")
        }
    }
}
