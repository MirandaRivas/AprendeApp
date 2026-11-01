package sv.edu.aprendeapp.util

import sv.edu.aprendeapp.R

object PasswordValidator {
    private const val ESPECIALES = "!@#\$%^&*"

    fun faltantes(p: String): List<Int> {
        val lista = mutableListOf<Int>()
        if (p.length < 12) lista.add(R.string.pass_req_longitud)
        if (p.none { it.isUpperCase() }) lista.add(R.string.pass_req_mayuscula)
        if (p.none { it.isLowerCase() }) lista.add(R.string.pass_req_minuscula)
        if (p.none { it.isDigit() }) lista.add(R.string.pass_req_numero)
        if (p.none { ESPECIALES.contains(it) }) lista.add(R.string.pass_req_especial)
        return lista
    }
}