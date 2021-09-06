package com.example.quizu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_asignatura_alumno.*
import kotlinx.android.synthetic.main.activity_asignatura_alumno.editTextCodigoCuestionarioAlumno

class AsignaturaAlumno : AppCompatActivity() {
    val database = Firebase.database("https://quizu-f4483-default-rtdb.europe-west1.firebasedatabase.app/").reference
    var idQuiz = ""
    var dniA =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asignatura_alumno)

        val bundle = intent.extras
        val idAsignatura = bundle?.getString("idAsignatura")
        val dni = bundle?.getString("dni")
        dniA = dni.toString()
        setup(idAsignatura ?:"", dni ?:"")
    }

    private fun setup(idAsignatura: String, dni: String) {

        database.child("Asignaturas").child(idAsignatura).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    textViewNombreAsignaturaAlumno.text= snapshot.child("nombre").value.toString()

                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })


        buttonRealizarCuestionarioAlumno.setOnClickListener {
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
                                    showSolveQuiz(dni, idQuiz)
                                }else{
                                   // showAlert("Clave incorrecta, por favor introduzca de nuevo")
                                }
                            }
                        }


                    }

                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }



        buttonCuestionariosAlumno.setOnClickListener {
            showList("cuestionariosAlumno",dni)
        }


        buttonHomeAsignaturaAlumno.setOnClickListener {
            showHome(dni)
        }



    }


    private fun showHome(dni: String) {
        val mIntent = Intent(this,HomeAlumno::class.java).apply {
            putExtra("dni",dni)
        }
        startActivity(mIntent)
    }

    private fun showList(tipo:String,dni: String) {
        val mIntent = Intent(this,Lista::class.java).apply {
            putExtra("ListaDe",tipo)
            putExtra("dni",dni)
        }
        startActivity(mIntent)
    }

    fun showAlert(mensaje:String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showSolveQuiz(dni: String, idQuiz: String) {

        val intent = Intent(this,SolveQuiz::class.java).apply {
            putExtra("dni",dni)
            putExtra("idQuiz",idQuiz)
        }
        startActivity(intent)

    }

}