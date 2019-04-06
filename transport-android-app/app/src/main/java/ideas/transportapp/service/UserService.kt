package ideas.transportapp.service

import androidx.lifecycle.MutableLiveData
import ideas.transportapp.model.User
import ideas.transportapp.repository.UserRepository
import ideas.transportapp.viewmodel.OperationState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface UserService {
    fun register(user: User, operationState: MutableLiveData<OperationState>)
}
class UserServiceImpl(private val userRepository: UserRepository): UserService{
    override fun register(user: User, operationState: MutableLiveData<OperationState>) {
        userRepository.register(user, object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                operationState.postValue(OperationState.error(t.message))
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                operationState.postValue(OperationState.LOADED)
            }
        })
    }

}