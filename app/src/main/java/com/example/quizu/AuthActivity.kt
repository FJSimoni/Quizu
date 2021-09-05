package com.example.quizu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_auth.*


class AuthActivity : AppCompatActivity() {
    val database = Firebase.database("https://quizu-f4483-default-rtdb.europe-west1.firebasedatabase.app/").reference

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        //Guardado de datos para inicio automatico de sesion

        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)

        if (email==null)
        setTheme(R.style.Theme_Quizu)
        val analitics:FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle();
        bundle.putString("mesagge", "Integracion de Firebase Completa")
        analitics.logEvent("InitScreen",bundle)

        session()
        setup()


    }

    private fun setup(){


        setContentView(R.layout.activity_auth)

        registrarButton.setOnClickListener {

            showSign( "",false)

        }

        accederButton.setOnClickListener {


            if(editTextEmail.text.isNotEmpty() && editTextPassword.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(editTextEmail.text.toString(),
                    editTextPassword.text.toString()).addOnCompleteListener {

                    if (it.isSuccessful){
                        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
                        prefs.putString("email",editTextEmail.text.toString())
                        prefs.apply()
                        esProfesor()


                    }else{
                        showAlert("Email o contraseña incorrecta")
                    }
                }

            }



        }

    }


    fun session(){

        var control=false
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)

        if (email!=null){


            database.child("Profesores").addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(postSnapshot in snapshot.children ){
                        var aux = postSnapshot.child("email").value

                        if (email == aux.toString()) {
                            control=true
                        }

                    }
                    when(control){
                        true->showHome(email)
                        false ->  showHomeAlumno(email)
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })



        }


    }


    private fun esProfesor() {
        database.child("Profesores").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(postSnapshot in snapshot.children ){
                    var aux = postSnapshot.child("email").value

                    if (editTextEmail.text.toString() == aux.toString()) {


                        showHome(editTextEmail.text.toString())
                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        showHomeAlumno(editTextEmail.text.toString())

    }



    fun showHome(email: String){
        val homeIntent = Intent(this,HomeActivity::class.java).apply {
            putExtra("email",email)
        }
        startActivity(homeIntent)
    }


    fun showHomeAlumno(email: String){
        val intent = Intent(this,HomeAlumno::class.java).apply {
            putExtra("email",email)
        }
        startActivity(intent)
    }

    fun showSign(email:String,checkG:Boolean){
        val signIntent = Intent(this,Registro::class.java).apply {
            putExtra("email",email)
            putExtra("check",checkG)

        }
        startActivity(signIntent)

    }

    fun showAlert(mensaje:String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        finishAffinity()
        return super.onKeyDown(keyCode, event)
    }

}
