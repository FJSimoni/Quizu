package com.example.quizu

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_asignatura_alumno.*
import kotlinx.android.synthetic.main.activity_home_alumno.*
import kotlinx.android.synthetic.main.activity_home_alumno.editTextCodigoCuestionarioAlumno

class HomeAlumno : AppCompatActivity() {
    val database = Firebase.database("https://quizu-f4483-default-rtdb.europe-west1.firebasedatabase.app/").reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_alumno)

        val bundle = intent.extras
        var email = bundle?.getString("email")
        val nombre = bundle?.getString("nombre")
        val dni = bundle?.getString("dni")

        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)

        if (email==null){
             email = prefs.getString("email", null)
        }


        setup(email ?: "", nombre ?: "", dni ?: "")

    }

    private fun setup(email: String, nombre: String, dniAlumno: String) {
        var dni=""

        /*if (nombre!="" || nombre!="Nombre") {
            showAlert(nombre)
            nametextViewAlumno.text = nombre
        }*/



        database.child("Alumnos").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (postSnapshot in snapshot.children) {
                        if (email == postSnapshot.child("email").value.toString()) {
                            dni = postSnapshot.child("dni").value.toString()
                            nametextViewAlumno.text =
                                postSnapshot.child("nombre").value.toString()
                        }

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })



        buttonRealizarCuestionarioHomeAlumno.setOnClickListener {
            var control = false
            database.child("Cuestionarios").addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (postSnapshot in snapshot.children) {
                            if (postSnapshot.child("codigo").exists()) {
                                if (editTextCodigoCuestionarioAlumno.text.toString() == postSnapshot.child(
                                        "codigo"
                                    ).value.toString()
                                ) {
                                    var idQuiz = postSnapshot.key.toString()
                                    control = true
                                    showSolveQuiz(dni, idQuiz)
                                }

                            }
                        }
                        if (control == false)
                            showAlert("Clave incorrecta, por favor introduzca de nuevo")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        }

        buttonAsignaturasAlumno.setOnClickListener{
            showList("asignaturasAlumnos", dni)
        }

        buttonEditarPerfilAlumno.setOnClickListener {
            showEditarPerfil(dni, "alumno")
        }

        cerrarSesionHomeAlumno.setOnClickListener {
            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()

            FirebaseAuth.getInstance().signOut()
            finishAffinity()
            //showLogin()
        }


        buttonMisAsignaturas.setOnClickListener {
            showList("misAsignaturas", dni)
        }


    }

    private fun showList(listaDe: String, dni: String) {
        val intent = Intent(this, Lista::class.java).apply {
            putExtra("ListaDe", listaDe)
            putExtra("dni", dni)
        }
        startActivity(intent)
    }

    private fun showSolveQuiz(dni: String, idQuiz: String) {

        val intent = Intent(this, SolveQuiz::class.java).apply {
            putExtra("dni", dni)
            putExtra("idQuiz", idQuiz)
        }
        startActivity(intent)

    }

    private fun showLogin() {
        val intent = Intent(this, AuthActivity::class.java).apply {
            //putExtra("nombre",nombre)
        }
        startActivity(intent)
    }

    private fun showEditarPerfil(dni: String, tipo: String) {
        val intent = Intent(this, editarPerfil::class.java).apply {
            putExtra("dni", dni)
            putExtra("tipo", tipo)
        }
        startActivity(intent)
    }

    fun showAlert(mensaje: String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Salir!")
        builder.setMessage("¿Quieres salir de la aplicación")
        builder.setNegativeButton(
            "Volver",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.setPositiveButton(
            "Salir",
            DialogInterface.OnClickListener { dialog, which -> finishAffinity() })
        val dialog: AlertDialog = builder.create()
        dialog.show()
        return false
    }
}