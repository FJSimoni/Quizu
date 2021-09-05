package com.example.quizu

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_selecionado.*
import java.util.*

class Selecionado : AppCompatActivity() {

    val database = Firebase.database("https://quizu-f4483-default-rtdb.europe-west1.firebasedatabase.app/").reference
    override fun onCreate(savedInstanceState: Bundle?) {
        val bundle = intent.extras
        val tipo = bundle?.getString("tipoSelect")
        val idQuiz = bundle?.getString("idQuiz")
        val idPregunta = bundle?.getString("idPregunta")
        val idAsignatura = bundle?.getString("idAsignatura")
        var dni = bundle?.getString("dni")
        var mark = bundle?.getDouble("nota")


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selecionado)


        setup(idQuiz ?: "", idPregunta?:"", tipo ?: "", idAsignatura ?:"", dni ?:"", mark?:0.0)

    }

    private fun setup(


        idQuiz: String,
        idPregunta: String,
        tipo: String,
        idAsignatura: String,
        dni: String,
        mark: Double
    ) {

        buttonLanzarCuestionario.visibility = View.INVISIBLE


       if (tipo=="preguntas") {

           database.child("Preguntas").child(idPregunta).addValueEventListener(object :ValueEventListener{
               override fun onDataChange(snapshot: DataSnapshot) {
                   var question = snapshot.child("enunciado").value.toString()
                   var aopcion1 = snapshot.child("respuesta1").value.toString()
                   var aopcion2 = snapshot.child("respuesta2").value.toString()
                   var aopcion3 = snapshot.child("respuesta3").value.toString()
                   var aopcion4 = snapshot.child("respuesta4").value.toString()
                   var acorrecta = snapshot.child("correcta").value.toString()


                   when {
                       aopcion4 != "null" -> {
                           textViewSeleccionadoArriba.text = "Enunciado: " + question +
                                   System.getProperty("line.separator") +
                                   "Opcion 1: " + aopcion1 +
                                   System.getProperty("line.separator") +
                                   "Opcion 2: " + aopcion2 +
                                   System.getProperty("line.separator") +
                                   "Opcion 3: " + aopcion3 +
                                   System.getProperty("line.separator") +
                                   "Opcion 4: " + aopcion4 +
                                   System.getProperty("line.separator") +
                                   "Correcta: Opcion " + acorrecta +
                                   System.getProperty("line.separator")
                       }
                       aopcion3 != "null" -> {
                           textViewSeleccionadoArriba.text = "Enunciado: " + question +
                                   System.getProperty("line.separator") +
                                   "Opcion 1: " + aopcion1 +
                                   System.getProperty("line.separator") +
                                   "Opcion 2: " + aopcion2 +
                                   System.getProperty("line.separator") +
                                   "Opcion 3: " + aopcion3 +
                                   System.getProperty("line.separator") +
                                   "Correcta: Opcion " + acorrecta +
                                   System.getProperty("line.separator")
                       }
                       else -> {
                           textViewSeleccionadoArriba.text = "Enunciado: " + question +
                                   System.getProperty("line.separator") +
                                   "Opcion 1: " + aopcion1 +
                                   System.getProperty("line.separator") +
                                   "Opcion 2: " + aopcion2+
                                   System.getProperty("line.separator") +
                                   "Correcta: Opcion " + acorrecta +
                                   System.getProperty("line.separator")
                       }
                   }

               }

               override fun onCancelled(error: DatabaseError) {

               }

           })

           buttonEliminarSeleccionado.setOnClickListener {
               if (idQuiz != null) {
                   controlEliminarPregunta(idQuiz, idPregunta,idAsignatura)
               }
           }


           buttonAtrasSeleccionado.setOnClickListener {
               if (idQuiz != null) {
                   onBackPressed()
               }
           }


       }

        if (tipo=="preguntasBanco") {

            database.child("Preguntas").child(idPregunta).addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var question = snapshot.child("enunciado").value.toString()
                    var aopcion1 = snapshot.child("respuesta1").value.toString()
                    var aopcion2 = snapshot.child("respuesta2").value.toString()
                    var aopcion3 = snapshot.child("respuesta3").value.toString()
                    var aopcion4 = snapshot.child("respuesta4").value.toString()
                    var acorrecta = snapshot.child("correcta").value.toString()


                    when {
                        aopcion4 != "null" -> {
                            textViewSeleccionadoArriba.text = "Enunciado: " + question +
                                    System.getProperty("line.separator") +
                                    "Opcion 1: " + aopcion1 +
                                    System.getProperty("line.separator") +
                                    "Opcion 2: " + aopcion2 +
                                    System.getProperty("line.separator") +
                                    "Opcion 3: " + aopcion3 +
                                    System.getProperty("line.separator") +
                                    "Opcion 4: " + aopcion4 +
                                    System.getProperty("line.separator") +
                                    "Correcta: Opcion " + acorrecta +
                                    System.getProperty("line.separator")
                        }
                        aopcion3 != "null" -> {
                            textViewSeleccionadoArriba.text = "Enunciado: " + question +
                                    System.getProperty("line.separator") +
                                    "Opcion 1: " + aopcion1 +
                                    System.getProperty("line.separator") +
                                    "Opcion 2: " + aopcion2 +
                                    System.getProperty("line.separator") +
                                    "Opcion 3: " + aopcion3 +
                                    System.getProperty("line.separator") +
                                    "Correcta: Opcion " + acorrecta +
                                    System.getProperty("line.separator")
                        }
                        else -> {
                            textViewSeleccionadoArriba.text = "Enunciado: " + question +
                                    System.getProperty("line.separator") +
                                    "Opcion 1: " + aopcion1 +
                                    System.getProperty("line.separator") +
                                    "Opcion 2: " + aopcion2+
                                    System.getProperty("line.separator") +
                                    "Correcta: Opcion " + acorrecta +
                                    System.getProperty("line.separator")
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

            buttonEliminarSeleccionado.setOnClickListener {
                if (idQuiz != null) {
                   controlEliminardeBanco(idPregunta,idAsignatura)
                }
            }


            buttonAtrasSeleccionado.setOnClickListener {
                if (idQuiz != null) {
                    onBackPressed()
                }
            }


        }

        if (tipo=="cuestionarios"){

            buttonLanzarCuestionario.visibility = View.VISIBLE
            var question = arrayListOf<String>()
            var numRes = arrayListOf<String>()
            var aopcion1 = arrayListOf<String>()
            var aopcion2  = arrayListOf<String>()
            var aopcion3 = arrayListOf<String>()
            var aopcion4 = arrayListOf<String>()
            var acorrecta = arrayListOf<String>()
            var aIdpregunta = arrayListOf<String>()
            var adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,question)

            database.child("Cuestionarios").child(idQuiz).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        if(snapshot.child("codigo").exists()) {
                            textViewSeleccionadoArriba.text =
                                "Titulo:" + snapshot.child("Titulo").value.toString() +
                                System.getProperty("line.separator") +
                                "Penalizacion: "+ snapshot.child("Penalizacion").value.toString()+
                                System.getProperty("line.separator") +
                                        "Codigo: "+snapshot.child("codigo").value.toString()
                        }else{
                            textViewSeleccionadoArriba.text =
                                "Titulo:" + snapshot.child("Titulo").value.toString() +
                                        System.getProperty("line.separator") +
                                        "Penalizacion: "+ snapshot.child("Penalizacion").value.toString()
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

            database.child("Cuestionarios").child(idQuiz).child("Preguntas").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for(postSnapshot in snapshot.children ){
                            var enunciado = postSnapshot.child("enunciado").value
                            var numeroRespuestas = postSnapshot.child("numeroRespuestas").value
                            var opcion1 = postSnapshot.child("respuesta1").value
                            var opcion2 = postSnapshot.child("respuesta2").value
                            var opcion3 = postSnapshot.child("respuesta3").value
                            var opcion4 =postSnapshot.child("respuesta4").value
                            var correcta = postSnapshot.child("correcta").value
                            var idPregunta = postSnapshot.key

                            numRes.add(numeroRespuestas.toString())
                            aopcion1.add(opcion1.toString())
                            aopcion2.add(opcion2.toString())
                            aopcion3.add(opcion3.toString())
                            aopcion4.add(opcion4.toString())
                            acorrecta.add(correcta.toString())
                            question.add(enunciado.toString())
                            aIdpregunta.add(idPregunta.toString())

                            listViewSeleccionado.adapter = adapter

                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    showAlert("Error al acceder a la base de datos ", ":")
                }
            })



            listViewSeleccionado.setOnItemClickListener { parent, view, position, id ->
                when {
                    aopcion4[position] != "null" -> {
                        textViewSelccionadoAbajo.text = "Enunciado: " + question[position] +
                                System.getProperty("line.separator") +
                                "Opcion 1: " + aopcion1[position] +
                                System.getProperty("line.separator") +
                                "Opcion 2: " + aopcion2[position] +
                                System.getProperty("line.separator") +
                                "Opcion 3: " + aopcion3[position] +
                                System.getProperty("line.separator") +
                                "Opcion 4: " + aopcion4[position] +
                                System.getProperty("line.separator") +
                                "Correcta: Opcion " + acorrecta[position] +
                                System.getProperty("line.separator")
                    }
                    aopcion3[position] != "null" -> {
                        textViewSelccionadoAbajo.text = "Enunciado: " + question[position] +
                                System.getProperty("line.separator") +
                                "Opcion 1: " + aopcion1[position] +
                                System.getProperty("line.separator") +
                                "Opcion 2: " + aopcion2[position] +
                                System.getProperty("line.separator") +
                                "Opcion 3: " + aopcion3[position] +
                                System.getProperty("line.separator") +
                                "Correcta: Opcion " + acorrecta[position] +
                                System.getProperty("line.separator")
                    }
                    else -> {
                        textViewSelccionadoAbajo.text = "Enunciado: " + question[position] +
                                System.getProperty("line.separator") +
                                "Opcion 1: " + aopcion1[position] +
                                System.getProperty("line.separator") +
                                "Opcion 2: " + aopcion2[position] +
                                System.getProperty("line.separator") +
                                "Correcta: Opcion " + acorrecta[position] +
                                System.getProperty("line.separator")
                    }
                }

            }

            buttonEliminarSeleccionado.setOnClickListener {
                if (idQuiz != null) {
                    controlEliminarCuestionario(idQuiz,idAsignatura)
                }
            }


            buttonAtrasSeleccionado.setOnClickListener {
                    onBackPressed()
            }


            buttonLanzarCuestionario.setOnClickListener {
                var codigo = lanzarCuestionario(idQuiz)
                showCodigo(codigo,idAsignatura)
            }


        }
        if (tipo=="nota") {

            database.child("Cuestionarios").child(idQuiz).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    textViewSeleccionadoArriba.text = "DNI: " + dni+
                            System.getProperty("line.separator") +
                            "Cuestionario: " + snapshot.child("Titulo").value.toString()+
                            System.getProperty("line.separator") +
                            "Nota: " + mark.toString() +
                            System.getProperty("line.separator")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })


            buttonLanzarCuestionario.visibility = View.INVISIBLE
            buttonEliminarSeleccionado.visibility = View.INVISIBLE
            var question = arrayListOf<String>()
            var numRes = arrayListOf<String>()
            var aopcion1 = arrayListOf<String>()
            var aopcion2  = arrayListOf<String>()
            var aopcion3 = arrayListOf<String>()
            var aopcion4 = arrayListOf<String>()
            var acorrecta = arrayListOf<String>()
            var aIdpregunta = arrayListOf<String>()
            var adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,question)


            database.child("Cuestionarios").child(idQuiz).child("Preguntas").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for(postSnapshot in snapshot.children ){
                            var enunciado = postSnapshot.child("enunciado").value
                            var numeroRespuestas = postSnapshot.child("numeroRespuestas").value
                            var opcion1 = postSnapshot.child("respuesta1").value
                            var opcion2 = postSnapshot.child("respuesta2").value
                            var opcion3 = postSnapshot.child("respuesta3").value
                            var opcion4 =postSnapshot.child("respuesta4").value
                            var correcta = postSnapshot.child("correcta").value
                            var idPregunta = postSnapshot.key

                            numRes.add(numeroRespuestas.toString())
                            aopcion1.add(opcion1.toString())
                            aopcion2.add(opcion2.toString())
                            aopcion3.add(opcion3.toString())
                            aopcion4.add(opcion4.toString())
                            acorrecta.add(correcta.toString())
                            question.add(enunciado.toString())
                            aIdpregunta.add(idPregunta.toString())

                            listViewSeleccionado.adapter = adapter

                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    showAlert("Error al acceder a la base de datos ", ":")
                }
            })


            listViewSeleccionado.setOnItemClickListener { parent, view, position, id ->
                when {
                    aopcion4[position] != "null" -> {
                        textViewSelccionadoAbajo.text = "Enunciado: " + question[position] +
                                System.getProperty("line.separator") +
                                "Opcion 1: " + aopcion1[position] +
                                System.getProperty("line.separator") +
                                "Opcion 2: " + aopcion2[position] +
                                System.getProperty("line.separator") +
                                "Opcion 3: " + aopcion3[position] +
                                System.getProperty("line.separator") +
                                "Opcion 4: " + aopcion4[position] +
                                System.getProperty("line.separator") +
                                "Correcta: Opcion " + acorrecta[position] +
                                System.getProperty("line.separator")
                    }
                    aopcion3[position] != "null" -> {
                        textViewSelccionadoAbajo.text = "Enunciado: " + question[position] +
                                System.getProperty("line.separator") +
                                "Opcion 1: " + aopcion1[position] +
                                System.getProperty("line.separator") +
                                "Opcion 2: " + aopcion2[position] +
                                System.getProperty("line.separator") +
                                "Opcion 3: " + aopcion3[position] +
                                System.getProperty("line.separator") +
                                "Correcta: Opcion " + acorrecta[position] +
                                System.getProperty("line.separator")
                    }
                    else -> {
                        textViewSelccionadoAbajo.text = "Enunciado: " + question[position] +
                                System.getProperty("line.separator") +
                                "Opcion 1: " + aopcion1[position] +
                                System.getProperty("line.separator") +
                                "Opcion 2: " + aopcion2[position] +
                                System.getProperty("line.separator") +
                                "Correcta: Opcion " + acorrecta[position] +
                                System.getProperty("line.separator")
                    }
                }

            }

            buttonAtrasSeleccionado.setOnClickListener {

                    showHomeAlumno(dni)


            }


        }
    }




    private fun lanzarCuestionario(idQuiz: String): String{
        var codigo = valorRandom(10000..99999)
        database.child("Cuestionarios").child(idQuiz).child("codigo").setValue(codigo)
        return codigo.toString()

    }


    fun valorRandom(valores: IntRange) : Int {
        var r = Random()
        var valorRandom = r.nextInt(valores.last - valores.first) + valores.first
        return valorRandom
    }





    private fun controlEliminarCuestionario(idQuiz: String,idAsignatura:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cuidado!")
        builder.setMessage("¿Esta seguro de eliminar el cuestionario?")
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.setPositiveButton("Eliminar", DialogInterface.OnClickListener { dialog, which ->
            var key = idQuiz
            database.child("Cuestionarios").child(key).removeValue()
            database.child("Asignaturas-Cuestionarios").child(idAsignatura).child(key).removeValue()
            showListCuestionarios("cuestionariosProfesor",idAsignatura)
        })
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showListCuestionarios(tipo: String, idAsignatura: String) {
        val homeIntent = Intent(this,Lista::class.java).apply {
            putExtra("ListaDe",tipo)
            putExtra("idAsignatura",idAsignatura)
        }
        startActivity(homeIntent)
    }





    private fun controlEliminarPregunta(idQuiz: String,idPregunta:String,idAsignatura: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cuidado!")
        builder.setMessage("¿Esta seguro de eliminar la pregunta del cuestionario?")
        builder.setNegativeButton("Volver al cuestionario", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.setPositiveButton("Eliminar", DialogInterface.OnClickListener { dialog, which ->
            borrarPregunta(idQuiz,idPregunta)
            showFormQuiz(idQuiz,idAsignatura)
        })
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun controlEliminardeBanco(idPregunta: String,idAsignatura: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cuidado!")
        builder.setMessage("¿Quiere eliminarla del banco de preguntas?")
        builder.setNegativeButton("No", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.setPositiveButton("Si", DialogInterface.OnClickListener { dialog, which ->
            borrarPreguntaBanco(idPregunta,idAsignatura)
            showAsigaturaProfesor(idAsignatura)
        })
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun borrarPreguntaBanco(idPregunta: String,idAsignatura: String) {
        var key = idPregunta
        database.child("Preguntas").child(key).removeValue()
        database.child("Asignaturas-Preguntas").child(idAsignatura).child(key).removeValue()
    }

    private fun borrarPregunta(idQuiz: String, idPregunta: String) {
        var keyQ = idQuiz
        var keyP = idPregunta
        database.child("Cuestionarios").child(keyQ).child("Preguntas").child(keyP).removeValue()
    }

    private fun showFormQuiz(idQuiz:String,idAsignatura: String) {
        val homeIntent = Intent(this,FormQuiz::class.java).apply {
            putExtra("idQuiz",idQuiz)
            putExtra("idAsignatura",idAsignatura)
        }
        startActivity(homeIntent)
    }


    private fun showHomeAlumno(dni: String) {
        val homeIntent = Intent(this,HomeAlumno::class.java).apply {
            putExtra("dni",dni)

        }
        startActivity(homeIntent)
    }

    private fun showFormQuestion(idQuiz: String?, idPregunta: String) {
        val homeIntent = Intent(this,FormQuestion::class.java).apply {
            putExtra("idQuiz",idQuiz)
            putExtra("iPregunta",idPregunta)
        }
        startActivity(homeIntent)
    }

    fun showAlert(mensaje:String,value: String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("$mensaje" + "$value")
        builder.setNegativeButton("Aceptar" , DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAsigaturaProfesor(idAsignatura: String) {
        val homeIntent = Intent(this,AsignaturaProfesor::class.java).apply {
            putExtra("idAsignatura",idAsignatura)
        }
        startActivity(homeIntent)
    }
    private fun showCodigo(codigo: String,idAsignatura: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cuestionario activado")
        builder.setMessage("Codigo:" + "$codigo")
        builder.setPositiveButton("Aceptar" , DialogInterface.OnClickListener { dialog, which ->  showAsigaturaProfesor(idAsignatura)})
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
