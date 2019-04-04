package ideas.transport.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import com.microsoft.sqlserver.jdbc.SQLServerDataSource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import javax.sql.DataSource

@Configuration
class TransportAppConfig {
    @Value("\${spring.datasource.url}")
    lateinit var url: String
    @Value("\${spring.datasource.username}")
    lateinit var username: String
    @Value("\${spring.datasource.password}")
    lateinit var password: String

    @Bean
    fun namedParameterJdbcTemplate(): NamedParameterJdbcTemplate {
        return NamedParameterJdbcTemplate(dataSource())
    }

    @Bean
    fun dataSource(): DataSource {
        val dataSource = SQLServerDataSource()
        dataSource.url = url
        dataSource.user = username
        dataSource.setPassword(password)
        return dataSource
    }

    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper()
                .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                .enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(ParameterNamesModule())
                .registerModule(Jdk8Module())
                .registerModule(JavaTimeModule())
                .registerKotlinModule()
    }
}
