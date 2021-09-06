package com.example.quizu

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_form_question.*
import kotlinx.android.synthetic.main.activity_form_quiz.*

class FormQuestion : AppCompatActivity() {

    val database = Firebase.database("https://quizu-f4483-default-rtdb.europe-west1.firebasedatabase.app/").reference


    private var correctAnswer: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_question)


        val bundle = intent.extras
        val numeroPregunta  = bundle?.getInt("numeroPreguntas")
        val idQuiz = bundle?.getString("idQuiz")
        val idPregunta = bundle?.getString("idPregunta")
        val idAsignatura = bundle?.getString("idAsignatura")


        setup(numeroPregunta ?:0, idQuiz ?:"",idPregunta ?:"",idAsignatura ?:"")


    }


    private fun setup(numPregunta: Int, id: String, idPregunta: String, idAsignatura:String) {


        controlAnswer()

        if(idPregunta!=""&&idPregunta!=null){
            modificarPregunta(id,idPregunta)
            }

        buttonAnteriorFormQuestion.setOnClickListener {
            controlExit(idAsignatura)
        }


        buttonAceptarFormQuestion.setOnClickListener {
            saveQuestion(id,numPregunta,idPregunta,idAsignatura)

        }
    }



    private fun saveQuestion(id: String, numPregunta: Int,idPregunta: String, idAsignatura: String) {
        var question = HashMap<String,String>()
        var numeros = HashMap<String,Long>()
        var control = true
        var numRespuestas:Long = 2
        var correcta = editTextCorrecta.text.toString()
        var key = database.push().key

        if (editTextEnunciadoForm.text.toString()!="") {
            question.put("enunciado", editTextEnunciadoForm.text.toString())
        }else{
            control = false
            showAlert("Introduzca el enunciado de la pregunta")
        }

        if(checkBoxOpcion1.isChecked&& editTextFromOpcion1.text.toString() != "") {
            question.put("respuesta1", editTextFromOpcion1.text.toString())
        }else{
            control = false
            showAlert("La pregunta debe contener al menos dos posibles respuestas")
        }

        if(checkBoxOpcion2.isChecked&& editTextFromOpcion2.text.toString() != "") {
            question.put("respuesta2", editTextFromOpcion2.text.toString())
            if (correcta=="3" ||correcta=="4"){
                showAlert("La respuesta correcta no debe ser mayor que 2")
                control = false
            }
        }else{
            control = false
            showAlert("La pregunta debe contener al menos dos posibles respuestas")


        }

        if (checkBoxOpcion3.isChecked && editTextFromOpcion3.text.toString() != "") {
            question.put("respuesta3", editTextFromOpcion3.text.toString())

            numRespuestas=3

            if (correcta=="4"){
                showAlert("La respuesta correcta no debe ser mayor que 3")
                control = false
            }
        }

        if (checkBoxOpcion4.isChecked && editTextFromOpcion4.text.toString() != "") {
            question.put("respuesta4", editTextFromOpcion4.text.toString())
            numRespuestas=4
        }
        if ((correcta=="1" || correcta=="2" || correcta=="3" || correcta=="4")){
          //  if (correcta.toLong() <= numRespuestas && correcta.toLong()>0) {
                question.put("correcta", editTextCorrecta.text.toString())
                //numeros.put("correcta", correcta.toLong())


        }else{
            showAlert("Introduzca un número entre 1 y 4")
            control = false
        }
        numeros.put("numeroRespuestas", numRespuestas)


        if (control) {
            //Si se le pasa id de cuestionario
            if (id != "") {
                if (key != null) {
                    //Si se le pasa el id de pregunta
                        if (idPregunta!=""&&idPregunta!=null) {
                            database.child("Cuestionarios").child(id).child("Preguntas").child(idPregunta)
                                .setValue(question+numeros)

                            database.child("Preguntas").child(idPregunta).setValue(question+numeros)
                            database.child("Asignaturas-Preguntas").child(idAsignatura).child(idPregunta).setValue(idPregunta)

                        }else{
                            database.child("Cuestionarios").child(id).child("Preguntas").child(key)
                                .setValue(question+numeros)

                            database.child("Preguntas").child(key).setValue(question+numeros)
                            database.child("Asignaturas-Preguntas").child(idAsignatura).child(key).setValue(key)

                        }
                }
            }//Si no se le pasa id de cuestionario
            else if (key != null) {
                database.child("Preguntas").child(key).setValue(question+numeros)
                database.child("Asignaturas-Preguntas").child(idAsignatura).child(key).setValue(key)
            }
            if (id != "" || id == null) {
                showFormQuiz(id,idAsignatura)
            } else {
                showAsignatura(idAsignatura)
            }
        }
    }



    private fun modificarPregunta(idQuiz: String,idPregunta: String) {
        database.child("Cuestionarios").child(idQuiz).child("Preguntas").child(idPregunta).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var numeroRespuestas = snapshot.child("numeroRespuestas").value.toString()

                    when {
                        numeroRespuestas == "4" -> {
                            editTextFromOpcion1.setText(snapshot.child("Respuesta1").value.toString())
                            editTextFromOpcion2.setText(snapshot.child("Respuesta2").value.toString())
                            editTextFromOpcion3.setText(snapshot.child("Respuesta3").value.toString())
                            editTextFromOpcion4.setText(snapshot.child("Respuesta4").value.toString())
                            editTextEnunciadoForm.setText(snapshot.child("enunciado").value.toString())
                            editTextPenalty.setText(snapshot.child("correcta").value.toString())
                            checkBoxOpcion3.isChecked = true
                            checkBoxOpcion4.isChecked = true
                            checkBoxOpcion3.visibility = View.VISIBLE
                            checkBoxOpcion4.visibility = View.VISIBLE
                            editTextFromOpcion3.visibility = View.VISIBLE
                            editTextFromOpcion4.visibility = View.VISIBLE

                        }
                        numeroRespuestas == "3" -> {
                            editTextFromOpcion1.setText(snapshot.child("Respuesta1").value.toString())
                            editTextFromOpcion2.setText(snapshot.child("Respuesta2").value.toString())
                            editTextFromOpcion3.setText(snapshot.child("Respuesta3").value.toString())
                            editTextEnunciadoForm.setText(snapshot.child("enunciado").value.toString())
                            editTextPenalty.setText(snapshot.child("correcta").value.toString())
                            checkBoxOpcion3.isChecked = true
                            checkBoxOpcion3.visibility = View.VISIBLE
                            editTextFromOpcion3.visibility = View.VISIBLE
                            checkBoxOpcion4.visibility = View.VISIBLE
                        }

                        else -> {
                            editTextFromOpcion1.setText(snapshot.child("Respuesta1").value.toString())
                            editTextFromOpcion2.setText(snapshot.child("Respuesta2").value.toString())
                            editTextEnunciadoForm.setText(snapshot.child("enunciado").value.toString())
                            editTextPenalty.setText(snapshot.child("correcta").value.toString())
                            checkBoxOpcion3.visibility = View.VISIBLE


                        }
                    }






                }
            }

            override fun onCancelled(error: DatabaseError) {
                showAlert("Error al acceder a la base de datos ")
            }
        })
    }



    private fun showAsignatura(idAsignatura: String) {
        val homeIntent = Intent(this,AsignaturaProfesor::class.java).apply {
            putExtra("idAsignatura",idAsignatura)

        }
        startActivity(homeIntent)
    }

    private fun showFormQuiz(idQuiz:String,idAsignatura: String) {
        val homeIntent = Intent(this,FormQuiz::class.java).apply {
            putExtra("idQuiz",idQuiz)
            putExtra("idAsignatura",idAsignatura)
        }
        startActivity(homeIntent)
    }

    fun showAlert(mensaje:String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(mensaje)
        builder.setNegativeButton("Aceptar" , DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    private fun controlAnswer() {
        var control1 = 1.0
        var control2 = 1.0
        var control3 = 0.0
        var control4 = 0.0

        // editTextFromOpcion1.visibility = View.INVISIBLE
        //editTextFromOpcion2.visibility = View.INVISIBLE
        editTextFromOpcion3.visibility = View.INVISIBLE
        editTextFromOpcion4.visibility = View.INVISIBLE

        checkBoxOpcion1.isChecked = true
        checkBoxOpcion2.isChecked = true
        // checkBoxOpcion3.visibility = View.INVISIBLE
        checkBoxOpcion4.visibility = View.INVISIBLE

        checkBoxOpcion1.setOnClickListener {
            if(control1%2==0.0) {
                editTextFromOpcion1.visibility = View.VISIBLE
                checkBoxOpcion2.visibility = View.VISIBLE
                control1++
            }else{
                editTextFromOpcion1.visibility = View.INVISIBLE
                editTextFromOpcion2.visibility = View.INVISIBLE
                editTextFromOpcion3.visibility = View.INVISIBLE
                editTextFromOpcion4.visibility = View.INVISIBLE

                checkBoxOpcion2.visibility = View.INVISIBLE
                checkBoxOpcion3.visibility = View.INVISIBLE
                checkBoxOpcion4.visibility = View.INVISIBLE

                checkBoxOpcion2.isChecked = false
                checkBoxOpcion3.isChecked = false
                checkBoxOpcion4.isChecked = false
                control1 =0.0
                control2 = 0.0
                control3 = 0.0
                control4 = 0.0
            }
        }


        checkBoxOpcion2.setOnClickListener {
            if(control2%2==0.0){
                editTextFromOpcion2.visibility = View.VISIBLE
                checkBoxOpcion3.visibility = View.VISIBLE
                control2++
            }else{
                editTextFromOpcion2.visibility = View.INVISIBLE
                editTextFromOpcion3.visibility = View.INVISIBLE
                editTextFromOpcion4.visibility = View.INVISIBLE

                checkBoxOpcion3.visibility = View.INVISIBLE
                checkBoxOpcion4.visibility = View.INVISIBLE

                checkBoxOpcion3.isChecked = false
                checkBoxOpcion4.isChecked = false
                control2=0.0
                control3 = 0.0
                control4 = 0.0
            }

        }


        checkBoxOpcion3.setOnClickListener {
            if (control3%2==0.0){
                editTextFromOpcion3.visibility = View.VISIBLE
                checkBoxOpcion4.visibility = View.VISIBLE
                control3++
            }else{
                editTextFromOpcion3.visibility = View.INVISIBLE
                editTextFromOpcion4.visibility = View.INVISIBLE

                checkBoxOpcion4.visibility = View.INVISIBLE


                checkBoxOpcion4.isChecked = false
                control3=0.0
                control4 = 0.0
            }
        }


        checkBoxOpcion4.setOnClickListener {
            if (control4%2==0.0){
                editTextFromOpcion4.visibility = View.VISIBLE
                control4++
            }else{
                editTextFromOpcion4.visibility = View.INVISIBLE
                control4=0.0
            }
        }


    }
    private fun controlExit(idAsignatura: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cuidado!")
        builder.setMessage("No has completado la pregunta, ¿seguro que quieres cancelar?")
        builder.setNegativeButton("Volver a la pregunta", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.setPositiveButton("Salir", DialogInterface.OnClickListener { dialog, which ->
                showAsignatura(idAsignatura)
        })
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}