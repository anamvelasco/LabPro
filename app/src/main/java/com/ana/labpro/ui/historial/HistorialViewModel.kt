import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ana.labpro.data.ReservasRepository
import com.ana.labpro.model.Reservas
import com.ana.labpro.data.ResourceRemote
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.launch

class HistorialViewModel : ViewModel() {

    private val reservasRepository = ReservasRepository()
    private var reservasListLocal = mutableListOf<Reservas?>()

    private val _errorMsg : MutableLiveData<String?> = MutableLiveData()
    val errorMsg: LiveData<String?> = _errorMsg

    private val _reservaErased : MutableLiveData<Boolean> = MutableLiveData()
    val reservaErased : LiveData<Boolean> = _reservaErased

    private val _reservasList: MutableLiveData<MutableList<Reservas?>> = MutableLiveData()
    val reservasList: LiveData<MutableList<Reservas?>> = _reservasList
    fun loadReservas() {
        reservasListLocal.clear()
        viewModelScope.launch {
            val result = reservasRepository.loadReservas()
            result.let { resourceRemote ->
                when(resourceRemote){
                    is ResourceRemote.Success ->{
                        result.data?.documents?.forEach{document ->
                            val reserva = document.toObject<Reservas>()
                            reservasListLocal.add(reserva)
                        }
                        _reservasList.postValue(reservasListLocal)
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

    fun deleteReserva(reserva: Reservas?) {
        viewModelScope.launch {
            val result = reservasRepository.deleteReserva(reserva)
            result.let { resourceRemote ->
                when(resourceRemote){
                    is ResourceRemote.Success ->{
                        _reservaErased.postValue(true)
                        _errorMsg.postValue("Reserva eliminada con exito")
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

}

/*private val reservasRepository = ReservasRepository()

private val _errorMsg: MutableLiveData<String?> = MutableLiveData()
val errorMsg: LiveData<String?> = _errorMsg

private val _reserva: MutableLiveData<Reservas?> = MutableLiveData()
val reserva: LiveData<Reservas?> = _reserva

suspend fun loadReservas(userEmail: String) {
    try {
        val result = reservasRepository.getReservasByUserEmail(userEmail)
        when (result) {
            is ResourceRemote.Success -> {

                val reserva = result.data?.firstOrNull()
                Log.d("HistorialViewModel", "Paso 3: Reserva obtenida: $reserva")
                if (reserva != null) {
                    _reserva.postValue(reserva)
                } else {
                    _errorMsg.postValue("No se encontró ninguna reserva para el usuario: $userEmail")
                }
            }
            is ResourceRemote.Error -> {
                _errorMsg.postValue(result.message)
                Log.e("HistorialViewModel", "Error al cargar reservas: ${result.message}")
            }
            else -> {
                Log.d("HistorialViewModel", "Paso 3: Resultado inesperado al cargar reservas")
            }
        }
    } catch (e: Exception) {

        _errorMsg.postValue("Error al cargar reservas: ${e.message}")
        Log.e("HistorialViewModel", "Error al cargar reservas: ${e.message}")
    }
}*/