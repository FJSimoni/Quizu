package com.example.quizu

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.view.size
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_form_quiz.*



class FormQuiz : AppCompatActivity() {
    val database = Firebase.database("https://quizu-f4483-default-rtdb.europe-west1.firebasedatabase.app/").reference
    var quiz = ""
    var asig = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_quiz)

        val bundle = intent.extras
        val id = bundle?.getString("idQuiz")
        val idAsignatura = bundle?.getString("idAsignatura")

        if (id != null) {
            quiz = id
        }
        if (idAsignatura != null) {
            asig = idAsignatura
        }

        setup(id ?: "",idAsignatura ?:"")
    }

    private fun setup(id: String,idAsignatura: String) {

        var idQuiz= ""


        if (id!=""){
            idQuiz = id
            database.child("Cuestionarios").child(idQuiz).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var titulo = snapshot.child("Titulo").value
                        var pen = snapshot.child("Penalizacion").value


                        editTextPenalty.setText(pen.toString())
                        editTextTituloQuiz.setText(titulo.toString())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })


        }else if(id==""){

            idQuiz = QuizcreateIdQuiz()


        }


        var aenunciado = arrayListOf<String>()
        var numRes = arrayListOf<String>()
        var aopcion1 = arrayListOf<String>()
        var aopcion2  = arrayListOf<String>()
        var aopcion3 = arrayListOf<String>()
        var aopcion4 = arrayListOf<String>()
        var acorrecta = arrayListOf<String>()
        var aid = arrayListOf<String>()
        var adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,aenunciado)

        database.child("Cuestionarios").child(idQuiz).child("Preguntas").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (postSnapshot in snapshot.children) {
                            var enunciado = postSnapshot.child("enunciado").value
                            var numeroRespuestas = postSnapshot.child("numeroRespuestas").value
                            var opcion1 = postSnapshot.child("respuesta1").value
                            var opcion2 = postSnapshot.child("respuesta2").value
                            var opcion3 = postSnapshot.child("respuesta3").value
                            var opcion4 = postSnapshot.child("respuesta4").value
                            var correcta = postSnapshot.child("correcta").value
                            var idAsignatura = postSnapshot.key

                            numRes.add(numeroRespuestas.toString())
                            aopcion1.add(opcion1.toString())
                            aopcion2.add(opcion2.toString())
                            aopcion3.add(opcion3.toString())
                            aopcion4.add(opcion4.toString())
                            acorrecta.add(correcta.toString())
                            aenunciado.add(enunciado.toString())
                            aid.add(idAsignatura.toString())

                            listPreguntasQuiz.adapter = adapter



                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showAlert("Error al acceder a la base de datos ")
                }
        })



         listPreguntasQuiz.setOnItemClickListener { parent, view, position, id ->
            showSeleccionado(aid[position],idQuiz,idAsignatura)
        }


        buttonCancelarFormQuiz.setOnClickListener {
            controlExit(idQuiz,idAsignatura)
        }

        buttonAñadirPreguntaFormQuiz.setOnClickListener {
            database.child("Cuestionarios").child(idQuiz).child("Penalizacion").setValue(editTextPenalty.text.toString())
            database.child("Cuestionarios").child(idQuiz).child("Titulo").setValue(editTextTituloQuiz.text.toString())
            showList(idQuiz,"preguntas",idAsignatura)

        }

        buttonCrearPreguntaFormQuiz.setOnClickListener{

            database.child("Cuestionarios").child(idQuiz).child("Penalizacion").setValue(editTextPenalty.text.toString())
            database.child("Cuestionarios").child(idQuiz).child("Titulo").setValue(editTextTituloQuiz.text.toString())
            showFormQuestion(idQuiz,idAsignatura)
        }

        buttonCrearFormQuiz.setOnClickListener {
            if(editTextTituloQuiz.text.isEmpty()||editTextPenalty.toString().isEmpty()) {
                showAlert("Introduzca valores correctos a todos los campos")
            }else if(listPreguntasQuiz.size==0){
                showAlert("No se ha introducido ninguna pregunta")
                }else{
                    crearCuestionario(idQuiz,idAsignatura)
                    showAsignatura(idAsignatura)
            }


            }

    }

    override fun onBackPressed() {
        controlExit(quiz,asig)
    }

    private fun crearCuestionario(idQuiz: String, idAsignatura: String) {
        var titulo = editTextTituloQuiz.text.toString()
        database.child("Asignaturas-Cuestionarios").child(idAsignatura).child(idQuiz).child("Titulo").setValue(titulo)
        database.child("Asignaturas-Cuestionarios").child(idAsignatura).child(idQuiz).setValue(idQuiz)
    }


    private fun QuizcreateIdQuiz(): String {
        var key = database.push().key
        return key.toString()
    }



    private fun borrarQuiz(idQuiz: String) {
        var key = idQuiz
        database.child("Cuestionarios").child(key).removeValue()
    }

    private fun showFormQuestion(id:String,idAsignatura: String) {
        val homeIntent = Intent(this,FormQuestion::class.java).apply {
            putExtra("idQuiz",id)
            putExtra("idAsignatura",idAsignatura)
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
    private fun showSeleccionado(seleccionado: String, idQuiz: String,idAsignatura: String) {
        val mintent = Intent(this,Selecionado::class.java).apply {
            putExtra("idQuiz",idQuiz)
            putExtra("idPregunta",seleccionado)
            putExtra("tipoSelect","preguntas")
            putExtra("idAsignatura",idAsignatura)

        }
        startActivity(mintent)
    }

    private fun showList(idQuiz: String, tipo: String, idAsignatura: String) {
        val mIntent = Intent(this,Lista::class.java).apply {
            putExtra("idQuiz",idQuiz)
            putExtra("ListaDe",tipo)
            putExtra("idAsignatura",idAsignatura)
        }
        startActivity(mIntent)
    }

    private fun controlExit(idQuiz: String,idAsignatura: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cuidado!")
        builder.setMessage("No has completado el cuestionario, ¿seguro que quieres cancelar?")
        builder.setNegativeButton("Volver al cuestionario", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.setPositiveButton("Salir", DialogInterface.OnClickListener { dialog, which ->
            borrarQuiz(idQuiz)
            showAsignatura(idAsignatura)
        })
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAsignatura(idAsignatura: String) {
        val mIntent = Intent(this,AsignaturaProfesor::class.java).apply {
            putExtra("idAsignatura",idAsignatura)
        }
        startActivity(mIntent)
    }

    private fun showHome() {
        val mIntent = Intent(this,HomeActivity::class.java).apply {

        }
        startActivity(mIntent)
    }
}
