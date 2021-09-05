package com.example.quizu

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home_alumno.*


class HomeActivity : AppCompatActivity() {
    val database = Firebase.database("https://quizu-f4483-default-rtdb.europe-west1.firebasedatabase.app/").reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)



        val bundle = intent.extras
        var email = bundle?.getString("email")
        var nombre = bundle?.getString("nombre")

        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)

        if (email==null){
            email = prefs.getString("email", null)
        }



        setup(email ?:"",nombre?:"")





    }


    private fun setup(email:String, nombre:String){
        var dni=""

        database.child("Profesores").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (postSnapshot in snapshot.children) {
                        if (email == postSnapshot.child("email").value.toString()) {
                            dni = postSnapshot.child("dni").value.toString()
                            nametextView.text =
                                postSnapshot.child("nombre").value.toString()
                        }

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })


        cerrarSesion.setOnClickListener{
            //Borrado de datos cuando cerramos sesion
            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()

            FirebaseAuth.getInstance().signOut()
            //showLogin()
            finishAffinity()
        }


        buttonSalirHome.setOnClickListener {
            finishAffinity()
        }

        buttonEditarPerfil.setOnClickListener {
            showEditarPerfil(dni,"profesor")
        }

        buttonAsignaturas.setOnClickListener {
            showLista("asignaturasProfesor")
        }

        buttonCrearAsignatura.setOnClickListener {
            showFormAsignatura()
        }

    }

    private fun showEditarPerfil(dni: String, tipo:String) {
        val intent = Intent(this,editarPerfil::class.java).apply {
            putExtra("dni",dni)
            putExtra("tipo", tipo)
        }
        startActivity(intent)
    }

    private fun showFormAsignatura() {
        val intent = Intent(this,FormAsignatura::class.java).apply {

        }
        startActivity(intent)
    }




    private fun showLogin() {
        val intent = Intent(this,AuthActivity::class.java).apply {
            //putExtra("nombre",nombre)
        }
        startActivity(intent)
    }


    private fun showFormQuiz() {
        val intent = Intent(this,FormQuiz::class.java).apply {
            //putExtra("nombre",nombre)
        }
        startActivity(intent)
    }


    private fun showLista(listaDe: String) {
        val intent = Intent(this,Lista::class.java).apply {
            putExtra("ListaDe",listaDe)
        }
        startActivity(intent)
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

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Salir!")
        builder.setMessage("¿Quieres salir de la aplicación")
        builder.setNegativeButton("Volver", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.setPositiveButton("Salir", DialogInterface.OnClickListener { dialog, which -> finishAffinity()  })
        val dialog: AlertDialog = builder.create()
        dialog.show()
        return false
    }


}