package com.example.metalapp

import android.os.Bundle
import android.view.View
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
    private lateinit var tvLoginStatus: TextView

    private val adminPhone = "+380000000000"
    private val adminPassword = "123"

    private var isLoginMode = true

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
        btnRegister = findViewById(R.id.btn_edit)
        tvLoginStatus = findViewById(R.id.tv_login_status)

        btnLogin.setOnClickListener {
            if (isLoginMode) {
                validateLogin()
            } else {
                validateRegistration()
            }
        }

        btnRegister.setOnClickListener {
            swapButtonTexts()
            isLoginMode = !isLoginMode
            tvLoginStatus.visibility = View.GONE
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
            val digitCount = phone.filter { it.isDigit() }.length
            if (digitCount > 12) {
                Toast.makeText(this, "Несуществующий номер телефона", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Неверный номер телефона или пароль", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateRegistration() {
        val phone = etPhone.text.toString().trim()
        val password = etPassword.text.toString().trim()

        val digitCount = phone.filter { it.isDigit() }.length

        if (digitCount > 12) {
            Toast.makeText(this, "Несуществующий номер телефона", Toast.LENGTH_SHORT).show()
            tvLoginStatus.visibility = View.GONE
        } else if (phone == adminPhone) {
            tvLoginStatus.visibility = View.VISIBLE
            tvLoginStatus.text = "Логин занят"
        } else if (phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            tvLoginStatus.visibility = View.GONE
        } else {
            Toast.makeText(this, "Регистрация успешна", Toast.LENGTH_SHORT).show()
            tvLoginStatus.visibility = View.GONE
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

    private fun swapButtonTexts() {
        val tempLoginText = btnLogin.text.toString()
        val tempRegisterText = btnRegister.text.toString()

        btnLogin.text = tempRegisterText
        btnRegister.text = tempLoginText
    }
}