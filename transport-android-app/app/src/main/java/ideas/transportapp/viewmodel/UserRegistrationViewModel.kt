package ideas.transportapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ideas.transportapp.model.User
import ideas.transportapp.service.UserService

class UserRegistrationViewModel(private val userService: UserService): ViewModel() {
    val operationState = MutableLiveData<OperationState>()
    fun submit(user: User){
        userService.register(user, operationState)
    }
}