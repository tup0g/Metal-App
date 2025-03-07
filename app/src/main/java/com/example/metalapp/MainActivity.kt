package com.example.metalapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var metalLogo: ImageView
    private lateinit var metalAppName: TextView
    private lateinit var metalDescription: TextView
    private lateinit var etPhone: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button

    private val adminPhone = "+3800000000"
    private val adminPassword = "123"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        metalLogo = findViewById(R.id.MetalLogo)
        metalAppName = findViewById(R.id.MetalAppName)
        metalDescription = findViewById(R.id.MetalDescription)
        etPhone = findViewById(R.id.et_phone)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)
        btnRegister = findViewById(R.id.btn_register)

        btnLogin.setOnClickListener {
            validateLogin()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.vertical)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun validateLogin() {
        val phone = etPhone.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (phone == adminPhone && password == adminPassword) {
            showAdminLoginDialog()
        } else {
            Toast.makeText(this, "Неверный номер телефона или пароль", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAdminLoginDialog() {
        AlertDialog.Builder(this)
            .setTitle("Успешный вход")
            .setMessage("Вы вошли в аккаунт Администратора")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}