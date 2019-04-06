package ideas.transportapp.model

data class User(
    var userId: Int?,
    var name: String,
    var contactNo: Int,
    var alternateContactNo: Int?,
    var gender: String,
    var address: String,
    var pinCode: Int
)