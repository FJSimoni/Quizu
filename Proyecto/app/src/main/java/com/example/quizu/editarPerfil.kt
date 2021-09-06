package com.example.quizu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_editar_perfil.*

class editarPerfil : AppCompatActivity() {
    val database = Firebase.database("https://quizu-f4483-default-rtdb.europe-west1.firebasedatabase.app/").reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        val bundle = intent.extras
        var dni = bundle?.getString("dni")
        var tipo = bundle?.getString("tipo")
        setup(dni?:"", tipo?:"")
    }

    private fun setup(dni: String, tipo:String) {
        buttonCancelarModificarPerfil.setOnClickListener {
            onBackPressed()
        }

        buttonAceptarModificarPerfil.setOnClickListener {
            saveData(dni,tipo)
        }
    }

    private fun saveData(dni: String,tipo:String) {
        var name = editTextName2.text.toString()

        if (tipo == "alumno") {
            database.child("Alumnos").child(dni).child("nombre").setValue(name)
        } else {
            database.child("Profesores").child(dni).child("nombre").setValue(name)
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