package com.example.basket

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.basket.ui.MainApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        WindowCompat.setDecorFitsSystemWindows(window, false)
         sharedPreferences = EncryptedSharedPreferences.create(
            "preferences",
             MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        AppBase.scale = sharedPreferences.getInt("SCALE", 0)
        setContent { MainApp() }
    }

    override fun onPause() {
        super.onPause()
        sharedPreferences.edit().putInt("SCALE", AppBase.scale).apply()
    }
}
