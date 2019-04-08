package ideas.transportapp.application

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.util.regex.Pattern

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit){
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun EditText.validate(validator : (String) -> Boolean, msg : String){
    this.afterTextChanged{
        this.error = if(validator.invoke(it)) null else msg
    }
    this.error = if (validator(this.text.toString())) null else msg
}

fun String.isValidEmail(): Boolean {
    /*
        Not using {java.util.regex.Patterns} due to unit testing difficulties.
     */
    val emailAddressPattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+")


    return this.isNotEmpty() &&
            emailAddressPattern.matcher(this).matches()
}

fun String.isValidContactNumber(len: Int): Boolean {
    return this.isNotEmpty() && this.length == len
}

