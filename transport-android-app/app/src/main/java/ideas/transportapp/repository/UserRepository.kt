package ideas.transportapp.repository

import ideas.transportapp.cloud.UserApi
import ideas.transportapp.model.User
import retrofit2.Callback

class UserRepository(var userApi: UserApi) {

    fun register(user: User, callback: Callback<User>){
        userApi.register(user).enqueue(callback)
    }
}