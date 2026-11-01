package sv.edu.aprendeapp.util

import android.content.Context
import sv.edu.aprendeapp.model.Usuario

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE)

    fun guardar(u: Usuario) {
        prefs.edit()
            .putString("id", u.id)
            .putString("nombre", u.nombre)
            .putString("rol", u.rol)
            .apply()
    }

    fun estaLogueado() = prefs.getString("id", null) != null
    fun userId() = prefs.getString("id", "") ?: ""
    fun nombre() = prefs.getString("nombre", "") ?: ""
    fun rol() = prefs.getString("rol", "") ?: ""
    fun esDocente() = rol() == Constants.ROL_DOCENTE
    fun cerrar() { prefs.edit().clear().apply() }
}