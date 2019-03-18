package ideas.transport

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/cab")
class CabController {
    @RequestMapping(value = ["/hello"], method = [RequestMethod.GET], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun hello(): Cab {
        return Cab(1, "cab2",111)
    }
}

data class Cab(val cabId: Int, val cabNo: String, val cabCapacity:Int)