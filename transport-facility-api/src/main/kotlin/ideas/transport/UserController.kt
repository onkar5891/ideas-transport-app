package ideas.transport

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.http.MediaType
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.sql.ResultSet

@RestController
@RequestMapping("/user")
class UserController(val service: UserService) {
    @RequestMapping(
            value = ["/save"],
            method = [RequestMethod.POST],
            produces = [MediaType.APPLICATION_JSON_VALUE],
            consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun save(@RequestBody user: User): User? {
        val userId = service.save(user)
        return findById(userId)
    }

    @RequestMapping(value = ["/find/{userId}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findById(@PathVariable("userId") userId: Int): User? = service.findById(userId)

    @RequestMapping(value = ["/findAll"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun list(): List<User> = service.findAll()
}

@Service
class UserService(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {
    val logger: Logger = LogManager.getLogger(UserService::class.qualifiedName)

    fun findAll(): List<User> {
        return namedParameterJdbcTemplate.query(FIND_ALL_QUERY, allColumnsRowMapper())
    }

    fun findById(userId: Int): User? {
        val parameterSource = MapSqlParameterSource(
                mapOf(Pair("userId", userId))
        )

        val result = namedParameterJdbcTemplate.query(FIND_BY_ID_QUERY, parameterSource, allColumnsRowMapper())
        if (!result.isEmpty()) {
            return result[0]
        }
        return null
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun save(user: User): Int = if (user.userId == null) create(user) else update(user)

    private fun create(user: User): Int {
        logger.info("Creating new User..")

        val parameterSource = MapSqlParameterSource(
                mapOf(
                        "name" to user.name,
                        "contactNo" to user.contactNo,
                        "alternateContactNo" to user.alternateContactNo,
                        "gender" to user.gender,
                        "address" to user.address,
                        "pinCode" to user.pinCode
                )
        )
        val keyHolder = GeneratedKeyHolder()

        namedParameterJdbcTemplate.update(INSERT_QUERY, parameterSource, keyHolder)
        return keyHolder.key.toInt()
    }

    private fun update(user: User): Int {
        logger.info("Updating User: ${user.userId}")

        val parameterSource = MapSqlParameterSource(
                mapOf(
                        "userId" to user.userId,
                        "name" to user.name,
                        "contactNo" to user.contactNo,
                        "alternateContactNo" to user.alternateContactNo,
                        "gender" to user.gender,
                        "address" to user.address,
                        "pinCode" to user.pinCode
                )
        )

        namedParameterJdbcTemplate.update(UPDATE_QUERY, parameterSource)
        return user.userId!!
    }

    private fun allColumnsRowMapper(): (ResultSet, Int) -> User = { rs, _ ->
        User(
                rs.getInt("user_id"),
                rs.getString("name"),
                rs.getInt("contact_no"),
                rs.getInt("alternate_contact_no"),
                rs.getString("gender"),
                rs.getString("address"),
                rs.getInt("pincode")
        )
    }
}

data class User(
        var userId: Int?,
        var name: String,
        var contactNo: Int,
        var alternateContactNo: Int?,
        var gender: String,
        var address: String,
        var pinCode: Int
)

const val INSERT_QUERY =
        "INSERT INTO Users " +
                "(name, contact_no, alternate_contact_no, gender, address, pinCode) " +
                "VALUES " +
                "(:name, :contactNo, :alternateContactNo, :gender, :address, :pinCode)"
const val UPDATE_QUERY =
        "UPDATE Users " +
                "SET name = :name, contact_no = :contactNo, alternate_contact_no = :alternateContactNo, gender = :gender, address = :address, pincode = :pinCode " +
                "WHERE user_id = :userId"
const val FIND_ALL_QUERY =
        "SELECT * FROM " +
                "Users"
const val FIND_BY_ID_QUERY =
        "SELECT * FROM Users " +
                "WHERE user_id = :userId"
