package com.example.quizu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_form_asignatura.*
import kotlinx.android.synthetic.main.activity_form_quiz.*
import kotlinx.android.synthetic.main.activity_home.*


class FormAsignatura : AppCompatActivity() {

    var idAsignatura = ""
    val database = Firebase.database("https://quizu-f4483-default-rtdb.europe-west1.firebasedatabase.app/").reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_asignatura)

        setup()
    }

    private fun setup() {

        buttonCrearFormAsignatura.setOnClickListener {
            createAsignatura()
            showAsignatura(idAsignatura)
        }

        buttonCancelarFormAsignatura.setOnClickListener {
            onBackPressed()
        }
    }

    private fun showAsignatura(idAsignatura: String) {
        val intent = Intent(this,AsignaturaProfesor::class.java).apply {
            putExtra("idAsignatura",idAsignatura)
        }
        startActivity(intent)
    }

    private fun createAsignatura() {

        var nombre = editTextNombreAsignatura.text.toString()
        var curso = editTextAnoAcademico.text.toString()
        var key = database.push().key
        idAsignatura = key.toString()
        var asignatura = HashMap<String,String>()

        asignatura.put("nombre", nombre)
        asignatura.put("curso", curso)


        if (key != null) {
            database.child("Asignaturas").child(key).setValue(asignatura)
        }else{
            showAlert("Error, se ha generado una clave erronea")
        }
    }

    fun showAlert(mensaje:String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}