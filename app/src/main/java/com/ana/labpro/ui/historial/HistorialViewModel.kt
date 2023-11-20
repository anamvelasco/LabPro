// HistorialViewModel.kt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ana.labpro.data.ReservasRepository
import com.ana.labpro.model.Reservas
import com.ana.labpro.data.ResourceRemote

class HistorialViewModel : ViewModel() {

    private val reservasRepository = ReservasRepository()

    private val _errorMsg: MutableLiveData<String?> = MutableLiveData()
    val errorMsg: LiveData<String?> = _errorMsg

    private val _reserva: MutableLiveData<Reservas?> = MutableLiveData()
    val reserva: LiveData<Reservas?> = _reserva // Cambiado a LiveData para evitar modificaciones externas

    suspend fun loadReservas(userEmail: String) {
        try {
            val result = reservasRepository.getReservasByUserEmail(userEmail)
            when (result) {
                is ResourceRemote.Success -> {
                    // Trabaja directamente con el valor obtenido
                    val reserva = result.data?.firstOrNull()
                    if (reserva != null) {
                        _reserva.postValue(reserva)
                    } else {
                        _errorMsg.postValue("No se encontró ninguna reserva para el usuario: $userEmail")
                    }
                }
                is ResourceRemote.Error -> _errorMsg.postValue(result.message)
                else -> { /* Maneja otros casos si es necesario */ }
            }
        } catch (e: Exception) {
            // Manejar la excepción si es necesario
            _errorMsg.postValue("Error al cargar reservas: ${e.message}")
        }
    }
}
