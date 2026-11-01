package sv.edu.aprendeapp.model

data class Usuario(
    val id: String? = null,
    val nombre: String = "",
    val email: String = "",
    val password: String = "",
    val rol: String = ""
)