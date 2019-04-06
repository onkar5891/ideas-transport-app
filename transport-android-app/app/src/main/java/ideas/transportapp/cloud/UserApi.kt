package ideas.transportapp.cloud

import android.util.Log
import ideas.transportapp.model.User
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.Executor
import java.util.concurrent.Executors

interface UserApi {
    @POST("/user/save")
    fun register(@Body user: User): Call<User>

    companion object {
        fun build(httpUrl: HttpUrl?):UserApi{
            val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                Log.d("API", it)
            })
            logger.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .callbackExecutor(Executors.newFixedThreadPool(5))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UserApi::class.java)

        }
    }
}