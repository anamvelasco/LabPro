package com.ana.labpro.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ana.labpro.model.Reservas
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class ReservasRepository {

    private var db = Firebase.firestore
    private var auth: FirebaseAuth = Firebase.auth

    suspend fun createReserva(reserva: Reservas): ResourceRemote<String?> {
        return try {
            val document = db.collection("reservas").document()
            reserva.id = document.id
            reserva.uid = auth.uid
            db.collection("reservas").document(document.id).set(reserva).await()
            ResourceRemote.Success(document.id)

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

    suspend fun getReservasByUserEmail(email: String): ResourceRemote<List<Reservas>> {
        return try {
            val querySnapshot =
                db.collection("reservas").whereEqualTo("email", email).get().await()

            val reservasList = querySnapshot.toObjects(Reservas::class.java)

            ResourceRemote.Success(data = reservasList)
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





    suspend fun loadReservas(): ResourceRemote<QuerySnapshot?> {
        return try {
            val result = db.collection("reservas").whereEqualTo("uid",auth.uid).get().await()
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

    suspend fun loadReservasbyDate(fecha:String) : ResourceRemote<QuerySnapshot?> {
        return try {
            val result = db.collection("reservas").whereEqualTo("date",fecha).get().await()
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

    suspend fun deleteReserva(reserva: Reservas?): ResourceRemote<String?> {
        return try {
            val result = reserva?.id?.let { db.collection("reservas").document(it).delete().await() }
            ResourceRemote.Success(data = reserva?.id)
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

}
