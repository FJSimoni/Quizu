package com.example.quizu

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_solve_quiz.*
import com.example.quizu.Pregunta as Question


class SolveQuiz : AppCompatActivity() {
    val database = Firebase.database("https://quizu-f4483-default-rtdb.europe-west1.firebasedatabase.app/").reference

    private var currentPos: Int = 1
    private var questionList: ArrayList<Question> = ArrayList()
    private var correctAnswer: Int = 0
    private var mark = -1.0f
    private var penalty = 33
    var preguntas = HashMap<Int, Int>()
    var control1 =0.0
    var control2 = 0.0
    var control3= 0.0
    var control4=0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solve_quiz)

        val bundle = intent.extras
        val dni = bundle?.getString("dni")
        var idQuiz = bundle?.getString("idQuiz")

        if (idQuiz != null) {
            database.child("Cuestionarios").child(idQuiz).addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                   textViewTituloQuiz.text = snapshot.child("Titulo").value.toString()

                        penalty = snapshot.child("Penalizacion").value.toString().toInt()


                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }


            setup(dni ?: "", idQuiz ?: "")

    }

    private fun setupRevision(dni: String, idQuiz: String) {
        val color = textViewEnunciadoSolve.currentTextColor
        var nota = "%.2f".format(mark)
        textViewTituloQuiz.text = "Revision: Su nota es de "+ nota




        radioButtonOpcion1.setOnClickListener{

              //radioGroupQuiz.clearCheck()

        }

        radioButtonOpcion2.setOnClickListener{


                 //pQuiz.clearCheck()

        }

        radioButtonOpcion3.setOnClickListener{

                // radioGroupQuiz.clearCheck()

        }

        radioButtonOpcion4.setOnClickListener{

            //radioGrou pQuiz.clearCheck()

        }


                    when {
                        currentPos <= questionList.size -> {
                            setQuestion()

                            revision(checkAnswer(),color)


                            buttonPreguntaAnterior.setOnClickListener {


                                    setQuestion()

                                    currentPos -= 1
                                    setQuestion()
                                    revision(checkAnswer(),color)



                            }

                            buttonAvanzar.setOnClickListener {


                                if(currentPos<questionList!!.size) {


                                    currentPos ++
                                    setQuestion()
                                    revision(checkAnswer(),color)




                                }else{

                                        revision(checkAnswer(), color)
                                        controlfinalizarRevision(dni)

                                }
                            }


                        }else ->{
                        //No deberia de entrar nunca, si lo hace volver a la primera pregunta
                        //Control de errores

                        mark = 0.0F
                        currentPos = 1
                        //setQuestion()
                    }
                    }

                    buttonCancelar.setOnClickListener {
                        controlfinalizarRevision(dni)
                    }



    }



    fun setup(dni:String,idQuiz:String) {

         if (idQuiz != null) {
             database.child("Cuestionarios").child(idQuiz).child("Preguntas").addValueEventListener(object :ValueEventListener{
                 override fun onDataChange(snapshot: DataSnapshot) {
                     if (snapshot.exists()) {
                         for (postSnapshot in snapshot.children) {
                             val idPregunta = postSnapshot.key
                             val numeroRespuestas = postSnapshot.child("numeroRespuestas").value
                             val enunciado = postSnapshot.child("enunciado").value
                             val respuesta1 = postSnapshot.child("respuesta1").value
                             val respuesta2 = postSnapshot.child("respuesta2").value
                             val respuesta3 = postSnapshot.child("respuesta3").value
                             val respuesta4 = postSnapshot.child("respuesta4").value
                             val correcta = postSnapshot.child("correcta").value.toString().toInt()
                             val questionDetails = Question(idPregunta.toString(),enunciado.toString(),numeroRespuestas.toString()
                                 ,respuesta1.toString(), respuesta2.toString(),respuesta3.toString()
                                 ,respuesta4.toString(), correcta)
                             questionList.add(questionDetails)


                         }

                         var correcta = HashMap<Int, Int>()


                         radioButtonOpcion1.setOnClickListener{
                                 control2 = 0.0
                                 control3= 0.0
                                 control4=0.0
                                 control1++
                                 if (control1%2 ==0.0) {
                                     radioGroupQuiz.clearCheck()
                                     control1 = 0.0
                                }

                         }

                         radioButtonOpcion2.setOnClickListener{

                                 control1 = 0.0
                                 control3 = 0.0
                                 control4 = 0.0
                                 control2++
                                 if (control2 % 2 == 0.0) {
                                     radioGroupQuiz.clearCheck()
                                     control2 = 0.0
                                 }

                         }

                         radioButtonOpcion3.setOnClickListener{

                                 control1 = 0.0
                                 control2 = 0.0
                                 control4 = 0.0
                                 control3++
                                 if (control3 % 2 == 0.0) {
                                     radioGroupQuiz.clearCheck()
                                     control3 = 0.0
                                 }

                         }

                         radioButtonOpcion4.setOnClickListener{

                                 control4++
                                 control1 = 0.0
                                 control2 = 0.0
                                 control3 = 0.0

                                 if (control4 % 2 == 0.0) {
                                     radioGroupQuiz.clearCheck()
                                     control4 = 0.0
                                 }

                         }


                         when {
                             currentPos <= questionList.size -> {
                                 setQuestion()






                                 buttonPreguntaAnterior.setOnClickListener {

                                         control1 = 0.0
                                         control2 = 0.0
                                         control3 = 0.0
                                         control4 = 0.0

                                         preguntas.put(currentPos, checkAnswer())
                                         correcta.put(currentPos, correctAnswer)
                                         currentPos -= 1
                                         setQuestion()
                                         saveQuestion(preguntas)



                                 }

                                 buttonAvanzar.setOnClickListener {

                                         control1 = 0.0
                                         control2 = 0.0
                                         control3 = 0.0
                                         control4 = 0.0


                                     if(currentPos<questionList!!.size) {

                                             preguntas.put(currentPos, checkAnswer())
                                             correcta.put(currentPos, correctAnswer)
                                             currentPos++
                                             setQuestion()
                                             saveQuestion(preguntas)


                                        
                                     }else{

                                             preguntas.put(currentPos, checkAnswer())
                                             correcta.put(currentPos, correctAnswer)
                                             mark(preguntas, correcta)
                                             saveQuestion(preguntas)


                                             if (mark < 0.0)
                                                 mark = 0.0F


                                             controlfinalizar(dni, idQuiz)

                                     }
                                 }


                             }else ->{
                             //No deberia de entrar nunca, si lo hace volver a la primera pregunta
                             //Control de errores

                             mark = 0.0F
                             currentPos = 1

                         }
                         }

                         buttonCancelar.setOnClickListener {

                             controlExit()

                         }


                     }

                 }

                 override fun onCancelled(error: DatabaseError) {

                 }

             })
         }


    }




    private fun revision(checkAnswer: Int, color: Int) {
        val question = questionList[currentPos -1]

        when(preguntas[currentPos]){
            1->{
                radioButtonOpcion1.setTextColor(Color.parseColor("#FF0000"))
                radioButtonOpcion2.setTextColor(color)
                radioButtonOpcion3.setTextColor(color)
                radioButtonOpcion4.setTextColor(color)

            }
            2->{
                radioButtonOpcion2.setTextColor(Color.parseColor("#FF0000"))
                radioButtonOpcion1.setTextColor(color)
                radioButtonOpcion3.setTextColor(color)
                radioButtonOpcion4.setTextColor(color)
            }
            3->{
                radioButtonOpcion3.setTextColor(Color.parseColor("#FF0000"))
                radioButtonOpcion2.setTextColor(color)
                radioButtonOpcion1.setTextColor(color)
                radioButtonOpcion4.setTextColor(color)
            }
            4->{
                radioButtonOpcion4.setTextColor(Color.parseColor("#FF0000"))
                radioButtonOpcion2.setTextColor(color)
                radioButtonOpcion1.setTextColor(color)
                radioButtonOpcion4.setTextColor(color)
            }
        }
        when(question.correcta){
            1->{
                radioButtonOpcion1.setTextColor(Color.parseColor("#00FF00"))


            }
            2->radioButtonOpcion2.setTextColor(Color.parseColor("#00FF00"))
            3->radioButtonOpcion3.setTextColor(Color.parseColor("#00FF00"))
            4->radioButtonOpcion4.setTextColor(Color.parseColor("#00FF00"))
        }

    }

    private fun guardaNota(dni: String, idQuiz: String, nota: Float) {

        database.child("Alumnos").child(dni).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                database.child("Alumnos-Cuestionarios").child(dni).child("nombre").setValue(snapshot.child("nombre").value.toString())
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

        database.child("Cuestionarios").child(idQuiz).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                database.child("Alumnos-Cuestionarios").child(dni).child(idQuiz).child("titulo").setValue(snapshot.child("Titulo").value.toString())
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        database.child("Alumnos-Cuestionarios").child(dni).child(idQuiz).child("nota").setValue(nota)
    }

    private fun controlExit() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cuidado!")
        builder.setMessage("No has completado el cuestionario, ¿seguro que quieres cancelar?")
        builder.setNegativeButton("Volver", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.setPositiveButton("Salir", DialogInterface.OnClickListener { dialog, which ->  onBackPressed() })
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun controlExitRevision() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cuidado!")
        builder.setMessage("No has completado la revision, ¿seguro que quieres cancelar?")
        builder.setNegativeButton("Volver", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.setPositiveButton("Salir", DialogInterface.OnClickListener { dialog, which ->  onBackPressed() })
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun controlfinalizar(dni: String, idQuiz: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cuidado!")
        builder.setMessage("Una vez enviado no se puede modificar, ¿Deseas terminar el cuestionario?")
        builder.setNegativeButton("Volver", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.setPositiveButton("Enviar", DialogInterface.OnClickListener { dialog, which ->
            guardaNota(dni,idQuiz,mark)
            currentPos=1
            showNota(mark)
            setupRevision(dni,idQuiz)
            //setup(dni,idQuiz,mark)
            })
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun controlfinalizarRevision(dni: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Revision Completada")
        builder.setMessage("Una vez salgas no se puede volver, ¿Deseas terminar la revision?")
        builder.setNegativeButton("Volver", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.setPositiveButton("Salir", DialogInterface.OnClickListener { dialog, which ->

            showHome(dni)
        })
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    private fun saveQuestion(preguntas: HashMap<Int, Int>) {
        var checked = 0
        if(preguntas.get(currentPos) == null){
            preguntas.put(currentPos,0)
        }else{
            checked = preguntas.get(currentPos)!!
            when(checked){
                1 ->{
                    radioButtonOpcion1.isChecked = true
                    control1++
                }
                2 ->{
                    radioButtonOpcion2.isChecked = true
                    control2++
                }
                3 ->{
                    radioButtonOpcion3.isChecked = true
                    control3++
                }
                4-> {
                    radioButtonOpcion4.isChecked = true
                    control4++
                }
                else ->{
                    radioGroupQuiz.clearCheck()
                }
            }
        }
    }

    private fun checkAnswer():Int{


        var check = 0
        if (radioButtonOpcion1.isChecked)
            check = 1

        if (radioButtonOpcion2.isChecked)
            check = 2

        if (radioButtonOpcion3.isChecked)
            check = 3

        if (radioButtonOpcion4.isChecked)
            check = 4

        radioGroupQuiz.clearCheck()
        return check




    }

    private fun mark(preguntas: HashMap<Int, Int>, correcta: HashMap<Int, Int>){
        var resultado = 0.0
        var mal = penalty
        var i = 1
        var valor = 10.0/preguntas.size
        var incorret = valor * (mal/100.0)

        while (i<=preguntas.size) {

            when (preguntas.get(i)) {

                correcta.get(i) -> {
                    resultado = resultado + valor


                }

                0 -> {

                    resultado = resultado


                }

                else -> {

                    resultado = resultado - incorret


                }
            }
            i++

        }
        mark = resultado.toFloat()
    }

    private fun setQuestion(){


        val question = questionList[currentPos -1]

        when(question.numeroRespuestas){
            "4"->{

                textViewNumeroPregunta.text = "Pregunta " + "$currentPos"
                textViewEnunciadoSolve.text = question.enunciado
                radioButtonOpcion1.text = question.opcion1
                radioButtonOpcion2.text = question.opcion2
                radioButtonOpcion3.text = question.opcion3
                radioButtonOpcion4.text = question.opcion4
                correctAnswer = question.correcta
            }
            "3"->{
                textViewNumeroPregunta.text = "Pregunta " + "$currentPos"
                textViewEnunciadoSolve.text = question.enunciado
                radioButtonOpcion1.text = question.opcion1
                radioButtonOpcion2.text = question.opcion2
                radioButtonOpcion3.text = question.opcion3
                radioButtonOpcion4.visibility =View.INVISIBLE
                correctAnswer = question.correcta

            }else->{
                textViewNumeroPregunta.text = "Pregunta " + "$currentPos"
                textViewEnunciadoSolve.text = question.enunciado
                radioButtonOpcion1.text = question.opcion1
                radioButtonOpcion2.text = question.opcion2
                radioButtonOpcion3.visibility =View.INVISIBLE
                radioButtonOpcion4.visibility =View.INVISIBLE
                correctAnswer = question.correcta
            }
        }




        if(currentPos==1) {
            buttonPreguntaAnterior.visibility = View.INVISIBLE
        }else{
            buttonPreguntaAnterior.visibility = View.VISIBLE
        }
        //Se comprueba si es la ultima pregunta si lo es se cambia el mensaje del boton
        if (currentPos == questionList!!.size) {
            buttonAvanzar.text = "Finalizar"
        }else{
            buttonAvanzar.text = "Siguiente"
        }
    }


    private fun showNota(tipo: String, dni: String,idQuiz: String,nota: Double) {
        val mIntent = Intent(this,Selecionado::class.java).apply {
            putExtra("idQuiz",idQuiz)
            putExtra("tipoSelect",tipo)
            putExtra("dni",dni)
            putExtra("nota",nota)
        }
        startActivity(mIntent)
    }

    fun showNota(nota: Float){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cuestionario Finalizado")
        builder.setMessage("Nota final: "+"$nota")
        builder.setPositiveButton("Iniciar Revision",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(dni: String) {
        val mIntent = Intent(this,HomeAlumno::class.java).apply {
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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cuidado!")
        builder.setMessage("No has completado el cuestionario, ¿seguro que quieres cancelar?")
        builder.setNegativeButton("Volver", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.setPositiveButton("Salir", DialogInterface.OnClickListener { dialog, which ->  onBackPressed() })
        val dialog: AlertDialog = builder.create()
        dialog.show()
        return false
    }

}