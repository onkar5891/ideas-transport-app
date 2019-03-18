package ideas.transport

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/driver")
class DriverController {
    @RequestMapping(value = ["/hello"], method = [RequestMethod.GET], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun hello(): Driver {
        return Driver(1, "driver2",980)
    }
}
data class Driver(val driverId: Int, val driverName: String, val driverContactNo: Int)