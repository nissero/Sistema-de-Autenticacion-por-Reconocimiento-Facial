package com.biogin.myapplication

import android.util.Log
import com.biogin.myapplication.data.Institute
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.tasks.await

class FirebaseMethods {

    //todo esto hay que modificarlo cuando se cambie la base de datos
    //y se agreguen los modulos de acceso para cada rol.
    fun readData(dni: String, callback: (Usuario) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val documentReference = db.collection("usuarios").document(dni)

        documentReference.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userData = documentSnapshot.data
                    val nombre = userData?.get("nombre") as? String ?: ""
                    val apellido = userData?.get("apellido") as? String ?: ""
                    val email = userData?.get("email") as? String ?: ""
                    val area = userData?.get("area") as? String ?: ""
                    val categoria = userData?.get("categoria") as? String ?: ""
                    val areasPermitidas = userData?.get("areasPermitidas") as? ArrayList<String> ?: arrayListOf()

                    val usuario = Usuario(nombre, apellido, dni, email, area, categoria,  areasPermitidas)
                    callback(usuario)
                } else {
                    // Usuario no encontrado, devolver objeto Usuario vacío
                    callback(Usuario())
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al obtener los datos del usuario con DNI $dni", e)
                // En caso de error, devolver objeto Usuario vacío
                callback(Usuario())
            }
    }

    fun readInstitutes(instituteName: String, callback: (Institute) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val documentReference = db.collection("institutos").document(instituteName)

        documentReference.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userData = documentSnapshot.data
                    val areas = userData?.get("areas") as? ArrayList<String> ?: arrayListOf()

                    val institute = Institute(instituteName, areas)
                    callback(institute)
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al obtener los datos del instituto con nombre $instituteName", e)
            }
    }
}