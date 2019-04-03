package ideas.transportapp.service

import androidx.lifecycle.MutableLiveData
import ideas.transportapp.model.Bookings
import ideas.transportapp.repository.BookingsRepository
import java.util.*
import java.util.concurrent.Executors

interface TransportService {
    fun getBookingsForMonth(
        calender: Calendar,
        bookings: MutableLiveData<Bookings>
    )
}

class TransportServiceImpl(private val bookingsRepository: BookingsRepository): TransportService{
    override fun getBookingsForMonth(
        calender: Calendar,
        bookings: MutableLiveData<Bookings>) {
        Executors.newSingleThreadExecutor().execute {
            bookings.postValue(bookingsRepository.getBookingsForMonth(calender))
        }
    }
}