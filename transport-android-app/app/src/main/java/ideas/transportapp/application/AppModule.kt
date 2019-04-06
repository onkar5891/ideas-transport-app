package ideas.transportapp.application

import android.app.Application
import dagger.Module
import dagger.Provides
import ideas.transportapp.repository.BookingsRepository
import ideas.transportapp.repository.UserRepository
import javax.inject.Singleton

@Module
class AppModule(val application: Application) {

    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    fun provideBookingsRepository(): BookingsRepository  = BookingsRepository(application)
}