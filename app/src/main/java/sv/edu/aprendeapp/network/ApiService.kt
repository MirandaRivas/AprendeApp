package sv.edu.aprendeapp.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import sv.edu.aprendeapp.model.Recurso
import sv.edu.aprendeapp.model.Usuario

interface ApiService {
    @GET("recursos")
    fun getRecursos(): Call<List<Recurso>>

    @POST("recursosX")
    fun crearRecurso(@Body recurso: Recurso): Call<Recurso>

    @PUT("recursos/{id}")
    fun actualizarRecurso(@Path("id") id: String, @Body recurso: Recurso): Call<Recurso>

    @DELETE("recursos/{id}")
    fun eliminarRecurso(@Path("id") id: String): Call<Void>

    @GET("usuario")
    fun getUsuarios(): Call<List<Usuario>>

    @POST("usuario")
    fun registrarUsuario(@Body usuario: Usuario): Call<Usuario>
}

