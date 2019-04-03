package ideas.transportapp.model

data class Bookings(
    var user_id: String?,
    var monthly_bookings: List<MonthlyBookings>?
)

data class MonthlyBookings (
    var date: Long?,
    var in_time: String?,
    var out_time: String?
)