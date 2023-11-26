package com.ana.labpro.ui.reservas

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ana.labpro.R
import com.ana.labpro.databinding.FragmentReservasBinding
import kotlinx.coroutines.launch  // Agrega esta importación
import androidx.lifecycle.lifecycleScope

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ReservasFragment : Fragment() {

    private lateinit var reservasViewModel: ReservasViewModel
    private lateinit var reservasBinding: FragmentReservasBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        reservasBinding = FragmentReservasBinding.inflate(inflater, container, false)
        reservasViewModel = ViewModelProvider(this)[ReservasViewModel::class.java]
        val view = reservasBinding.root

        reservasViewModel.errorMsg.observe(viewLifecycleOwner) { msg ->
            showErrorMsg(msg)
        }

        reservasViewModel.createReservaSuccess.observe(viewLifecycleOwner) {
            if (it) {
                lifecycleScope.launch {
                    // Incrementar el número de reservas al realizar una reserva
                    reservasViewModel.loadCurrentUser()
                }
            }
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val programaSpinner = reservasBinding.programaSpinner
        val programaAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.programa_options,
            android.R.layout.simple_spinner_item
        )
        programaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        programaSpinner.adapter = programaAdapter

        val maquinaSpinner = reservasBinding.maquinaSpinner
        val maquinaAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.maquina_options,
            android.R.layout.simple_spinner_item
        )
        maquinaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        maquinaSpinner.adapter = maquinaAdapter

        val dateTextView = reservasBinding.dateTextView
        val timeTextView = reservasBinding.timeTextView

        dateTextView.setOnClickListener {
            showDatePickerDialog()
        }

        timeTextView.setOnClickListener {
            showTimePickerDialog()
        }

        reservasBinding.reservasButton.setOnClickListener {
            with(reservasBinding) {
                val name = nameEditText.text.toString()
                val cedulaText = identiEditText.text.toString()
                val cedula = try {
                    if (cedulaText.isNotEmpty()) cedulaText.toInt() else 0
                } catch (e: NumberFormatException) {
                    0
                }
                val email = email2EditText.text.toString()
                val programa = programaSpinner.selectedItem.toString()
                val maquina = maquinaSpinner.selectedItem.toString()
                val dateText = dateTextView.text.toString()
                val hour = timeTextView.text.toString()

                // Intenta convertir la fecha a Date
                val date = try {
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateText)
                } catch (e: ParseException) {
                    null // Maneja el error de conversión de fecha
                }

                if (date != null) {
                    val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)

                    // Llama a validateFields desde un ámbito de coroutine
                    lifecycleScope.launch {
                        reservasViewModel.validateFields(name, cedula, email, programa, maquina, dateString, hour)
                    }
                } else {
                    showErrorMsg("Formato de fecha incorrecto")
                }
            }
        }

        return view
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = "$year-${monthOfYear + 1}-$dayOfMonth"
                if (isWeekday(year, monthOfYear, dayOfMonth)) {
                    reservasBinding.dateTextView.text = selectedDate
                } else {
                    // Muestra un mensaje indicando que el horario es de lunes a viernes
                    Toast.makeText(requireContext(), "El horario es de lunes a viernes", Toast.LENGTH_SHORT).show()
                }
            },
            currentYear,
            currentMonth,
            currentDay
        )

        datePickerDialog.show()
    }

    private fun isWeekday(year: Int, month: Int, day: Int): Boolean {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return dayOfWeek >= Calendar.MONDAY && dayOfWeek <= Calendar.FRIDAY
    }


    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                if (hourOfDay in 8..17) {
                    if (minute == 0) {
                        val selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
                        reservasBinding.timeTextView.text = selectedTime
                    } else {
                        // Muestra un mensaje indicando que las horas deben ser en punto
                        Toast.makeText(requireContext(), "Las horas deben ser en punto", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Muestra un mensaje indicando el rango permitido de horas
                    Toast.makeText(requireContext(), "Selecciona una hora entre 8:00 y 17:00", Toast.LENGTH_SHORT).show()
                }
            },
            currentHour,
            currentMinute,
            true
        )

        timePickerDialog.show()
    }




    private fun showErrorMsg(msg: String?) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_LONG).show()
    }

}
