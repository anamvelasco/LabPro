package com.ana.labpro.ui.homee
import com.ana.labpro.data.ResourceRemote
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ana.labpro.data.GaleriaRepository
import com.ana.labpro.model.Galeria
import com.squareup.picasso.Picasso

import com.ana.labpro.model.Reservas


class HomeeViewModel : ViewModel() {
    private val galeriaRepository = GaleriaRepository()
    private var galeriaListLocal = mutableListOf<Galeria?>()

    private val _errorMsg: MutableLiveData<String?> = MutableLiveData()
    val errorMsg: LiveData<String?> = _errorMsg

    private val _pictureErased: MutableLiveData<Boolean> = MutableLiveData()
    val pictureErased: LiveData<Boolean> = _pictureErased

    private val _galleryList: MutableLiveData<MutableList<Galeria?>> = MutableLiveData()
    val galleryList: MutableLiveData<MutableList<Galeria?>> = _galleryList

    fun loadGalery() {
        galeriaListLocal.clear()
        viewModelScope.launch {
            val result = galeriaRepository.loadGalery()
            result.let { resourceRemote ->
                when (resourceRemote) {
                    is ResourceRemote.Success -> {
                        result.data?.documents?.forEach { document ->
                            val galeria = document.toObject<Galeria>()
                            galeriaListLocal.add(galeria)
                        }
                        _galleryList.postValue(galeriaListLocal)
                    }
                    is ResourceRemote.Error -> {
                        val msg = result.message
                        _errorMsg.postValue(msg)
                    }
                    else -> {
                        //don't use
                    }
                }
            }
        }
    }

    fun deleteImagenConValidacionDeRol(galeria: Galeria?) {
        viewModelScope.launch {
            val result = galeriaRepository.deleteImagenConValidacionDeRol(galeria)
            result.let { resourceRemote ->
                when (resourceRemote) {
                    is ResourceRemote.Success -> {
                        _pictureErased.postValue(true)
                        _errorMsg.postValue("Foto eliminada con éxito")
                    }
                    is ResourceRemote.Error -> {
                        val msg = result.message
                        _errorMsg.postValue(msg)
                    }
                    else -> {
                        // no se utiliza
                    }
                }
            }
        }
    }

    suspend fun createGaleria(name: String, urlPicture: String): Boolean {
        val currentUserRole = getCurrentUserRole()
        return if (currentUserRole == "admin") {
            val result = galeriaRepository.createGaleria(name, urlPicture)
            handleCreateGaleriaResult(result)
            true // Indica que la operación fue exitosa
        } else {
            _errorMsg.postValue("No tienes permisos para crear una galería.")
            false // Indica que la operación no fue exitosa
        }
    }

    suspend fun getCurrentUserRole(): String? {
        return galeriaRepository.obtenerRolDelUsuarioActual()
    }

    private fun handleCreateGaleriaResult(result: ResourceRemote<String?>) {
        when (result) {
            is ResourceRemote.Success -> {
                // Manejar el caso de éxito
            }
            is ResourceRemote.Error -> {
                val errorMsg = result.message
                _errorMsg.postValue(errorMsg)
            }

            else -> {
                //Nothing
            }
        }
    }




}
