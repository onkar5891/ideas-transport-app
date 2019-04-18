package ideas.transportapp.model

data class User(
    var userId: Long?,
    var name: String,
    var contactNo: String,
    var alternateContactNo: String?,
    var gender: String,
    var address: String,
    var pinCode: Int
)