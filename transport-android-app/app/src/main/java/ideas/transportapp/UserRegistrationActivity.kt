package ideas.transportapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import ideas.transportapp.application.TransportApplication
import ideas.transportapp.application.isValidContactNumber
import ideas.transportapp.application.validate
import ideas.transportapp.model.User
import ideas.transportapp.repository.UserRepository
import ideas.transportapp.service.UserServiceImpl
import ideas.transportapp.viewmodel.OperationState
import ideas.transportapp.viewmodel.UserRegistrationViewModel
import kotlinx.android.synthetic.main.activity_user_screen.*
import javax.inject.Inject

class UserRegistrationActivity : AppCompatActivity() {
    val TAG = "Ideas App"

    private lateinit var viewModel: UserRegistrationViewModel
    @Inject
    lateinit var userRepository: UserRepository

    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_screen)
        TransportApplication.applicationComponent.inject(this)
        initViewModel()
    }
    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return UserRegistrationViewModel(UserServiceImpl(userRepository)) as T
            }
        }).get(UserRegistrationViewModel::class.java)

        viewModel.operationState.observe(this, Observer {
            when(it){
                OperationState.LOADING -> {
                    operation_progress.visibility = View.VISIBLE
                    window?.setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }
                OperationState.LOADED -> {
                    operation_progress.visibility = View.GONE
                }
            }
            it?.msg?.let { errorMessage ->
                window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                operation_progress.visibility = View.GONE
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            }
        })

        viewModel.userLiveData.observe(this, Observer {
            it?.let { user ->
                this@UserRegistrationActivity.user = user
                cloudMessagingRegistration()
            }
        })
    }
    fun registerUser(view: View) {
        user_registration_et.validate({s->s.isNotEmpty()}, getString(R.string.user_name_empty_error_message))
        user_contact_no_et.validate({s -> s.isValidContactNumber(10)}, getString(R.string.user_contact_no_invalid_message))
        address_et.validate({s -> s.isNotEmpty()}, getString(R.string.user_addres_empty_error_message))
        pincode_et.validate({s -> s.isNotEmpty()}, getString(R.string.user_pincode_empty_error_message))

        if(user_registration_et.text.toString().isNotEmpty() &&
            user_contact_no_et.text.toString().isValidContactNumber(10) &&
            address_et.text.toString().isNotEmpty() &&
            pincode_et.text.toString().isNotEmpty()){

            viewModel.submit(
                User(
                    userId = null,
                    name = user_registration_et.text.toString(),
                    contactNo = user_contact_no_et.text.toString(),
                    alternateContactNo = user_alt_contact_no_et.text.toString(),
                    gender = gender_spinner.selectedItem.toString(),
                    address = address_et.text.toString(),
                    pinCode = Integer.parseInt(pincode_et.text.toString())))
        }
    }
    fun cancel(view: View){
        finish()
    }
    private fun cloudMessagingRegistration(){
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                Log.d(TAG, token)
                user.userId?.let { token?.let { it1 -> viewModel.submitInstanceId(it, it1) } }
                window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                startActivity(Intent(this, MainActivity::class.java))
            })
    }
}
