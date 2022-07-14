package com.example.cally

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cally.databinding.LogoutLayoutBinding
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LogoutFragment : Fragment() {
    lateinit var binding: LogoutLayoutBinding
    private var auth: FirebaseAuth? = null
    var googleSignInClient: GoogleSignInClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LogoutLayoutBinding.inflate(LayoutInflater.from(context), container, false)
        binding.tvLogoutTitle.setOnClickListener {
                view -> FirebaseAuth.getInstance().signOut()
            Firebase.auth.signOut()
            googleSignInClient?.signOut()?.addOnCompleteListener {
                activity?.finish()
            }
            startActivity(Intent(context, SplashActivity::class.java))
        }
        binding.ivLogoutIcon.setOnClickListener {
                view -> FirebaseAuth.getInstance().signOut()
            Firebase.auth.signOut()
            googleSignInClient?.signOut()?.addOnCompleteListener {
                activity?.finish()
            }
            startActivity(Intent(context, SplashActivity::class.java))
        }

        return binding.root
    }



}
