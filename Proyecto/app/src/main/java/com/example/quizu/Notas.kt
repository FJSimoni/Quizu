package com.example.quizu

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_notas.*
import java.io.*


class Notas : AppCompatActivity() {
    var adni = arrayListOf<String>()
    var anombre = arrayListOf<String>()
    var aCuestionarios = arrayListOf<String>()
    val notas = arrayListOf<Cuestionario>()
    var aTitulo = arrayListOf<String>()
    val database =
        Firebase.database("https://quizu-f4483-default-rtdb.europe-west1.firebasedatabase.app/").reference
    var control = false
    var medias = ArrayList<BarEntry>()
    var labelsG = ArrayList<String>()
    var participaciones = ArrayList<BarEntry>()
    private var notasList: ArrayList<Cuestionario> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas)

        val bundle = intent.extras
        var idAsignatura = bundle?.getString("idAsignatura")
        var tipo = bundle?.getString("tipo")
        var dni = bundle?.getString("dni")
        var nombre = bundle?.getString("nombre")

        if ((ContextCompat.checkSelfPermission(
                this,
                READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED) ||
            (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    READ_EXTERNAL_STORAGE,
                    WRITE_EXTERNAL_STORAGE
                ),
                123
            )
        }

        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)


        if (idAsignatura != null) {
            database.child("Asignaturas-Cuestionarios").child(idAsignatura)
                .addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (postSnapshot in snapshot.children) {
                                var id = ""
                                if (postSnapshot.exists()) {
                                    id = postSnapshot.value.toString()
                                    aCuestionarios.add(id)

                                }

                                database.child("Cuestionarios").child(id)
                                    .addValueEventListener(object :
                                        ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (snapshot.exists()) {
                                                var titulo =
                                                    snapshot.child("Titulo").value.toString()
                                                aTitulo.add(titulo)

                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                        }

                                    })

                            }

                        }

                    }


                    override fun onCancelled(error: DatabaseError) {

                    }

                })
        }

        if (idAsignatura != null) {
            database.child("Asignaturas-Alumnos").child(idAsignatura)
                .addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (postSnapshot in snapshot.children) {
                                var dni = ""
                                if (postSnapshot.exists()) {
                                    dni = postSnapshot.child("dni").value.toString()
                                    adni.add(dni)

                                }

                                database.child("Alumnos").child(dni)
                                    .addValueEventListener(object :
                                        ValueEventListener {
                                        override fun onDataChange(ssnapshot: DataSnapshot) {
                                            if (ssnapshot.exists()) {
                                                var nombre =
                                                    ssnapshot.child("nombre").value.toString()
                                                anombre.add(nombre)
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                        }

                                    })

                            }

                        }

                    }


                    override fun onCancelled(error: DatabaseError) {

                    }

                })
        }




        database.child("Alumnos-Cuestionarios")
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        for (postSnapshot in snapshot.children) {
                            var dni = ""
                            if (postSnapshot.exists()) {
                                dni = postSnapshot.key.toString()
                            }
                            database.child("Alumnos-Cuestionarios").child(dni)
                                .addListenerForSingleValueEvent(
                                    object : ValueEventListener {
                                        override fun onDataChange(esnapshot: DataSnapshot) {
                                            if (esnapshot.exists()) {

                                                var idA = ""
                                                for (apostSnapshot in esnapshot.children) {
                                                    if (apostSnapshot.exists()) {
                                                        if (apostSnapshot.child("nota")
                                                                .exists()
                                                        ) {

                                                            var nota =
                                                                apostSnapshot.child("nota").value
                                                            idA = apostSnapshot.key.toString()
                                                            var quiz = Cuestionario(
                                                                nota.toString().toFloat(),
                                                                idA,
                                                                dni, ""
                                                            )
                                                            notas.add(quiz)
                                                            notasList.add(quiz)

                                                        }
                                                    }
                                                }
                                            }


                                            database.removeEventListener(this);
                                        }

                                        override fun onCancelled(error: DatabaseError) {

                                        }

                                    })


                        }

                    }


                }


                override fun onCancelled(error: DatabaseError) {

                }

            })




        if (idAsignatura != null) {
            barchart(idAsignatura)
        }
        setup(idAsignatura ?: "", tipo ?: "", dni ?: "", email ?: "", nombre ?: "")



    }

    private fun barchart(idAsignatura: String) {

        var idcuestionarios = arrayListOf<String>()
        var cuestionarios = arrayListOf<Cuestionario>()
        database.child("Asignaturas-Cuestionarios").child(idAsignatura).get().addOnSuccessListener {
            for(snapshot in it.children){
                if (snapshot.exists()) {

                        if(snapshot.exists()){
                            idcuestionarios.add(snapshot.value.toString())
                        }

                }
            }
        }

        database.child("Alumnos-Cuestionarios").get().addOnSuccessListener {
            for(snapshot in it.children){
                if (snapshot.exists()) {
                    for (postsnapshot in snapshot.children) {
                        if (postsnapshot.child("nota").exists()) {
                            var nota = postsnapshot.child("nota").value
                            var idA = postsnapshot.key.toString()
                            var dni = snapshot.key.toString()
                            var titulo = postsnapshot.child("titulo").value

                            for (j in idcuestionarios){
                                if (j == idA){
                                    if (nota != null) {

                                        var quiz = Cuestionario(
                                            nota.toString().toFloat(),
                                            idA,
                                            dni,
                                            titulo.toString()
                                        )
                                        cuestionarios.add(quiz)

                                    }
                                }
                            }


                        }

                    }
                }
            }






        }
        database.child("Alumnos-Cuestionarios").get().addOnSuccessListener {
            val entries = ArrayList<BarEntry>()
            val labels = ArrayList<String>()
            val entriesP = ArrayList<BarEntry>()


            var c = ArrayList<String>()
            for (f in idcuestionarios) {
                for(j in cuestionarios){
                    if (j.id == f)
                         c.add(j.titulo)
                }
            }

            val cuestionariosactivados = ArrayList<String>()

            for (f in c){
                if (!(labels.contains(f))){
                    labels.add(f)
                    cuestionariosactivados.add(f)
                }
            }

            var i=0
            var contador = 0
            var media = 0.0f
            var presentacion = 0.0f

            for (f in cuestionariosactivados) {
                for(j in cuestionarios){
                    if (f == j.titulo) {
                        media = media + j.nota
                        contador++
                    }
                }
                media = media/contador
                entries.add(BarEntry(media, i))
                presentacion = (100.0f /(adni.size/contador))
                entriesP.add(BarEntry(presentacion, i))
                i++
                contador = 0
                media = 0.0f
            }

            medias = entries
            participaciones = entriesP
            labelsG = labels
            val barDataSet = BarDataSet(entries, "Cells")


            val data = BarData(labels, barDataSet)
            barChart.axisRight.setAxisMaxValue(10f)
            barChart.axisLeft.setAxisMaxValue(10f)
            barChart.data = data // set the data and list of lables into chart
            //barChart.setVisibleYRangeMaximum(10.0f,)

            barChart.setDescription("Notas Cuestionarios")  // set the description
            barChart.axisRight.setStartAtZero(true)
            barChart.axisLeft.setStartAtZero(true)



            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
            //barDataSet.color = resources.getColor(R.color.browser_actions_bg_grey)

            barChart.legend.calculatedLabelSizes

            barChart.animateY(2000)




        }

    }


    fun Guardar(texto: String, idAsignatura: String){//,ficheroFisico : File){
        try {
            val rutaSd = baseContext.getExternalFilesDir(null)?.absolutePath
            val miCarpeta = File(rutaSd, "Notas")
            if (!miCarpeta.exists()) {
                miCarpeta.mkdir()
            }
            val ficheroFisico = File(miCarpeta, "$idAsignatura.csv")
            ficheroFisico.appendText("$texto")
            //ficheroFisico.delete()
        }catch (e: Exception){
            Toast.makeText(
                this,
                "No se ha podido Guardar",
                Toast.LENGTH_LONG
            ).show()

        }

    }

    fun Cargar(idAsignatura: String): String{
        var texto = ""
        try {

            val rutaSd = baseContext.getExternalFilesDir(null)?.absolutePath
            val miCarpeta = File(rutaSd, "Notas")
            val ficheroFisico = File(miCarpeta, "$idAsignatura.csv")
            val fichero = BufferedReader(InputStreamReader(FileInputStream(ficheroFisico)))
            texto = fichero.use(BufferedReader::readText)
            Toast.makeText(
                this,
                rutaSd,
                Toast.LENGTH_LONG
            ).show()
        }catch (e: Exception){
            //textViewArchivo.text = "No existe fichero de notas de esta asignatura, pinche el boton de exportar para crearlo"
        }
        return texto
    }

    fun Eliminar(idAsignatura: String){

        try {

            val rutaSd = baseContext.getExternalFilesDir(null)?.absolutePath
            val miCarpeta = File(rutaSd, "Notas")
            val ficheroFisico = File(miCarpeta, "$idAsignatura.csv")
            ficheroFisico.delete()
        }catch (e: Exception){

        }
    }





    private fun setup(
        idAsignatura: String,
        tipo: String,
        dni: String,
        email: String,
        nombre: String
    ) {

        if (tipo == "asignatura") {
            if (idAsignatura != "") {

                buttonGrafica.setOnClickListener {
                    if (buttonGrafica.text == "Grafica Participacion"){
                        val barDataSet = BarDataSet(participaciones, "Cells")
                        barChart.axisRight.setAxisMaxValue(100f)
                        barChart.axisLeft.setAxisMaxValue(100f)

                        val data = BarData(labelsG, barDataSet)
                        barChart.data = data // set the data and list of lables into chart
                        //barChart.setVisibleYRangeMaximum(10.0f,)

                        barChart.setDescription("Participacion Cuestionarios")  // set the description


                        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
                        //barDataSet.color = resources.getColor(R.color.browser_actions_bg_grey)

                        barChart.legend.calculatedLabelSizes

                        barChart.animateY(2000)

                        buttonGrafica.text = "Grafica Medias"




                    }else if (buttonGrafica.text == "Grafica Medias"){
                        val barDataSet = BarDataSet(medias, "Cells")

                        barChart.axisRight.setAxisMaxValue(10f)
                        barChart.axisLeft.setAxisMaxValue(10f)
                        val data = BarData(labelsG, barDataSet)
                        barChart.data = data // set the data and list of lables into chart
                        //barChart.setVisibleYRangeMaximum(10.0f,)

                        barChart.setDescription("Notas Cuestionarios")  // set the description

                        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
                        //barDataSet.color = resources.getColor(R.color.browser_actions_bg_grey)


                        barChart.legend.calculatedLabelSizes

                        barChart.animateY(2000)

                        buttonGrafica.text = "Grafica Participacion"
                    }
                }


                buttonInsertar.setOnClickListener {

                    var media = 0.0f
                    var tamaño = 0.0f

                    var i = 0
                    Eliminar(idAsignatura)
                    Guardar("DNI;Nombre;", idAsignatura)
                    for (j in aTitulo) {
                        Guardar("$j;", idAsignatura)

                    }
                    Guardar("Nota Media\n", idAsignatura)
                    while (i < adni.size) {
                        var dni = adni[i]
                        Guardar(dni + ";" + anombre[i] + ",", idAsignatura)
                        for (id in aCuestionarios) {
                            control = false
                            for (cobjet in notas) {
                                if (id == cobjet.id && dni == cobjet.dni) {
                                    Guardar(cobjet.nota.toString() + ";", idAsignatura)
                                    media = cobjet.nota
                                    tamaño++
                                    control = true
                                }
                            }
                            if (control == false) {
                                Guardar(";", idAsignatura)
                            }

                        }

                        media = media / aCuestionarios.size
                        Guardar(media.toString() + "\n", idAsignatura)

                        i++
                    }


                    //Todo: ***********************************************************************
                    try {
                        val emails = arrayOf(email)
                        val rutaSd = baseContext.getExternalFilesDir(null)?.absolutePath
                        val miCarpeta = File(rutaSd, "Notas")
                        val ficheroFisico = File(miCarpeta, "$idAsignatura.csv")
                        val uri = FileProvider.getUriForFile(this
                            ,
                            this.getApplicationContext().getPackageName() + ".provider",
                            ficheroFisico
                        )
                        val intent = Intent(Intent.ACTION_SEND).apply {

                            type = "*/*"
                            putExtra(Intent.EXTRA_EMAIL, emails)
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "En este correo se encuentran todas las notas de la asignatura $nombre.\n\nEste correo ha sido " +
                                        "enviado desde la aplicacion QUIZU"
                            )
                            putExtra(
                                Intent.EXTRA_SUBJECT,
                                "QUIZU: Exportación de notas de $nombre"
                            )
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)




                            putExtra(Intent.EXTRA_STREAM, uri)

                        }

                            startActivity(intent)

                    }catch (e: Exception){
                        Toast.makeText(
                            this,
                            "No se ha podido enviar el archivo",
                            Toast.LENGTH_LONG
                        ).show()
                        showAlert(e.toString())
                    }

                }
            }

        }





    }

    fun showAlert(mensaje: String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    private fun controlNotas(idAsignatura: String) {

    }
}

