package ideas.transportapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ideas.transportapp.model.PushNotificationDetails
import ideas.transportapp.model.User
import ideas.transportapp.service.UserService

class UserRegistrationViewModel(private val userService: UserService): ViewModel() {
    val operationState = MutableLiveData<OperationState>()
    val userLiveData = MutableLiveData<User>()
    fun submit(user: User){
        userService.register(user, operationState, userLiveData)
    }
    fun submitInstanceId(userId: Long, instanceId: String){
        userService.registerPushNotificationId(PushNotificationDetails(null, userId, instanceId))
    }
}