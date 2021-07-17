package dev.parkar.myapplication

import androidx.annotation.StringRes

sealed class FirebaseConfiguration(
    @StringRes val applicationId: Int,
    @StringRes val apiKey: Int,
    @StringRes val databaseUrl: Int,
    @StringRes val storageBucket: Int,
    @StringRes val projectId: Int,
    @StringRes val gcmSenderId: Int
)

class SecondaryFirebaseConfiguration :
    FirebaseConfiguration(
        R.string.appIdDebug,
        R.string.apiKeyDebug,
        R.string.databaseUrlDebug,
        R.string.storeageBucketDebug,
        R.string.projectIdDebug,
        R.string.gcmSenderIdDebug
    )