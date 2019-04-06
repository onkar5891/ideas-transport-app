package ideas.transportapp.viewmodel

enum class OperationStatusType {
    RUNNING,
    SUCCESS,
    FAILED
}
data class OperationState private constructor(
    val status: OperationStatusType,
    val msg: String? = null) {
    companion object {
        val LOADED = OperationState(OperationStatusType.SUCCESS)
        val LOADING = OperationState(OperationStatusType.RUNNING)
        fun error(msg: String?) = OperationState(OperationStatusType.FAILED, msg)
    }
}