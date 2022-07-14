package com.example.cally

import java.time.DayOfWeek
import java.time.LocalDate

class TodoUtil {
    var selectedDate: LocalDate? = LocalDate.now()

    fun daysInWeekArray(selectedDate: LocalDate): ArrayList<LocalDate?>? {
        val days: ArrayList<LocalDate?> = ArrayList()
        var current: LocalDate? = sundayForDate(selectedDate)
        val endDate = current!!.plusWeeks(1)
        while (current!!.isBefore(endDate)) {
            days.add(current)
            current = current.plusDays(1)
        }
        return days
    }

    private fun sundayForDate(selectedDate: LocalDate): LocalDate? {
        var current = selectedDate
        val oneWeekAgo = current.minusWeeks(1)
        while (current.isAfter(oneWeekAgo)) {
            if (current.dayOfWeek == DayOfWeek.SUNDAY) return current
            current = current.minusDays(1)
        }
        return null
    }

}