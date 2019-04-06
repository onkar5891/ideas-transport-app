package ideas.transportapp

import android.graphics.Color
import android.os.Bundle
import android.text.style.QuoteSpan
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.prolificinteractive.materialcalendarview.*
import com.prolificinteractive.materialcalendarview.format.DayFormatter
import ideas.transportapp.application.TransportApplication
import ideas.transportapp.model.Bookings
import ideas.transportapp.repository.BookingsRepository
import ideas.transportapp.service.TransportServiceImpl
import ideas.transportapp.viewmodel.CalenderViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), OnMonthChangedListener {
    @Inject
    lateinit var bookingsRepository: BookingsRepository

    private lateinit var viewModel: CalenderViewModel
    private var dayFormatter = MyDayFormatter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        TransportApplication.applicationComponent.inject(this)
        calendarView.selectionMode = MaterialCalendarView.SELECTION_MODE_SINGLE
        calendarView.addDecorator(EventDecorator())
        calendarView.setDayFormatter(dayFormatter)
        calendarView.setOnMonthChangedListener(this)
        initViewModel()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CalenderViewModel(TransportServiceImpl(bookingsRepository)) as T
            }
        }).get(CalenderViewModel::class.java)

        viewModel.bookings.observe(this, androidx.lifecycle.Observer { newBookings ->
            newBookings?.let {
                dayFormatter.bookings = it
                calendarView.invalidateDecorators()
            }
            Log.d(MainActivity::class.java.name, "Bookings : $newBookings")
        })

        viewModel.loadBookings(Calendar.getInstance())
    }

    override fun onMonthChanged(widget: MaterialCalendarView?, date: CalendarDay?) {
        date?.let {
            val calenderDay = Calendar.getInstance()
            calenderDay.time = it.date
            viewModel.loadBookings(calenderDay)
        }
    }
}


class EventDecorator: DayViewDecorator{
    override fun shouldDecorate(day: CalendarDay?): Boolean  = true

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(QuoteSpan(Color.RED))
    }

}

class MyDayFormatter : DayFormatter {
    val formatter = SimpleDateFormat("d", Locale.getDefault())
    var bookings: Bookings? = null

    override fun format(day: CalendarDay): String  {
        bookings?.let { bookings1 ->
            bookings1.monthly_bookings?.let {
                it.forEach { monthlyBookings ->
                    monthlyBookings.date?.let {
                        val date = Date(it * 1000L)
                        val calender = Calendar.getInstance()
                        calender.time = date
                        if(calender.get(Calendar.DAY_OF_MONTH) == day.day &&
                                calender.get(Calendar.MONTH) == day.month){
                            return "${formatter.format(day.date)}\n${monthlyBookings.in_time}\n${monthlyBookings.out_time}"
                        }
                    }
                }
            }
        }
        return formatter.format(day.date)
    }

}