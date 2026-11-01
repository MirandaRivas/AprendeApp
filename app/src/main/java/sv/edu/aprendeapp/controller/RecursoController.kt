package sv.edu.aprendeapp.controller

import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sv.edu.aprendeapp.R
import sv.edu.aprendeapp.model.Recurso
import sv.edu.aprendeapp.network.RetrofitClient

class RecursoController(private val context: Context) {

    private val api = RetrofitClient.api

    fun cargar(onSuccess: (List<Recurso>) -> Unit, onError: (String) -> Unit) {
        api.getRecursos().enqueue(object : Callback<List<Recurso>> {
            override fun onResponse(call: Call<List<Recurso>>, response: Response<List<Recurso>>) {
                if (response.isSuccessful) {
                    onSuccess(response.body().orEmpty())
                } else if (response.code() == 404) {
                    onSuccess(emptyList())
                } else {
                    onError(context.getString(R.string.error_servidor, response.code()))
                }
            }

            override fun onFailure(call: Call<List<Recurso>>, t: Throwable) {
                onError(context.getString(R.string.error_conexion))
            }
        })
    }

    fun filtrar(
        lista: List<Recurso>,
        consulta: String,
        tipo: String?,
        soloFavoritos: Boolean,
        favoritos: Set<String>,
        orden: Int
    ): List<Recurso> {
        var r = lista
        val q = consulta.trim()
        if (q.isNotEmpty()) {
            r = r.filter {
                it.id == q ||
                        it.titulo.contains(q, ignoreCase = true) ||
                        it.descripcion.contains(q, ignoreCase = true) ||
                        it.tipo.contains(q, ignoreCase = true)
            }
        }
        if (tipo != null) r = r.filter { it.tipo.equals(tipo, ignoreCase = true) }
        if (soloFavoritos) r = r.filter { it.id != null && favoritos.contains(it.id) }

        return when (orden) {
            1 -> r.sortedWith(compareBy({ it.tipo.lowercase() }, { it.titulo.lowercase() }))
            2 -> r.sortedByDescending { it.promedio() }
            else -> r.sortedBy { it.titulo.lowercase() }
        }
    }

    fun calificar(r: Recurso, estrellas: Int, onOk: () -> Unit, onError: (String) -> Unit) {
        val id = r.id ?: return
        val actualizado = r.copy(
            ratingTotal = r.ratingTotal + estrellas,
            ratingCount = r.ratingCount + 1
        )
        api.actualizarRecurso(id, actualizado).enqueue(object : Callback<Recurso> {
            override fun onResponse(call: Call<Recurso>, response: Response<Recurso>) {
                if (response.isSuccessful) onOk()
                else onError(context.getString(R.string.error_servidor, response.code()))
            }

            override fun onFailure(call: Call<Recurso>, t: Throwable) {
                onError(context.getString(R.string.error_conexion))
            }
        })
    }
}