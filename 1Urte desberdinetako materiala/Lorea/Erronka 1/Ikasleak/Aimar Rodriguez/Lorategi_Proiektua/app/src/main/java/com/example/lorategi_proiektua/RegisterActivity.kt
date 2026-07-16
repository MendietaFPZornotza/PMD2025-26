package com.example.lorategi_proiektua

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegisterActivity : AppCompatActivity() {
    lateinit var inputMail: EditText
    lateinit var inputPass: EditText
    lateinit var inputUser: EditText
    lateinit var inputGender: RadioGroup
    lateinit var inputMale: RadioButton
    lateinit var inputFemale: RadioButton
    lateinit var inputOther: RadioButton
    lateinit var inputNotifications: CheckBox
    lateinit var inputCity: Spinner
    lateinit var btnRegister: Button
    lateinit var goToLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //meter 3 ciudades en el spinner
        val ciudades = arrayOf("Bilbao", "Donostia", "Gasteiz")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, ciudades)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val spinner: Spinner = findViewById(R.id.spineerCity)
        spinner.adapter = adapter

        inputMail=findViewById(R.id.inputMail)
        inputPass=findViewById(R.id.inputPass)
        inputUser=findViewById(R.id.inputUser)
        inputGender=findViewById(R.id.inputGender)

        inputNotifications=findViewById(R.id.cbNotifications)
        inputCity=findViewById(R.id.spineerCity)
        btnRegister=findViewById(R.id.btnRegister)
        goToLogin=findViewById(R.id.linkLogin)

        goToLogin.setOnClickListener(){
            System.out.println("Go To Login")
            //Loginera joan
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            //registroa itxi
            finish()
        }

        //ERREGISTRATU
        btnRegister.setOnClickListener() {
            System.out.println("Register")
            //Datuak jaso
            //Idatzizko datuak hutsik ez egotea ziurtatu
            if (inputMail.text.toString().isEmpty() || inputPass.text.toString().isEmpty() || inputUser.text.toString().isEmpty()) {
                //mostrar un toast
                Toast.makeText(this, "Datu guztiak sartu", Toast.LENGTH_SHORT).show()
                System.out.println("Datu guztiak sartu")
                return@setOnClickListener
            }
            //Emaila egiaztatu
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(inputMail.text.toString()).matches()) {
                //mostrar un toast
                Toast.makeText(this, "Sartu helbide elektronikoa ondo", Toast.LENGTH_SHORT).show()
                System.out.println("Mail okerra")
                return@setOnClickListener
            }
            //Erregistroa egin
            else{
                val mail = inputMail.text.toString()
                val pass = inputPass.text.toString()
                val user = inputUser.text.toString()
                val gender = when(inputGender.checkedRadioButtonId) {
                    R.id.rbMan   -> "Gizona"
                    R.id.rbWoman -> "Emakumea"
                    else         -> "Bestea"
                }
                val notifications = if(inputNotifications.isChecked) 1 else 0
                val city = inputCity.selectedItem.toString()

                //mirar si el email ya existe en la base de datos
                val admin = AdminSQLiteOpenHelper(
                    this,
                    "administracion", null, 5
                )
                val bd = admin.writableDatabase

                val cursor = bd.rawQuery("select * from users where mail = '$mail'", null)
                if (cursor.moveToFirst()) {
                    //mostrar un toast
                    Toast.makeText(this, "Email hori erregistratuta dago", Toast.LENGTH_SHORT).show()
                    System.out.println("Email hori erregistratuta dago")
                    return@setOnClickListener
                }
                else{
                    //Datu basean gorde

                    val erregistroa = ContentValues()
                    erregistroa.put("mail", mail)
                    erregistroa.put("pass", pass)
                    erregistroa.put("user", user)
                    erregistroa.put("gender", gender)
                    erregistroa.put("notifications", notifications)
                    erregistroa.put("city", city)

                    bd.insert("users", null, erregistroa)
                    bd.close()
                    Toast.makeText(this, "Erregistroa burutu da", Toast.LENGTH_SHORT).show()
                    System.out.println("Erregistroa burutu da")

                    //Produktuetara joan
                    val intent = Intent(this, ProductActivity::class.java)
                    startActivity(intent)
                    //registroa itxi
                    finish()
                }
            }
        }
    }
}