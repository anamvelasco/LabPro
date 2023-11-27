package com.ana.labpro.data
import android.util.Log
import com.ana.labpro.model.Galeria
import com.ana.labpro.model.Reservas
import com.ana.labpro.model.User
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class GaleriaRepository {

    private var db = Firebase.firestore
    private var auth: FirebaseAuth = Firebase.auth

    suspend fun createGaleria(name: String, urlPicture: String): ResourceRemote<String?> {
        return try {
            val currentUserDocument = db.collection("users").document(auth.uid ?: "").get().await()
            val currentUser = currentUserDocument.toObject(User::class.java)

            if (currentUser != null) {
                if (currentUser.role == "admin") {
                    // El usuario tiene permisos para crear la galería
                    val galeria = Galeria(name = name, urlPicture = urlPicture)
                    galeria.ownerUid = currentUser.uid
                    galeria.ownerRole = currentUser.role

                    val document = db.collection("gallery").document()
                    galeria.id = document.id
                    db.collection("gallery").document(document.id).set(galeria).await()

                    ResourceRemote.Success(data = document.id)
                } else {
                    // El usuario no tiene permisos
                    ResourceRemote.Error(message = "No tienes permisos para crear una galería.")
                }
            } else {
                // No se pudo obtener el usuario actual
                ResourceRemote.Error(message = "No se pudo obtener la información del usuario actual.")
            }
        } catch (e: FirebaseFirestoreException) {
            Log.e("FirebaseFirestoreError", e.localizedMessage)
            ResourceRemote.Error(message = e.localizedMessage)
        } catch (e: FirebaseNetworkException) {
            Log.e("FirebaseNetworkException", e.localizedMessage)
            ResourceRemote.Error(message = e.localizedMessage)
        } catch (e: FirebaseException) {
            Log.e("FirebaseException", e.localizedMessage)
            ResourceRemote.Error(message = e.localizedMessage)
        }
    }



    /*private suspend fun obtenerRolDelUsuarioActual(): String? {
        val currentUserEmail = auth.currentUser?.email
        if (currentUserEmail != null) {
            val querySnapshot = db.collection("users").whereEqualTo("email", currentUserEmail).get().await()
            if (!querySnapshot.isEmpty) {
                val userDocument = querySnapshot.documents[0]
                return userDocument.getString("role") ?: ""
            }
        }
        return ""
    }*/



    suspend fun deleteImagenConValidacionDeRol(galeria: Galeria?): ResourceRemote<String?> {
        return try {
            // Verifica los permisos utilizando ownerUid y ownerRole en lugar de uid y role
            val currentUserDocument = auth.uid?.let { db.collection("users").document(it).get().await() }

            if (currentUserDocument != null && currentUserDocument.exists()) {
                val currentUser = currentUserDocument.toObject(User::class.java)

                if (currentUser?.role == "admin" || currentUser?.uid == galeria?.ownerUid) {
                    val result = galeria?.id?.let { db.collection("gallery").document(it).delete().await() }
                    ResourceRemote.Success(data = galeria?.id)
                } else {
                    ResourceRemote.Error(message = "No tienes permisos para eliminar esta foto.")
                }
            } else {
                ResourceRemote.Error(message = "No se pudo obtener la información del usuario actual.")
            }
        } catch (e: FirebaseFirestoreException) {
            Log.e("FirebaseFirestoreError", e.localizedMessage)
            ResourceRemote.Error(message = e.localizedMessage)
        } catch (e: FirebaseNetworkException) {
            Log.e("FirebaseNetworkException", e.localizedMessage)
            ResourceRemote.Error(message = e.localizedMessage)
        } catch (e: FirebaseException) {
            Log.e("FirebaseException", e.localizedMessage)
            ResourceRemote.Error(message = e.localizedMessage)
        }
    }

    suspend fun loadGalery(): ResourceRemote<QuerySnapshot?> {
        return try {
            val result = db.collection("gallery").get().await()
            ResourceRemote.Success(data = result)
        } catch (e: FirebaseFirestoreException) {
            Log.e("FirebaseFirestoreError", e.localizedMessage)
            ResourceRemote.Error(message = e.localizedMessage)
        } catch (e: FirebaseNetworkException) {
            Log.e("FirebaseNetworkException", e.localizedMessage)
            ResourceRemote.Error(message = e.localizedMessage)
        } catch (e: FirebaseException) {
            Log.e("FirebaseException", e.localizedMessage)
            ResourceRemote.Error(message = e.localizedMessage)
        }

    }

    suspend fun obtenerRolDelUsuarioActual(): String? {
        val currentUserEmail = auth.currentUser?.email
        if (currentUserEmail != null) {
            val querySnapshot = db.collection("users").whereEqualTo("email", currentUserEmail).get().await()
            if (!querySnapshot.isEmpty) {
                val userDocument = querySnapshot.documents[0]
                return userDocument.getString("role")
            }
        }
        return null
    }


    /*
    suspend fun deleteImagenConValidacionDeRol(galeria: Galeria?): ResourceRemote<String?> {
        return try {
            // Obtener el documento del usuario actual
            val currentUserDocument = auth.uid?.let { db.collection("users").document(it).get().await() }

            // Verificar si se pudo obtener el documento del usuario actual
            if (currentUserDocument != null && currentUserDocument.exists()) {
                val currentUser = currentUserDocument.toObject(User::class.java)

                // Verificar si el usuario actual es un administrador o el propietario de la galería
                if (currentUser?.role == "admin" || currentUser?.uid == galeria?.uid) {
                    // El usuario tiene permisos para eliminar la galería
                    val result = galeria?.id?.let { db.collection("gallery").document(it).delete().await() }
                    ResourceRemote.Success(data = galeria?.id)
                } else {
                    // El usuario no tiene permisos
                    ResourceRemote.Error(message = "No tienes permisos para eliminar esta foto.")
                }
            } else {
                // No se pudo obtener el usuario actual
                ResourceRemote.Error(message = "No se pudo obtener la información del usuario actual.")
            }
        } catch (e: FirebaseFirestoreException) {
            Log.e("FirebaseFirestoreError", e.localizedMessage)
            ResourceRemote.Error(message = e.localizedMessage)
        } catch (e: FirebaseNetworkException) {
            Log.e("FirebaseNetworkException", e.localizedMessage)
            ResourceRemote.Error(message = e.localizedMessage)
        } catch (e: FirebaseException) {
            Log.e("FirebaseException", e.localizedMessage)
            ResourceRemote.Error(message = e.localizedMessage)
        }
    }
*/


}