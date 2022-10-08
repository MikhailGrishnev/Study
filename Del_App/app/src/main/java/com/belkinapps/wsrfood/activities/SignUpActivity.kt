package com.belkinapps.wsrfood.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.belkinapps.wsrfood.App
import com.belkinapps.wsrfood.data.remote.FoodApi
import com.belkinapps.wsrfood.data.requests.LoginRequest
import com.belkinapps.wsrfood.data.requests.RegisterRequest
import com.belkinapps.wsrfood.databinding.ActivitySignUpBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val compositeDisposable = CompositeDisposable()
    lateinit var foodApi: FoodApi
    var pref: SharedPreferences? = null
    var isLogged = false
    val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
        "[a-z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-z0-9][a-z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-z][a-z\\-]{0,2}" +
                ")+"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pref = getSharedPreferences("TABLE", Context.MODE_PRIVATE)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val registerBtn = binding.suRegisterBtn
        registerBtn.setOnClickListener {
            val email = binding.suEmailField.text.toString()
            val password = binding.suPasswordField.text.toString()
            val login = binding.suNameField.text.toString()
            val phone_numb = binding.suPhoneField.text.toString()
            if (checkEmail(email) && password != "" && login != "" && phone_numb != "") {
                foodApi = (application as? App)?.foodApi!!
                compositeDisposable.add(foodApi.sendRegisterRequest(
                    RegisterRequest(
                        email = email,
                        password = password,
                        login = login
                    )
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        compositeDisposable.add(foodApi.sendLoginRequest(
                            LoginRequest(
                                email = email,
                                password = password
                            )
                        )
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                isLogged = true
                                SaveState(isLogged, it.token)
                                (application as? App)?.configureRetrofit()
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            }, {
                                Toast.makeText(applicationContext, "Неверный e-mail или пароль", Toast.LENGTH_SHORT).show()
                            }))
                    }, {
                        Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                    }))
            } else {
                Toast.makeText(applicationContext, "Проверьте правильность заполнения полей", Toast.LENGTH_SHORT).show()
            }

        }
        val cancelBtn = binding.cancelBtn
        cancelBtn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

    }

    fun SaveState(state: Boolean, jwtToken: Int) {
        val editor = pref?.edit()
        editor?.putBoolean("isLogged", state)
        editor?.putInt("jwtToken", jwtToken)
        editor?.apply()
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    private fun checkEmail(email: String): Boolean {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
    }
}