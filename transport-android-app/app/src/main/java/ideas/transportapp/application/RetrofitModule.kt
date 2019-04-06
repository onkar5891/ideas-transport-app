package ideas.transportapp.application

import dagger.Module
import dagger.Provides
import ideas.transportapp.cloud.UserApi
import ideas.transportapp.repository.UserRepository
import okhttp3.HttpUrl
import javax.inject.Singleton

@Module
class RetrofitModule {
    @Provides
    @Singleton
    fun provideHttpUrl(): HttpUrl? = HttpUrl.parse("http://192.168.2.37:4101")

    @Provides
    fun provideUserApi(httpUrl: HttpUrl?): UserApi = UserApi.build(httpUrl = httpUrl)


    @Provides
    fun provideUserRepository(userApi: UserApi): UserRepository = UserRepository(userApi = userApi)
}