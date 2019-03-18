package ideas.transport

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/vendor")
class VendorController {
    @RequestMapping(value = ["/hello"], method = [RequestMethod.GET], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun hello(): Vendor {
        return Vendor(1, "vendor2",111,null, "Pune", "a11@b111.com")
    }
}
data class Vendor(val vendorId: Int, val name: String, val contactNo : Int, val alternateContactNo : Int?, val address : String, val emailId:String)