package com.visualeap.aliforreddit.data.repository.post

import android.app.Application
import android.preference.PreferenceManager
import dagger.Reusable
import javax.inject.Inject

//This is more like a nullable string store.
//TODO remove
@Reusable
class NextPageKeyStore @Inject constructor(app: Application): KeyValueStore<String?> {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(app)

    override fun get(key: String, defaultValue: String?): String? {
        return preferences.getString(key, null)
    }

    override fun put(key: String, value: String?) {
        preferences.edit().putString(key, value).apply()
    }
}