package ideas.transportapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ideas.transportapp.model.Bookings
import ideas.transportapp.service.TransportService
import java.util.*

class CalenderViewModel(private val transportService: TransportService) : ViewModel() {
    val bookings = MutableLiveData<Bookings>()
    fun loadBookings(calender: Calendar){
            transportService.getBookingsForMonth(calender, bookings)
    }
}