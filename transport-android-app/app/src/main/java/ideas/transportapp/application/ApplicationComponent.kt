package ideas.transportapp.application

import dagger.Component
import ideas.transportapp.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface ApplicationComponent {
    fun inject(mainActivity: MainActivity)
}