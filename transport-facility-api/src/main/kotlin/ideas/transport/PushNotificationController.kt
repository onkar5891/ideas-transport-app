package ideas.transport

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.http.MediaType
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import java.sql.ResultSet


@RestController
@RequestMapping("/pushNotificationDetails")
class PushNotificationController(val service: PushNotificationService) {

    @RequestMapping(value = ["/save"],
            consumes = [MediaType.APPLICATION_JSON_VALUE],
            produces = [MediaType.APPLICATION_JSON_VALUE],
            method = [RequestMethod.POST])
    fun savePushNotificationDetails(@RequestBody pushNotificationDetails: PushNotificationDetails): PushNotificationDetails? {
        return service.save(pushNotificationDetails)
    }

    @RequestMapping(value = ["/find/{userId}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findByUserId(@PathVariable("userId") userId: Int): PushNotificationDetails? {
        return service.findByUserId(userId)
    }
}

@Service
class PushNotificationService(val jdbcTemplate: NamedParameterJdbcTemplate, val firebaseMessaging: FirebaseMessaging) {

    fun save(pushNotificationDetails: PushNotificationDetails): PushNotificationDetails? {
        val saveParameterSource = MapSqlParameterSource(
                mapOf(Pair("userId", pushNotificationDetails.userId), Pair("notificationId", pushNotificationDetails.notificationId))
        )

        val pushNotificationDetailsInDb = findByUserId(pushNotificationDetails.userId)

        if (pushNotificationDetailsInDb != null) {
            jdbcTemplate.update(UPDATE_PUSH_NOTIFICATION_QUERY, saveParameterSource)
        } else {
            jdbcTemplate.update(INSERT_PUSH_NOTIFICATION_QUERY, saveParameterSource)
        }


        val token = pushNotificationDetails.notificationId
        val notification = Notification("Hi", "Your registration request is accepted!!")
        val message = Message.builder()
                .setNotification(notification)
                .setToken(token)
                .build()
        val response = firebaseMessaging.send(message)
        println(response)
        return pushNotificationDetails
    }

    fun findByUserId(userId: Int): PushNotificationDetails? {
        val fetchParameterSource = MapSqlParameterSource(mapOf(Pair("userId", userId)))
        val pushNotificationDetails = jdbcTemplate.query(FIND_PUSH_NOTIFICATION_BY_USER_ID, fetchParameterSource, allColumnsMapper())

        if (!pushNotificationDetails.isEmpty()) {
            return pushNotificationDetails[0]
        }
        return null
    }

    private fun allColumnsMapper(): (ResultSet, Int) -> PushNotificationDetails = { resultSet, i ->
        PushNotificationDetails(
                resultSet.getInt("push_notification_details_id"),
                resultSet.getInt("user_id"),
                resultSet.getString("notification_id")
        )
    }
}

data class PushNotificationDetails(
        val pushNotificationDetailsId: Int?,
        val userId: Int,
        val notificationId: String
)

const val INSERT_PUSH_NOTIFICATION_QUERY =
        "insert into push_notification_details " +
                "(user_id, notification_id) " +
                "values " +
                "(:userId, :notificationId)"
const val UPDATE_PUSH_NOTIFICATION_QUERY =
        "update push_notification_details " +
                "set notification_id = :notificationId " +
                "where user_id = :userId"
const val FIND_PUSH_NOTIFICATION_BY_USER_ID =
        "select * from push_notification_details " +
                "where user_id = :userId"
