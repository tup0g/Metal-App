package com.example.metalapp

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ChatListActivity : AppCompatActivity() {
    private lateinit var imageViewChats: ImageView
    private lateinit var imageViewProfile: ImageView
    private lateinit var imageViewSettings: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.chat_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ChatList)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imageViewChats = findViewById(R.id.imageView_chats)
        imageViewProfile = findViewById(R.id.imageView_profile)
        imageViewSettings = findViewById(R.id.imageView_settings)

        imageViewChats.setOnClickListener {
        }

        imageViewProfile.setOnClickListener {
            val options = ActivityOptions.makeCustomAnimation(
                this,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            startActivity(Intent(this, ProfileSettings::class.java), options.toBundle())
            finish()
        }

        imageViewSettings.setOnClickListener {
            val options = ActivityOptions.makeCustomAnimation(
                this,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            startActivity(Intent(this, SettingsActivity::class.java), options.toBundle())
            finish()
        }
    }
}