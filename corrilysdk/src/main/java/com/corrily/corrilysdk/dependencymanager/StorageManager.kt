import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "CorrilySDK")

class StorageManager(private val context: Context) {


  fun write(key: String, value: String) {
    val dataStoreKey = stringPreferencesKey(key)
    runBlocking {
      context.dataStore.edit { settings ->
        settings[dataStoreKey] = value
      }
    }
  }

  fun read(key: String): String? {
    val dataStoreKey = stringPreferencesKey(key)
    var value: String?
    runBlocking {
      value = context.dataStore.data.first()[dataStoreKey]
    }
    return value
  }
}