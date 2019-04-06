package ideas.transportapp.application

import dagger.Component
import ideas.transportapp.MainActivity
import ideas.transportapp.UserRegistrationActivity
import ideas.transportapp.repository.UserRepository
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, RetrofitModule::class])
interface ApplicationComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(userRegistrationActivity: UserRegistrationActivity)
    fun inject(userRepository: UserRepository)
}