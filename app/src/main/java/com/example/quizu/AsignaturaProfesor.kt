package com.example.quizu

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_asignatura_profesor.*

class AsignaturaProfesor : AppCompatActivity() {
    val database = Firebase.database("https://quizu-f4483-default-rtdb.europe-west1.firebasedatabase.app/").reference
    var nombre = ""
    var idAsignaturaG=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asignatura_profesor)

        val bundle = intent.extras
        var idAsignatura = bundle?.getString("idAsignatura")
        idAsignaturaG = idAsignatura.toString()
        setup(idAsignatura ?: "")
    }

    private fun setup(idAsignatura: String) {
        textViewIdAsignaturaProfesor.text = idAsignatura
        database.child("Asignaturas").child(idAsignatura).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    textViewNombreAsignaturaProfesor.text =
                        snapshot.child("nombre").value.toString()
                    nombre = snapshot.child("nombre").value.toString()

                }
            }


            override fun onCancelled(error: DatabaseError) {

            }
        })


        buttonCrearCuestionarioAsignatura.setOnClickListener {
            showFormQuiz(idAsignatura)
        }


        buttonCuestionariosProfesor.setOnClickListener {
            showListAñadir("cuestionariosProfesor", idAsignatura)
        }

        buttonCuestionariosLanzados.setOnClickListener {
            showList("cuestionariosLanzados", idAsignatura)
        }


        buttonListaAlumnosAsignatura.setOnClickListener {
            showList("alumnos", idAsignatura)
        }

        buttonNotasExportar.setOnClickListener {

            showNotas(idAsignatura,nombre)
        }


        buttonHomeAsignaturaProfesor.setOnClickListener {
            showHome()
        }

        buttonCrearPregutna.setOnClickListener {
            showQuestion(idAsignatura)
        }

        buttonPreguntas.setOnClickListener {
            showListaPreguntas("preguntas", idAsignatura)
        }



    }




    private fun showListaPreguntas(tipo: String, idAsignatura: String) {
        val mIntent = Intent(this, Lista::class.java).apply {
            putExtra("ListaDe", tipo)
            putExtra("idAsignatura", idAsignatura)
        }
        startActivity(mIntent)
    }

    private fun showQuestion(idAsignatura: String) {
        val intent = Intent(this, FormQuestion::class.java).apply {
            putExtra("idAsignatura", idAsignatura)
        }
        startActivity(intent)

    }

    private fun showFormQuiz(idAsignatura: String) {
        val mIntent = Intent(this, FormQuiz::class.java).apply {
            putExtra("idAsignatura", idAsignatura)
        }
        startActivity(mIntent)
    }

    private fun showHome() {
        val mIntent = Intent(this, HomeActivity::class.java).apply {
            //putExtra("ListaDe",tipo)
        }
        finish()
        startActivity(mIntent)
    }

    private fun showNotas(idAsignatura: String, nombre: String) {
        val mIntent = Intent(this, Notas::class.java).apply {
            putExtra("tipo","asignatura")
            putExtra("idAsignatura", idAsignatura)
            putExtra("nombre", nombre)
        }
        startActivity(mIntent)
    }

    private fun showList(tipo: String, idAsignatura: String) {
        val mIntent = Intent(this, Lista::class.java).apply {
            putExtra("ListaDe", tipo)
            putExtra("idAsignatura", idAsignatura)
        }
        startActivity(mIntent)
    }

    private fun showListAñadir(tipo: String, idAsignatura: String) {
        val mIntent = Intent(this, Lista::class.java).apply {
            putExtra("ListaDe", tipo)
            putExtra("idAsignatura", idAsignatura)

        }
        startActivity(mIntent)
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

        showList("asignaturasProfesor",idAsignaturaG)
        return true
    }

}