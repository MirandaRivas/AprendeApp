package sv.edu.aprendeapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sv.edu.aprendeapp.util.Constants

object RetrofitClient {
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}