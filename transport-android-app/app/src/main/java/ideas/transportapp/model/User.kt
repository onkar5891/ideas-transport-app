package ideas.transportapp.model

data class User(
    var userId: Int?,
    var name: String,
    var contactNo: Number?,
    var alternateContactNo: Number?,
    var gender: String,
    var address: String,
    var pinCode: Int
)