package com.example.quizu

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_registro.*
import kotlin.collections.HashMap


class Registro : AppCompatActivity() {

    val database = Firebase.database("https://quizu-f4483-default-rtdb.europe-west1.firebasedatabase.app/").reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val bundle = intent.extras
        val email = bundle?.getString("email")

        setup(email ?:"")




    }

    private fun setup(email:String){

        buttonSignAceptar.setOnClickListener {
            registroNormal()
        }


        buttonSignCancelar.setOnClickListener {

                val prefs = getSharedPreferences(
                    getString(R.string.prefs_file),
                    Context.MODE_PRIVATE
                ).edit()
                prefs.clear()
                prefs.apply()

                FirebaseAuth.getInstance().signOut()
                onBackPressed()

        }


    }



    fun registroNormal(){


        if (editTextSignEmail.text.isNotEmpty() && editTextSignPassword.text.isNotEmpty() && editTextName.text.isNotEmpty()
            && editTextConfirmSignPassword.text.isNotEmpty() && (radioButtonAlumno.isChecked || radioButtonProfesor.isChecked)
            && editTextDNI.text.isNotEmpty()) {
            if (editTextSignPassword.text.toString() == editTextConfirmSignPassword.text.toString()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    editTextSignEmail.text.toString(),
                    editTextSignPassword.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
                        prefs.putString("email",editTextSignEmail.text.toString())
                        prefs.apply()
                        if(radioButtonAlumno.isChecked) {
                            addNewAlumno()
                            showHomeAlumno(editTextSignEmail.text.toString(),editTextName.text.toString())
                        }else{
                            addNewProfesor()
                            showHome(editTextSignEmail.text.toString(),editTextName.text.toString())
                        }
                    }else{
                        showAlert("Compruebe que el email introducido es válido y que la contraseña tiene más de 6 caracteres")
                    }
                }
            }else{
                showAlert("Las contraseñas no coinciden")
            }
        }else{
            showAlert("Introduzca datos validos en todos los campos")
        }
    }


    private fun addNewAlumno(){

        var name = editTextName.text.toString()
        var email = editTextSignEmail.text.toString()
        var dni = editTextDNI.text.toString()
        var alumno =  HashMap<String,String>()

        alumno.put("dni", dni)
        alumno.put("email", email)
        alumno.put("nombre", name)



        database.child("Alumnos").child(dni).setValue(alumno)



    }

    private fun addNewProfesor(){


        var dni = editTextDNI.text.toString()
        var name = editTextName.text.toString()
        var email = editTextSignEmail.text.toString()
        var profesor =  HashMap<String,String>()


        profesor.put("dni", dni)
        profesor.put("email", email)
        profesor.put("nombre", name)

        database.child("Profesores").child(dni).setValue(profesor)


    }

    fun showHome(email:String,nombre:String){
        val homeIntent = Intent(this,HomeActivity::class.java).apply {
            putExtra("email",email)
            putExtra("nombre",nombre)
        }
        startActivity(homeIntent)
    }

    fun showAlert(mensaje:String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun showHomeAlumno(email:String,nombre:String){
        val homeIntent = Intent(this,HomeAlumno::class.java).apply {
            putExtra("email",email)
            putExtra("nombre",nombre)
        }
        startActivity(homeIntent)
    }

}