package dev.parkar.myapplication

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class SecondaryFirebaseToggleInitializer(
    private val context: Context,
    private val firebaseConfiguration: FirebaseConfiguration
) {

    fun initIfRequired() {
        initialize()
    }

    private var isInitialized = false

    private fun initialize() {
        synchronized(isInitialized){
            if (!isInitialized) {
                initializeFirebase()
                setUpRemoteConfig()
                isInitialized = true
            }
        }
    }

    private fun initializeFirebase() {
        try {
            val options = FirebaseOptions.Builder()
                .setApplicationId(context.getString(firebaseConfiguration.applicationId))
                .setApiKey(context.getString(firebaseConfiguration.apiKey))
                .setDatabaseUrl(context.getString(firebaseConfiguration.databaseUrl))
                .setStorageBucket(context.getString(firebaseConfiguration.storageBucket))
                .setProjectId(context.getString(firebaseConfiguration.projectId))
                .setGcmSenderId(context.getString(firebaseConfiguration.gcmSenderId))
                .build()
            FirebaseApp.initializeApp(context, options, FirebaseAppName)
        } catch (exception: Exception) {
            Log.e(FeatureConfigTag, "Exception initializing firebase", exception)
        }
    }

    private fun setUpRemoteConfig() {
        val remoteConfig = FirebaseRemoteConfig.getInstance(FirebaseApp.getInstance(FirebaseAppName))
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0L)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings).addOnCompleteListener {
            Log.d(FeatureConfigTag, "Config Setting: ${it.isSuccessful}")
        }
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d(FeatureConfigTag, "Config params updated: $updated")
                } else {
                    Log.e(FeatureConfigTag, "Unable to Update Config")
                }
            }
            .addOnFailureListener  {
                Log.e(FeatureConfigTag, "Failed to setup remote", it)
            }
    }

    companion object {
        const val FirebaseAppName = "SecondaryRemote"
        private const val FeatureConfigTag = "Feature Config"
    }

}