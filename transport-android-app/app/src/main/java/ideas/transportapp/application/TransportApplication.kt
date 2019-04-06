package ideas.transportapp.application

import android.app.Application

class TransportApplication: Application() {
    companion object {
        lateinit var application: TransportApplication
        lateinit var applicationComponent: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        applicationComponent = DaggerApplicationComponent.builder().appModule(AppModule(this)).retrofitModule(RetrofitModule()).build()
    }
}