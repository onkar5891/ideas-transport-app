package ideas.transport

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController {
    @RequestMapping(value = ["/hello"], method = [RequestMethod.GET], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun hello(): User {
        return User(1, "user2",980,null, "Male", "Pune", 411027)
    }
}

data class User(val userId: Int, val name: String, val contactNo : Int, val alternateContactNo : Int?,val gender:String , val address : String, val pincode:Int)
