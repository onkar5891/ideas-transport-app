package ideas.transport

import java.time.Month

class Extensions

fun Month.twoDigitMonthNumber(): String {
    return String.format("%02d", this.value)
}
