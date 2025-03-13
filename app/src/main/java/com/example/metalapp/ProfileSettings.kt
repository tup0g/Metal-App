package com.example.metalapp

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.app.ActivityOptions

class ProfileSettings : AppCompatActivity() {
    private lateinit var profileMain: ConstraintLayout
    private lateinit var header: ConstraintLayout
    private lateinit var imageView2: ImageView
    private lateinit var imageView3: ImageView
    private lateinit var profileBanner: ConstraintLayout
    private lateinit var profileImage: ImageView
    private lateinit var tvUsername: TextView
    private lateinit var textView3: TextView
    private lateinit var usernameContainer: ConstraintLayout
    private lateinit var tvPhone2: TextView
    private lateinit var imageView4: ImageView
    private lateinit var btnEdit2: Button
    private lateinit var birthdayContainer: ConstraintLayout
    private lateinit var imageView5: ImageView
    private lateinit var tvPhone3: TextView
    private lateinit var btnEdit3: Button
    private lateinit var descriptionContainer: ConstraintLayout
    private lateinit var btnEdit4: Button
    private lateinit var imageView7: ImageView
    private lateinit var tvPhone4: TextView
    private lateinit var footer: ConstraintLayout
    private lateinit var imageView: ImageView
    private lateinit var imageViewChats: ImageView
    private lateinit var imageViewProfile: ImageView
    private lateinit var imageViewSettings: ImageView
    private lateinit var btnLogin: Button
    private lateinit var textView2: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_settings)

        initViews()

        ViewCompat.setOnApplyWindowInsetsListener(profileMain) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupClickListeners()
        updateUsernameFromLogin()
    }

    private fun initViews() {
        profileMain = findViewById(R.id.ProfileMain)
        header = findViewById(R.id.header)
        imageView2 = findViewById(R.id.imageView2)
        imageView3 = findViewById(R.id.imageView3)
        profileBanner = findViewById(R.id.profile_banner)
        profileImage = findViewById(R.id.profile_image)
        tvUsername = findViewById(R.id.tv_username)
        textView3 = findViewById(R.id.textView3)
        usernameContainer = findViewById(R.id.username_container)
        tvPhone2 = findViewById(R.id.tv_phone2)
        imageView4 = findViewById(R.id.imageView4)
        btnEdit2 = findViewById(R.id.btn_edit2)
        birthdayContainer = findViewById(R.id.birthday_container)
        imageView5 = findViewById(R.id.imageView5)
        tvPhone3 = findViewById(R.id.tv_phone3)
        btnEdit3 = findViewById(R.id.btn_edit3)
        descriptionContainer = findViewById(R.id.description_container)
        btnEdit4 = findViewById(R.id.btn_edit4)
        imageView7 = findViewById(R.id.imageView7)
        tvPhone4 = findViewById(R.id.tv_phone4)
        footer = findViewById(R.id.footer)
        imageView = findViewById(R.id.imageView)
        imageViewChats = findViewById(R.id.imageView_chats)
        imageViewProfile = findViewById(R.id.imageView_profile)
        imageViewSettings = findViewById(R.id.imageView_settings)
        btnLogin = findViewById(R.id.btn_login)
        textView2 = findViewById(R.id.textView2)
    }

    private fun setupClickListeners() {
        btnEdit2.setOnClickListener {
        }

        btnEdit3.setOnClickListener {
        }

        btnEdit4.setOnClickListener {
        }

        btnLogin.setOnClickListener {
            val options = ActivityOptions.makeCustomAnimation(
                this,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            finish()
            startActivity(Intent(this, MainActivity::class.java), options.toBundle())
        }

        imageViewChats.setOnClickListener {
            val options = ActivityOptions.makeCustomAnimation(
                this,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            startActivity(Intent(this, ChatListActivity::class.java), options.toBundle())
        }

        imageViewProfile.setOnClickListener {
        }

        imageViewSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun updateUsernameFromLogin() {
        val username = intent.getStringExtra("username") ?: "User"
        tvUsername.text = username
    }
}