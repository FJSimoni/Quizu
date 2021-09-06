package com.example.quizu

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_lista.*


class Lista : AppCompatActivity() {
    val database = Firebase.database("https://quizu-f4483-default-rtdb.europe-west1.firebasedatabase.app/").reference
    var pos = -1
    var control = false
    var tipoLista =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)

        val bundle = intent.extras
        val tipo = bundle?.getString("ListaDe")
        val idQuiz = bundle?.getString("idQuiz")
        val idAsignatura = bundle?.getString("idAsignatura")
        val idAlumno = bundle?.getString("idAlumno")
        val dni = bundle?.getString("dni")

        tipoLista = tipo.toString()

        setup(tipo ?:"", idQuiz ?:"",idAsignatura ?:"",idAlumno ?:"",dni ?:"")


    }

    private fun setup(tipo: String, idQuiz: String, idAsignatura: String, idAlumno:String,dni: String) {

        buttonNotas.visibility = View.INVISIBLE


        var adapter :ArrayAdapter<String>

        if (tipo == "preguntas") {
            buttonSeleccionar.visibility = View.INVISIBLE

            textViewLista.text = "Lista de preguntas: "
            var aenunciado = arrayListOf<String>()
            var numRes = arrayListOf<String>()
            var aopcion1 = arrayListOf<String>()
            var aopcion2  = arrayListOf<String>()
            var aopcion3 = arrayListOf<String>()
            var aopcion4 = arrayListOf<String>()
            var acorrecta = arrayListOf<String>()
            var aIdpregunta = arrayListOf<String>()

            adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,aenunciado)

            database.child("Asignaturas-Preguntas").child(idAsignatura).addValueEventListener(object :ValueEventListener{
                override fun onDataChange(psnapshot: DataSnapshot) {
                    if (psnapshot.exists()) {
                        for (postSnapshot in psnapshot.children) {
                            var id = postSnapshot.key

                            database.child("Preguntas").child(id.toString()).addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {

                                    if (snapshot.exists()) {

                                            var enunciado = snapshot.child("enunciado").value
                                            var numeroRespuestas = snapshot.child("numeroRespuestas").value
                                            var opcion1 = snapshot.child("respuesta1").value
                                            var opcion2 = snapshot.child("respuesta2").value
                                            var opcion3 = snapshot.child("respuesta3").value
                                            var opcion4 =snapshot.child("respuesta4").value
                                            var correcta = snapshot.child("correcta").value
                                            var idPregunta = snapshot.key

                                            numRes.add(numeroRespuestas.toString())
                                            aopcion1.add(opcion1.toString())
                                            aopcion2.add(opcion2.toString())
                                            aopcion3.add(opcion3.toString())
                                            aopcion4.add(opcion4.toString())
                                            acorrecta.add(correcta.toString())
                                            aenunciado.add(enunciado.toString())
                                            aIdpregunta.add(idPregunta.toString())

                                            listView.adapter = adapter

                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    showAlert("Error al acceder a la base de datos ", ":")
                                }
                            })

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })


            listView.setOnItemClickListener { parent, view, position, id ->
                pos = position
                if(idQuiz==""){
                    showSelecionarPregunta("preguntasBanco",aIdpregunta[pos],idAsignatura)
                }else {
                    if (pos >= 0) {
                        when {
                            aopcion4[pos] != "null" -> {
                                database.child("Cuestionarios").child(idQuiz).child("Preguntas")
                                    .child(aIdpregunta[pos]).child("enunciado")
                                    .setValue(aenunciado[pos])
                                database.child("Cuestionarios").child(idQuiz).child("Preguntas")
                                    .child(aIdpregunta[pos]).child("respuesta1")
                                    .setValue(aopcion1[pos])
                                database.child("Cuestionarios").child(idQuiz).child("Preguntas")
                                    .child(aIdpregunta[pos]).child("respuesta2")
                                    .setValue(aopcion2[pos])
                                database.child("Cuestionarios").child(idQuiz).child("Preguntas")
                                    .child(aIdpregunta[pos]).child("respuesta3")
                                    .setValue(aopcion3[pos])
                                database.child("Cuestionarios").child(idQuiz).child("Preguntas")
                                    .child(aIdpregunta[pos]).child("respuesta4")
                                    .setValue(aopcion4[pos])
                                database.child("Cuestionarios").child(idQuiz).child("Preguntas")
                                    .child(aIdpregunta[pos]).child("correcta")
                                    .setValue(acorrecta[pos])
                                database.child("Cuestionarios").child(idQuiz).child("Preguntas")
                                    .child(aIdpregunta[pos]).child("numeroRespuestas")
                                    .setValue(numRes[pos])

                            }
                            aopcion3[pos] != "null" -> {
                                database.child("Cuestionarios").child(idQuiz).child("Preguntas")
                                    .child(aIdpregunta[pos]).child("enunciado")
                                    .setValue(aenunciado[pos])
                                database.child("Cuestionarios").child(idQuiz).child("Preguntas")
                                    .child(aIdpregunta[pos]).child("respuesta1")
                                    .setValue(aopcion1[pos])
                                database.child("Cuestionarios").child(idQuiz).child("Preguntas")
                                    .child(aIdpregunta[pos]).child("respuesta2")
                                    .setValue(aopcion2[pos])
                                database.child("Cuestionarios").child(idQuiz).child("Preguntas")
                                    .child(aIdpregunta[pos]).child("respuesta3")
                                    .setValue(aopcion3[pos])
                                database.child("Cuestionarios").child(idQuiz).child("Preguntas")
                                    .child(aIdpregunta[pos]).child("correcta")
                                    .setValue(acorrecta[pos])
                                database.child("Cuestionarios").child(idQuiz).child("Preguntas")
                                    .child(aIdpregunta[pos]).child("numeroRespuestas")
                                    .setValue(numRes[pos])


                            }
                            else -> {
                                database.child("Cuestionarios").child(idQuiz).child("Preguntas")
                                    .child(aIdpregunta[pos]).child("enunciado")
                                    .setValue(aenunciado[pos])
                                database.child("Cuestionarios").child(idQuiz).child("Preguntas")
                                    .child(aIdpregunta[pos]).child("respuesta1")
                                    .setValue(aopcion1[pos])
                                database.child("Cuestionarios").child(idQuiz).child("Preguntas")
                                    .child(aIdpregunta[pos]).child("respuesta2")
                                    .setValue(aopcion2[pos])
                                database.child("Cuestionarios").child(idQuiz).child("Preguntas")
                                    .child(aIdpregunta[pos]).child("correcta")
                                    .setValue(acorrecta[pos])
                                database.child("Cuestionarios").child(idQuiz).child("Preguntas")
                                    .child(aIdpregunta[pos]).child("numeroRespuestas")
                                    .setValue(numRes[pos])

                            }


                        }

                        showFormQuiz(idQuiz, idAsignatura)
                    }
                }



            }

        }

        if (tipo == "asignaturasProfesor") {
            buttonSeleccionar.visibility = View.INVISIBLE
            textViewLista.text = "Lista de asignaturas: "
            var anombre = arrayListOf<String>()
            var acurso = arrayListOf<String>()
            var aid = arrayListOf<String>()


            adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, anombre)

            database.child("Asignaturas").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()) {
                        for (postSnapshot in snapshot.children) {
                            var nombre = postSnapshot.child("nombre").value
                            var curso = postSnapshot.child("curso").value

                            var idAsignatura = postSnapshot.key

                            anombre.add(nombre.toString())
                            acurso.add(curso.toString())
                            aid.add(idAsignatura.toString())


                            listView.adapter = adapter
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showAlert("Error al acceder a la base de datos ", ":")
                }
            })

            listView.adapter = adapter

            listView.setOnItemClickListener { parent, view, position, id ->
                showAsignatura(aid[position])
            }

        }

        if(tipo == "alumnos") {
            var idA = idAsignatura
            buttonSeleccionar.text = "Notas"
            buttonSeleccionar.visibility = View.INVISIBLE
            textViewLista.text = "Lista de alumnos: "
            var anombre = arrayListOf<String>()
            var aemail = arrayListOf<String>()
            var adni = arrayListOf<String>()

            var aidAlumnos = arrayListOf<String>()
            adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, anombre)
            database.child("Asignaturas-Alumnos").child(idA).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()) {
                        for (postSnapshot in snapshot.children) {

                            var dni = postSnapshot.child("dni").value
                            database.child("Alumnos").child(dni.toString()).addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {

                                            var dnid = snapshot.child("dni").value
                                            var nombre = snapshot.child("nombre").value
                                            var email = snapshot.child("email").value
                                            var idAlumno = snapshot.key

                                            anombre.add(nombre.toString())
                                            aemail.add(email.toString())
                                            adni.add(dnid.toString())
                                            aidAlumnos.add(idAlumno.toString())


                                            listView.adapter = adapter

                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    showAlert("Error al acceder a la base de datos ", ":")
                                }
                            })

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showAlert("Error al acceder a la base de datos ", ":")
                }
            })



            listView.setOnItemClickListener { parent, view, position, id ->
                pos = position
                textViewDesplegadoLista.text = "DNI: " + adni[position] +
                        System.getProperty("line.separator") +
                        "Nombre: " + anombre[position] +
                        System.getProperty("line.separator") +
                        "Email: " + aemail[position]
                buttonSeleccionar.visibility = View.VISIBLE
            }

            buttonSeleccionar.setOnClickListener {
                showListaNotas("notasProfesor",idAsignatura,adni[pos])

            }

        }

        if(tipo == "cuestionariosProfesor") {
            buttonNotas.visibility = View.INVISIBLE
            buttonSeleccionar.visibility = View.INVISIBLE
            var idA= idAsignatura
            textViewLista.text = "Lista de cuestionarios: "
            var atitulo = arrayListOf<String>()
            var apenalty = arrayListOf<String>()
            var aidCuestionarios = arrayListOf<String>()
            var aPreguntas = arrayListOf<String>()

            adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, atitulo)

            database.child("Asignaturas-Cuestionarios").child(idA).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (postSnapshot in snapshot.children) {
                            var idCuestionario = postSnapshot.key

                            database.child("Cuestionarios").child(idCuestionario.toString()).addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {

                                            var titulo = snapshot.child("Titulo").value
                                            var penalty = snapshot.child("Penalizacion").value
                                            var idCuestionario = snapshot.key
                                            var pregunta = snapshot.child("Preguntas")

                                            atitulo.add(titulo.toString())
                                            apenalty.add(penalty.toString())
                                            aidCuestionarios.add(idCuestionario.toString())
                                            aPreguntas.add(pregunta.toString())

                                            listView.adapter = adapter
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    showAlert("Error al acceder a la base de datos ", ":")
                                }
                            })
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showAlert("Error al acceder a la base de datos ", ":")
                }
            })

            listView.setOnItemClickListener { parent, view, position, id ->
                pos = position

                showSelecionar("cuestionarios",aidCuestionarios[pos],idAsignatura)

            }

            buttonNotas.setOnClickListener {
                showListaNotasAsignatura("notasCuestionarios",idAsignatura,aidCuestionarios[pos])
            }

            buttonSeleccionar.setOnClickListener {
                showSelecionar("cuestionarios",aidCuestionarios[pos],idAsignatura)
            }
        }

        if(tipo == "añadirCuestionarios") {
            buttonSeleccionar.text="Añadir"
            textViewLista.text = "Lista de cuestionarios: "
            var atitulo = arrayListOf<String>()
            var apenalty = arrayListOf<String>()
            var aidCuestionarios = arrayListOf<String>()

            adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, atitulo)

                database.child("Cuestionarios").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (postSnapshot in snapshot.children) {
                                var titulo = postSnapshot.child("Titulo").value
                                var penalty = postSnapshot.child("Penalizacion").value
                                var idCuestionario = postSnapshot.key

                                atitulo.add(titulo.toString())
                                apenalty.add(penalty.toString())
                                aidCuestionarios.add(idCuestionario.toString())


                                listView.adapter = adapter
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        showAlert("Error al acceder a la base de datos ", ":")
                    }
                })



            listView.setOnItemClickListener { parent, view, position, id ->
                pos = position
                textViewDesplegadoLista.text = "Titulo: " + atitulo[position] +
                        System.getProperty("line.separator") +
                        "Penalizacion al fallo: " + apenalty[position]

            }

            buttonSeleccionar.setOnClickListener {
                añadirCuestionario(idAsignatura,aidCuestionarios[pos])
            }

        }

        if(tipo == "notasProfesor") {
            buttonSeleccionar.text="Nota Media"
            var idA= idAsignatura
            var dni = idAlumno
            textViewLista.text = "Lista de cuestionarios: "



            var anotas = arrayListOf<String>()
            var atitulo = arrayListOf<String>()
            adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, atitulo)

            database.child("Asignaturas-Cuestionarios").child(idA).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (postSnapshot in snapshot.children) {
                            var idCuestionario = postSnapshot.key
                            database.child("Alumnos-Cuestionarios").child(dni).addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {

                                            for (xpostSnapshot in snapshot.children) {
                                                if (xpostSnapshot.exists()) {

                                                    if (idCuestionario.toString() == xpostSnapshot.key.toString()) {

                                                        var nota = xpostSnapshot.child("nota").value
                                                        var titulo =
                                                            xpostSnapshot.child("titulo").value


                                                        anotas.add(nota.toString())
                                                        atitulo.add(titulo.toString())
                                                    }




                                                    listView.adapter = adapter
                                                }
                                            }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    showAlert("Error al acceder a la base de datos ", ":")
                                }
                            })

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showAlert("Error al acceder a la base de datos ", ":")
                }
            })



            listView.setOnItemClickListener { parent, view, position, id ->
                pos = position
                textViewDesplegadoLista.text = "Titulo: " + atitulo[position] +
                        System.getProperty("line.separator") +
                        "Nota: " + anotas[position]

            }

            buttonSeleccionar.setOnClickListener {
                var notaMedia = 0.0
                for(i in anotas)
                    notaMedia += i.toDouble()

                notaMedia /= anotas.size
                textViewNotaMedia.text = "Nota media en los cuestionarios: "+ notaMedia.toString()
            }

        }

        if(tipo == "notasCuestionarios") {
            buttonNotas.visibility=View.VISIBLE
            buttonNotas.text = "Nota media"
            var idA= idAsignatura
            var idCuestionario = idQuiz
            textViewLista.text = "Seleccione un cuestionario: "


            var anombre = arrayListOf<String>()
            var anotas = arrayListOf<String>()
            var adni = arrayListOf<String>()

            adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, adni)
            database.child("Alumnos-Cuestionarios").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (postSnapshot in snapshot.children) {
                            for (ppostSnapshot in postSnapshot.children) {

                                if(ppostSnapshot.key.toString() == idCuestionario) {
                                    var id = postSnapshot.key
                                    var nota = ppostSnapshot.child("nota").value
                                    var nombre = postSnapshot.child("nombre").value

                                    anotas.add(nota.toString())
                                    adni.add(id.toString())
                                    anombre.add(nombre.toString())

                                    listView.adapter = adapter
                                }
                            }

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showAlert("Error al acceder a la base de datos ", ":")
                }
            })

            listView.setOnItemClickListener { parent, view, position, id ->
                pos = position
                textViewDesplegadoLista.text = "DNI: " + adni[position] +
                        System.getProperty("line.separator") +
                        "Nombre: " + anombre[position]+
                        System.getProperty("line.separator") +
                        "Nota: " + anotas[position]
            }

            buttonNotas.setOnClickListener {
                var notaMedia = 0.0
                for(i in anotas)
                    notaMedia += i.toDouble()

                notaMedia /= anotas.size
                textViewDesplegadoLista.text = "Nota media de los alumnos: "+ notaMedia.toString()
            }

        }

        if(tipo == "cuestionariosLanzados") {
            var idA= idAsignatura

            textViewLista.text = "Lista de cuestionarios: "
            buttonSeleccionar.text = "Cerrar cuestionario"
            buttonSeleccionar.visibility = View.INVISIBLE

            var atitulo = arrayListOf<String>()
            var acodigo = arrayListOf<String>()
            var aid = arrayListOf<String>()

            adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, atitulo)
            database.child("Asignaturas-Cuestionarios").child(idA).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (postSnapshot in snapshot.children) {
                            var idCuestionario = postSnapshot.key


                            database.child("Cuestionarios").child(idCuestionario.toString())
                                .addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()) {
                                                if (snapshot.child("codigo")
                                                        .exists() //&& snapshot.child("codigo").value.toString() != ""
                                                ) {
                                                    var id = snapshot.key
                                                    var codigo = snapshot.child("codigo").value
                                                    var titulo = snapshot.child("Titulo").value

                                                    atitulo.add(titulo.toString())
                                                    aid.add(id.toString())
                                                    acodigo.add(codigo.toString())

                                                    listView.adapter = adapter
                                                }


                                        }
                                    }


                                    override fun onCancelled(error: DatabaseError) {
                                        showAlert("Error al acceder a la base de datos ", ":")
                                    }
                                })

                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    showAlert("Error al acceder a la base de datos ", ":")
                }
            })

            listView.setOnItemClickListener { parent, view, position, id ->
                pos = position
                buttonSeleccionar.visibility = View.VISIBLE
                textViewDesplegadoLista.text = "ID: " + aid[position] +
                        System.getProperty("line.separator") +
                        "Titulo: " + atitulo[position]+
                        System.getProperty("line.separator") +
                        "Codigo: " + acodigo[position]
            }

            buttonSeleccionar.setOnClickListener {
                cerrarCuestionario(aid[pos],idAsignatura)
            }

        }


        if(tipo == "asignaturasAlumnos") {
            var idni = dni
            buttonSeleccionar.visibility = View.INVISIBLE
            buttonSeleccionar.text = "Matricularse"
            textViewLista.text = "Lista de asignaturas: "
            var anombre = arrayListOf<String>()
            var acurso = arrayListOf<String>()
            var aid = arrayListOf<String>()


            adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, anombre)

            database.child("Asignaturas").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()) {
                        for (postSnapshot in snapshot.children) {
                            var nombre = postSnapshot.child("nombre").value
                            var curso = postSnapshot.child("curso").value

                            var idAsignatura = postSnapshot.key

                            anombre.add(nombre.toString())
                            acurso.add(curso.toString())
                            aid.add(idAsignatura.toString())


                            listView.adapter = adapter
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showAlert("Error al acceder a la base de datos ", ":")
                }
            })

            listView.setOnItemClickListener { parent, view, position, id ->
                pos = position


                database.child("Asignaturas-Alumnos").child(aid[pos]).child(dni).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            showAsignaturaAlumno(aid[pos], dni)

                        } else {

                            buttonSeleccionar.visibility = View.VISIBLE
                            textViewDesplegadoLista.text = "Nombre: " + anombre[pos] +
                                    System.getProperty("line.separator") +
                                    "Curso: " + acurso[pos]

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        showAlert("Error al acceder a la base de datos ", ":")
                    }
                })

            }

            buttonSeleccionar.setOnClickListener {
                matricularAlumno(aid[pos],dni)
                showAsignaturaAlumno(aid[pos],dni)

            }


        }

        if(tipo == "misAsignaturas") {

            buttonSeleccionar.visibility = View.INVISIBLE

            textViewLista.text = "Lista de asignaturas: "
            var anombre = arrayListOf<String>()
            var acurso = arrayListOf<String>()
            var aid = arrayListOf<String>()
            var aidAsignatura = arrayListOf<String>()

            adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, anombre)

            database.child("Asignaturas").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()) {
                        for (postSnapshot in snapshot.children) {

                            var idAsignatura = postSnapshot.key

                            database.child("Asignaturas-Alumnos").child(idAsignatura.toString()).child(dni).addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(tsnapshot: DataSnapshot) {

                                    if (tsnapshot.exists()) {

                                        var nombre = postSnapshot.child("nombre").value
                                        var curso = postSnapshot.child("curso").value
                                        var asignatura = database.child("Asignaturas-Alumnos").child(idAsignatura.toString()).key

                                        aidAsignatura.add(asignatura.toString())
                                        anombre.add(nombre.toString())
                                        acurso.add(curso.toString())
                                        listView.adapter = adapter
                                    }


                                }

                                override fun onCancelled(error: DatabaseError) {
                                    showAlert("Error al acceder a la base de datos ", ":")
                                }
                            })



                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showAlert("Error al acceder a la base de datos ", ":")
                }
            })

            listView.setOnItemClickListener { parent, view, position, id ->
                pos = position

                showAsignaturaAlumno(aidAsignatura[pos],dni)
            }


        }

        if(tipo == "cuestionariosAlumno") {
            buttonSeleccionar.visibility = View.INVISIBLE
            var idA= idAsignatura
            var idni = dni
            var atitulo = arrayListOf<String>()
            var anotas = arrayListOf<String>()


            adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, atitulo)
            textViewLista.text = "Lista de cuestionarios: "
            var aidCuestionarios = arrayListOf<String>()

            database.child("Asignaturas-Cuestionarios").child(idAsignatura).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (postSnapshot in snapshot.children) {
                            for(idC in postSnapshot.children) {
                                var idCuestionario = idC.value
                                database.child("Alumnos-Cuestionarios").child(dni)
                                    .addValueEventListener(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (snapshot.exists()) {
                                                for (postSnapshot in snapshot.children) {

                                                        if (postSnapshot.key.toString() == idCuestionario.toString()) {

                                                            var titulo =
                                                                postSnapshot.child("titulo").value
                                                            var nota =
                                                                postSnapshot.child("nota").value

                                                            anotas.add(nota.toString())
                                                            atitulo.add(titulo.toString())


                                                            listView.adapter = adapter
                                                        }


                                                }
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            showAlert("Error al acceder a la base de datos ", ":")
                                        }
                                    })
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showAlert("Error al acceder a la base de datos ", ":")
                }
            })


            listView.setOnItemClickListener { parent, view, position, id ->
                pos = position
                textViewDesplegadoLista.text = "Titulo: " + atitulo[position] +
                        System.getProperty("line.separator") +
                        "Nota: " + anotas[position]
            }


        }



    }



    private fun cerrarCuestionario(idQuiz: String, idAsignatura: String) {
        database.child("Cuestionarios").child(idQuiz).child("codigo").removeValue()
        onBackPressed()
    }

    private fun showSelecionarPregunta(tipo:String,idPregunta: String,idAsignatura: String) {
        val intent = Intent(this,Selecionado::class.java).apply {
            putExtra("idPregunta",idPregunta)
            putExtra("tipoSelect",tipo)
            putExtra("idAsignatura",idAsignatura)
        }
        startActivity(intent)
    }

    private fun matricularAlumno(idAsignatura: String, dni: String) {


        database.child("Asignaturas-Alumnos").child(idAsignatura).child(dni).child("dni").setValue(dni)//todo
    }


    private fun añadirCuestionario(idAsignatura: String, idCuestionario: String) {
        database.child("Asignaturas-Cuestionarios").child(idAsignatura).child(idCuestionario).setValue(idCuestionario)
        showAsignatura(idAsignatura)
    }

    private fun showSelecionar(tipo: String,idQuiz: String,idAsignatura: String) {
        val intent = Intent(this,Selecionado::class.java).apply {
            putExtra("idQuiz",idQuiz)
            putExtra("tipoSelect",tipo)
            putExtra("idAsignatura",idAsignatura)
        }
        startActivity(intent)
    }


    private fun showAsignatura(idAsignatura: String) {
        val intent = Intent(this,AsignaturaProfesor::class.java).apply {
            putExtra("idAsignatura",idAsignatura)
        }
        startActivity(intent)
    }

    private fun showAsignaturaAlumno(idAsignatura: String, dni: String) {
        val intent = Intent(this,AsignaturaAlumno::class.java).apply {
            putExtra("idAsignatura",idAsignatura)
            putExtra("dni",dni)
        }
        startActivity(intent)
    }


    fun showAlert(mensaje:String,value: String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("$mensaje" + "$value")
        builder.setNegativeButton("Aceptar" , DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showFormQuiz(idQuiz: String, idAsignatura: String) {
        val homeIntent = Intent(this,FormQuiz::class.java).apply {
            putExtra("idQuiz",idQuiz)
            putExtra("idAsignatura",idAsignatura)
            //putExtra("idQuestion",idQuestion)
        }
        startActivity(homeIntent)
    }

    private fun showListaNotas(tipo: String,idAsignatura: String,dni:String) {
        val homeIntent = Intent(this,Lista::class.java).apply {
            putExtra("ListaDe",tipo)
            putExtra("idAsignatura",idAsignatura)
            putExtra("idAlumno",dni)
        }
        startActivity(homeIntent)
    }

    private fun showListaNotasAsignatura(tipo: String, idAsignatura: String, idCuestionario: String) {
        val homeIntent = Intent(this,Lista::class.java).apply {
            putExtra("ListaDe",tipo)
            putExtra("idAsignatura",idAsignatura)
            putExtra("idQuiz",idCuestionario)
        }
        startActivity(homeIntent)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (tipoLista=="asignaturasProfesor") {
            showHome()
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun showNotas(idAsignatura: String, dni :String) {
        val mIntent = Intent(this, Notas::class.java).apply {
            putExtra("tipo","alumno")
            putExtra("idAsignatura", idAsignatura)
            putExtra("dni",dni)
        }
        startActivity(mIntent)
    }

    private fun showHome() {
        val homeIntent = Intent(this,HomeActivity::class.java).apply {
            }
        startActivity(homeIntent)
    }

    private fun showHomeAlumno(dni:String) {
        val homeIntent = Intent(this,HomeAlumno::class.java).apply {
            putExtra("dni",dni)
        }
        startActivity(homeIntent)
    }

}