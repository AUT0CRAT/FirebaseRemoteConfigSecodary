package dev.parkar.myapplication

import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dev.parkar.myapplication.SecondaryFirebaseToggleInitializer.Companion.FirebaseAppName

class FirebaseToggleConfig(
    private val firebaseToggleInitializer: SecondaryFirebaseToggleInitializer
) {

    private val remoteConfig: FirebaseRemoteConfig by lazy { FirebaseRemoteConfig.getInstance(
        FirebaseApp.getInstance(FirebaseAppName)) }

    fun getString(key: String, default: String): String {
        firebaseToggleInitializer.initIfRequired()
        val value = remoteConfig.getString(key)
        return if (value == FirebaseRemoteConfig.DEFAULT_VALUE_FOR_STRING) default else value
    }

    fun getLong(key: String, default: Long): Long {
        firebaseToggleInitializer.initIfRequired()
        val value = remoteConfig.getLong(key)
        return if (value == FirebaseRemoteConfig.DEFAULT_VALUE_FOR_LONG) default else value
    }
}