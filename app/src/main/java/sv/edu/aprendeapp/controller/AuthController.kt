package sv.edu.aprendeapp.controller

import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sv.edu.aprendeapp.R
import sv.edu.aprendeapp.model.Usuario
import sv.edu.aprendeapp.network.RetrofitClient
import sv.edu.aprendeapp.util.sha256

class AuthController(private val context: Context) {

    private val api = RetrofitClient.api

    fun login(
        email: String,
        password: String,
        onSuccess: (Usuario) -> Unit,
        onError: (String) -> Unit
    ) {
        api.getUsuarios().enqueue(object : Callback<List<Usuario>> {
            override fun onResponse(call: Call<List<Usuario>>, response: Response<List<Usuario>>) {
                val lista = response.body()
                if (response.isSuccessful && lista != null) {
                    val usuario = lista.firstOrNull {
                        it.email.equals(email.trim(), ignoreCase = true) &&
                                it.password == password.sha256()
                    }
                    if (usuario != null) onSuccess(usuario)
                    else onError(context.getString(R.string.error_credenciales))
                } else if (response.code() == 404) {
                    onError(context.getString(R.string.error_credenciales))
                } else {
                    onError(context.getString(R.string.error_servidor, response.code()))
                }
            }

            override fun onFailure(call: Call<List<Usuario>>, t: Throwable) {
                onError(context.getString(R.string.error_conexion))
            }
        })
    }

    fun registrar(
        nombre: String,
        email: String,
        password: String,
        rol: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        api.getUsuarios().enqueue(object : Callback<List<Usuario>> {
            override fun onResponse(call: Call<List<Usuario>>, response: Response<List<Usuario>>) {
                val lista = response.body().orEmpty()
                if (!response.isSuccessful && response.code() != 404) {
                    onError(context.getString(R.string.error_servidor, response.code()))
                    return
                }
                if (lista.any { it.email.equals(email.trim(), ignoreCase = true) }) {
                    onError(context.getString(R.string.error_email_existe))
                    return
                }
                val nuevo = Usuario(
                    nombre = nombre.trim(),
                    email = email.trim(),
                    password = password.sha256(),
                    rol = rol
                )
                api.registrarUsuario(nuevo).enqueue(object : Callback<Usuario> {
                    override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                        if (response.isSuccessful) onSuccess()
                        else onError(context.getString(R.string.error_servidor, response.code()))
                    }

                    override fun onFailure(call: Call<Usuario>, t: Throwable) {
                        onError(context.getString(R.string.error_conexion))
                    }
                })
            }

            override fun onFailure(call: Call<List<Usuario>>, t: Throwable) {
                onError(context.getString(R.string.error_conexion))
            }
        })
    }
}