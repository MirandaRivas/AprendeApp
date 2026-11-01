package sv.edu.aprendeapp.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import sv.edu.aprendeapp.MainActivity
import sv.edu.aprendeapp.R
import sv.edu.aprendeapp.controller.AuthController
import sv.edu.aprendeapp.databinding.ActivityLoginBinding
import sv.edu.aprendeapp.util.SessionManager
import sv.edu.aprendeapp.util.aplicarInsets

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var session: SessionManager
    private lateinit var auth: AuthController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session = SessionManager(this)
        if (session.estaLogueado()) {
            irAPrincipal()
            return
        }
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.aplicarInsets()

        auth = AuthController(this)

        binding.btnLogin.setOnClickListener { intentarLogin() }
        binding.tvIrRegistro.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun intentarLogin() {
        val email = binding.etEmail.text.toString().trim()
        val pass = binding.etPassword.text.toString()
        binding.tilEmail.error = null
        binding.tilPassword.error = null

        var ok = true
        if (email.isEmpty()) {
            binding.tilEmail.error = getString(R.string.error_campo_vacio)
            ok = false
        }
        if (pass.isEmpty()) {
            binding.tilPassword.error = getString(R.string.error_campo_vacio)
            ok = false
        }
        if (!ok) return

        mostrarCarga(true)
        auth.login(email, pass,
            onSuccess = { usuario ->
                mostrarCarga(false)
                session.guardar(usuario)
                Toast.makeText(
                    this, getString(R.string.bienvenida_usuario, usuario.nombre), Toast.LENGTH_SHORT
                ).show()
                irAPrincipal()
            },
            onError = { mensaje ->
                mostrarCarga(false)
                Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
            })
    }

    private fun mostrarCarga(mostrar: Boolean) {
        binding.progress.visibility = if (mostrar) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !mostrar
    }

    private fun irAPrincipal() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}