package ideas.transportapp.service

import androidx.lifecycle.MutableLiveData
import ideas.transportapp.model.PushNotificationDetails
import ideas.transportapp.model.User
import ideas.transportapp.repository.UserRepository
import ideas.transportapp.viewmodel.OperationState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

interface UserService {
    fun register(user: User, operationState: MutableLiveData<OperationState>, userLiveData: MutableLiveData<User>)
    fun registerPushNotificationId(pushNotificationDetails: PushNotificationDetails)
}
class UserServiceImpl(private val userRepository: UserRepository): UserService{

    private val executor = Executors.newSingleThreadExecutor()

    override fun registerPushNotificationId(pushNotificationDetails: PushNotificationDetails) {
        executor.execute {
            userRepository.registerPushNotification(pushNotificationDetails, object: Callback<PushNotificationDetails>{
                override fun onFailure(call: Call<PushNotificationDetails>, t: Throwable) {
                    //TODO
                }

                override fun onResponse(
                    call: Call<PushNotificationDetails>,
                    response: Response<PushNotificationDetails>
                ) {
                    // TODO
                }
            })
        }
    }

    override fun register(user: User, operationState: MutableLiveData<OperationState>, userLiveData: MutableLiveData<User>) {
        executor.execute {
            userRepository.register(user, object : Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    operationState.postValue(OperationState.error(t.message))
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if(response.isSuccessful){
                        operationState.postValue(OperationState.LOADED)
                        userLiveData.postValue(response.body() as User)
                    } else {
                        operationState.postValue(OperationState.error(response.message()))
                    }
                }
            })
        }
    }

}