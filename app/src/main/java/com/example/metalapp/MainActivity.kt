package com.example.metalapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.Gravity
import kotlin.random.Random
import android.widget.Toast
import android.content.Intent
import android.view.View
import android.app.ActivityOptions

class MainActivity : AppCompatActivity() {

    private lateinit var metalLogo: ImageView
    private lateinit var metalAppName: TextView
    private lateinit var metalDescription: TextView
    private lateinit var etPhone: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnEdit: Button
    private lateinit var tvLoginStatus: TextView

    private val adminPhone = "admin"
    private val adminPassword = "123"

    private val descriptions = listOf(
        "Metal - New Social App 2025",
        "Did you know that it was created by 3 people?",
        "2 file was thrown into gitignore",
        "I'm tired of making an application",
        "Every time I came here I cried",
        "My favorite xml",
        "Database??"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val mainLayout = findViewById<ConstraintLayout>(R.id.main)

        initViews()

        btnEdit.setOnClickListener {
            val tempText = btnLogin.text.toString()
            btnLogin.text = btnEdit.text
            btnEdit.text = tempText
        }

        btnLogin.setOnClickListener {
            val phone = etPhone.text.toString()
            val password = etPassword.text.toString()

            if (btnLogin.text == "Login") {
                if (phone == adminPhone && password == adminPassword) {
                    val intent = Intent(this, ProfileSettings::class.java)
                    intent.putExtra("username", adminPhone)

                    val options = ActivityOptions.makeCustomAnimation(
                        this,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                    )

                    startActivity(intent, options.toBundle())
                } else if (phone.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Введіть номер телефону і пароль", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Неправильний логін або пароль", Toast.LENGTH_SHORT).show()
                }
            } else {
                if (phone == adminPhone) {
                    tvLoginStatus.visibility = View.VISIBLE
                    tvLoginStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
                    tvLoginStatus.text = "Цей логін вже використовується"
                } else if (phone.isEmpty() || password.isEmpty()) {
                    tvLoginStatus.visibility = View.VISIBLE
                    tvLoginStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
                    tvLoginStatus.text = "Заповніть всі поля"
                } else {
                    tvLoginStatus.visibility = View.VISIBLE
                    tvLoginStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
                    tvLoginStatus.text = "Користувач успішно зареєстрований"
                }
            }
        }


        setRandomDescription()

        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initViews() {
        metalLogo = findViewById(R.id.MetalLogo)
        metalAppName = findViewById(R.id.MetalAppName)
        metalDescription = findViewById(R.id.MetalDescription)
        etPhone = findViewById(R.id.et_phone)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)
        btnEdit = findViewById(R.id.btn_edit)
        tvLoginStatus = findViewById(R.id.tv_login_status)
        tvLoginStatus.visibility = View.INVISIBLE

        metalDescription.gravity = Gravity.CENTER_HORIZONTAL
    }

    private fun setRandomDescription() {
        val randomIndex = Random.nextInt(descriptions.size)
        metalDescription.text = descriptions[randomIndex]
    }
}