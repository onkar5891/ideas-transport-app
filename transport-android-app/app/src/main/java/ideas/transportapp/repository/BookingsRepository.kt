package ideas.transportapp.repository

import android.app.Application
import com.google.gson.Gson
import ideas.transportapp.model.Bookings
import java.util.*

class BookingsRepository(private val application: Application){

    fun getBookingsForMonth(calender: Calendar): Bookings? {
        val requestedMonth = calender.get(Calendar.MONTH)
        if(requestedMonth == Calendar.APRIL){ // April
                application.assets.open("user1_april_bookings.json").bufferedReader().use {
                    val json = it.readText()
                    return Gson().fromJson(json, Bookings::class.java)
                }
            }
        return Bookings("1", null)
    }

}