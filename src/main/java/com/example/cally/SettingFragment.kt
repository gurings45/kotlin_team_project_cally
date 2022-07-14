package com.example.cally

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.cally.databinding.FragmentSettingBinding
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth


class SettingFragment : PreferenceFragmentCompat() {
    lateinit var binding: FragmentSettingBinding
    private var auth: FirebaseAuth? = null
    var googleSignInClient: GoogleSignInClient? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting, rootKey)
    }


}