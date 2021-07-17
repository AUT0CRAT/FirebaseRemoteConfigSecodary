package dev.parkar.firebasedemo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dev.parkar.firebasedemo.databinding.MainBinding
import dev.parkar.myapplication.FirebaseToggleConfig
import dev.parkar.myapplication.SecondaryFirebaseConfiguration
import dev.parkar.myapplication.SecondaryFirebaseToggleInitializer

class MainActivity : ComponentActivity() {
    private lateinit var binding : MainBinding
    private val secondaryFirebaseConfig by lazy {
        FirebaseToggleConfig(secondaryToggleInitializer)
    }

    private val secondaryToggleInitializer by lazy {
        SecondaryFirebaseToggleInitializer(this, SecondaryFirebaseConfiguration())
    }

    private val primaryRemoteConfig by lazy {
        FirebaseRemoteConfig.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.initPrimary.setOnClickListener {
            FirebaseApp.initializeApp(this)
            primaryRemoteConfig.fetchAndActivate()
                .addOnSuccessListener {
                    Log.d("Primary", "Is activate success : $it")
                }.addOnFailureListener {
                    Log.e("Primary", "Init failed", it)
                }
        }

        binding.initSecondary.setOnClickListener {
            secondaryToggleInitializer.initIfRequired()
        }

        binding.fetchPrimary.setOnClickListener {
            val value = primaryRemoteConfig.getString("primary_key_1")
            binding.primaryKey.text= "Primary value = '$value'"
        }

        binding.fetchSecondary.setOnClickListener {
            val value = secondaryFirebaseConfig.getString("secondary_key_1", "Not found")
            binding.secondaryKey.text= "Secondary value = '$value'"
        }
    }
}
