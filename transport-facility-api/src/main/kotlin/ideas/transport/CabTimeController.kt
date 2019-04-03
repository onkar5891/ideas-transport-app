package ideas.transport

import org.springframework.http.MediaType
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.sql.ResultSet

@RestController
@RequestMapping("/cabTimings")
class CabTimeController(val cabTimeService: CabTimeService) {
    @RequestMapping(value = ["/pickup"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findPickupTimings(): List<String> = cabTimeService.findPickupTimings()

    @RequestMapping(value = ["/drop"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findDropTimings(): List<String> = cabTimeService.findDropTimings()
}

@Service
class CabTimeService(val jdbcTemplate: NamedParameterJdbcTemplate) {
    fun findPickupTimings(): List<String> = jdbcTemplate.query(FIND_PICKUP_TIMINGS_QUERY, cabTimeRowMapper())
    fun findDropTimings(): List<String> = jdbcTemplate.query(FIND_DROP_TIMINGS_QUERY, cabTimeRowMapper())

    private fun cabTimeRowMapper(): (ResultSet, Int) -> String = {rs, _ -> rs.getString("travel_time")}
}

const val FIND_PICKUP_TIMINGS_QUERY =
        "SELECT travel_time FROM " +
                "In_Time"

const val FIND_DROP_TIMINGS_QUERY =
        "SELECT travel_time FROM " +
                "Out_Time"
