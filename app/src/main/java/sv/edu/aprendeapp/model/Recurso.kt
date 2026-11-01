package sv.edu.aprendeapp.model

data class Recurso(
    val id: String? = null,
    val titulo: String = "",
    val descripcion: String = "",
    val tipo: String = "",
    val enlace: String = "",
    val imagen: String = "",
    val ratingTotal: Int = 0,
    val ratingCount: Int = 0
) {
    fun promedio(): Double =
        if (ratingCount == 0) 0.0 else ratingTotal.toDouble() / ratingCount
}