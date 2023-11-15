package com.ana.labpro.data

import android.util.Log
import com.ana.labpro.model.Reservas
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class ReservasRepository {

    private var db = Firebase.firestore
    suspend fun createReserva(reserva: Reservas) : ResourceRemote<String?>{
        return try {
            val document = db.collection("reservas").document()
            reserva.id = document.id
            db.collection("reservas").document(document.id).set(reserva).await()
            ResourceRemote.Success(document.id)

        } catch (e: FirebaseFirestoreException){
            Log.e("FirebaseFirestoreError", e.localizedMessage)
            ResourceRemote.Error(message = e.localizedMessage)
        } catch (e: FirebaseNetworkException){
            Log.e("FirebaseNetworkException", e.localizedMessage)
            ResourceRemote.Error(message = e.localizedMessage)
        } catch (e: FirebaseException){
            Log.e("FirebaseException", e.localizedMessage)
            ResourceRemote.Error(message = e.localizedMessage)
        }

    }


}