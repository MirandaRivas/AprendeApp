package sv.edu.aprendeapp.controller

import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sv.edu.aprendeapp.R
import sv.edu.aprendeapp.model.Recurso
import sv.edu.aprendeapp.network.RetrofitClient

class AdminController(private val context: Context) {

    private val api = RetrofitClient.api

    private fun <T> cb(onOk: () -> Unit, onError: (String) -> Unit) = object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) onOk()
            else onError(context.getString(R.string.error_servidor, response.code()))
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            onError(context.getString(R.string.error_conexion))
        }
    }

    fun cargar(onOk: (List<Recurso>) -> Unit, onError: (String) -> Unit) {
        api.getRecursos().enqueue(object : Callback<List<Recurso>> {
            override fun onResponse(call: Call<List<Recurso>>, response: Response<List<Recurso>>) {
                if (response.isSuccessful) onOk(response.body().orEmpty())
                else if (response.code() == 404) onOk(emptyList())
                else onError(context.getString(R.string.error_servidor, response.code()))
            }

            override fun onFailure(call: Call<List<Recurso>>, t: Throwable) {
                onError(context.getString(R.string.error_conexion))
            }
        })
    }

    fun crear(r: Recurso, onOk: () -> Unit, onError: (String) -> Unit) {
        api.crearRecurso(r).enqueue(cb<Recurso>(onOk, onError))
    }

    fun actualizar(id: String, r: Recurso, onOk: () -> Unit, onError: (String) -> Unit) {
        api.actualizarRecurso(id, r).enqueue(cb<Recurso>(onOk, onError))
    }

    fun eliminar(id: String, onOk: () -> Unit, onError: (String) -> Unit) {
        api.eliminarRecurso(id).enqueue(cb<Void>(onOk, onError))
    }
}