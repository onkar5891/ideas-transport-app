package ideas.transport

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class TransportAppApplication

@RestController
@RequestMapping("/app")
class MyController {
    @RequestMapping(value = ["/hello"], method = [RequestMethod.GET], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun hello(): User {
        return User(1, "user1")
    }
}

data class User(val id: Int, val name: String)

fun main(args: Array<String>) {
    runApplication<TransportAppApplication>(*args)
}
