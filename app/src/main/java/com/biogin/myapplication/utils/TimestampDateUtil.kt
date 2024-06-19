package com.biogin.myapplication.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale


class TimestampDateUtil {

    fun utcTimestampToLocalString(timestamp : Any) : String{
        val threeHoursInMiliseconds = 10800000
        return if (timestamp is Timestamp) {
            val sfd = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
            sfd.format(timestamp.seconds*1000 - threeHoursInMiliseconds)
        } else {
            ""
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun stringDateToLocalDate(date : String) : LocalDate {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return LocalDate.parse(date, formatter)
    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    private fun getCurrentDateTime(): Date {
        return android.icu.util.Calendar.getInstance().time
    }

    fun getStartOfDay() : Calendar {
        val startOfDay: Calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return startOfDay
    }

    fun getEndOfDay() : Calendar {
        val endOfDay: Calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        return endOfDay
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun nowAsStringFileFormat() : String {
        val date = LocalDateTime.now()
        return "${date.dayOfMonth}-${date.monthValue}_${date.year}_${date.hour}-${date.minute}-${date.second}"
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun asDate(localDateTime: LocalDateTime): Date {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
    }

}