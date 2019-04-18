package ideas.transport.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.mysql.cj.jdbc.MysqlDataSource
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import javax.sql.DataSource
import java.io.FileInputStream

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
        val dataSource = MysqlDataSource()
        dataSource.setURL(url)
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

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    fun firebaseMessaging(): FirebaseMessaging {
        val serviceAccount = FileInputStream("/Users/ashish/Projects/Ideas/ideas-transport-app/transport-facility-api/src/main/resources/serviceAccountKey.json")

        val options = FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://ideastransportapp.firebaseio.com")
                .build()

        FirebaseApp.initializeApp(options)
        return FirebaseMessaging.getInstance()
    }
}
