package ideas.transport

import org.springframework.http.MediaType
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.sql.ResultSet
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month

@RestController
@RequestMapping("/pickupDropDetails")
class PickupDropController(val service: PickupDropService) {
    @RequestMapping(value = ["/save"], consumes = [MediaType.APPLICATION_JSON_VALUE], method = [RequestMethod.POST])
    fun createPickupDropDetailsForUser(@RequestBody pickupDropList: List<PickupDropDetails>) {
        service.createPickupDropDetailsForUser(pickupDropList)
    }

    @RequestMapping(value = ["/fetch/{userId}/{month}/{year}"], consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE], method = [RequestMethod.GET])
    fun fetchPickupDropDetailsForUser(@PathVariable userId: Int, @PathVariable month: Month, @PathVariable year: Int): List<PickupDropDetails> {
        return service.fetchPickupDropDetailsForUser(userId, month, year)
    }
}

@Service
class PickupDropService(val jdbcTemplate: NamedParameterJdbcTemplate) {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun createPickupDropDetailsForUser(pickupDropList: List<PickupDropDetails>) {
        pickupDropList.forEach { details ->
            val parameterSource = MapSqlParameterSource(
                    mapOf(
                            "userId" to details.userId,
                            "travelDate" to details.travelDate,
                            "inTime" to details.inTime,
                            "outTime" to details.outTime
                    )
            )

            jdbcTemplate.update(INSERT_PICKUP_DROP_DETAILS_QUERY, parameterSource)
        }
    }

    fun fetchPickupDropDetailsForUser(userId: Int, month: Month, year: Int): List<PickupDropDetails> {
        val monthOfYear = "$year-${month.twoDigitMonthNumber()}%"

        val parameterSource = MapSqlParameterSource(
                mapOf(Pair("userId", userId), Pair("monthOfYear", monthOfYear))
        )

        return jdbcTemplate.query(FETCH_PICKUP_DROP_DETAILS_FOR_USER, parameterSource, allColumnsRowMapper())

    }

    private fun allColumnsRowMapper(): (ResultSet, Int) -> PickupDropDetails = { rs, _ ->
        PickupDropDetails(
                rs.getInt("pickup_drop_details_id"),
                rs.getInt("user_id"),
                rs.getDate("travel_date").toLocalDate(),
                rs.getTime("in_time").toLocalTime(),
                rs.getTime("out_time").toLocalTime()
        )
    }
}

data class PickupDropDetails(
        val pickup_drop_details_id: Int?,
        val userId: Int,
        val travelDate: LocalDate,
        val inTime: LocalTime,
        val outTime: LocalTime
)

const val INSERT_PICKUP_DROP_DETAILS_QUERY =
        "INSERT INTO pickup_drop_details " +
                "(user_id, travel_date, in_time, out_time) " +
                "VALUES " +
                "(:userId, :travelDate, :inTime, :outTime)"

const val FETCH_PICKUP_DROP_DETAILS_FOR_USER =
        "select * from pickup_drop_details " +
                "where user_id = :userId " +
                "and travel_date like :monthOfYear"
