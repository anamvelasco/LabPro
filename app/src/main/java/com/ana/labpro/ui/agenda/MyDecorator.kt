package com.ana.labpro.ui.agenda

import android.content.Context
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.ana.labpro.R

class MyDecorator(private val context: Context, private val calendarDay: CalendarDay) : DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day == calendarDay
    }

    override fun decorate(view: DayViewFacade?) {
        // Color de fondo resaltado (por ejemplo, rojo)
        val highlightColor = ContextCompat.getColor(context, android.R.color.holo_red_light)

        // Setear el color como fondo resaltado
        view?.setBackgroundDrawable(ColorDrawable(highlightColor))
    }
}
