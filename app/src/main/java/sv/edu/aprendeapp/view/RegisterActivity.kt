package sv.edu.aprendeapp.view

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import sv.edu.aprendeapp.R
import sv.edu.aprendeapp.controller.AuthController
import sv.edu.aprendeapp.databinding.ActivityRegisterBinding
import sv.edu.aprendeapp.util.Constants
import sv.edu.aprendeapp.util.PasswordValidator
import sv.edu.aprendeapp.util.aplicarInsets

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: AuthController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.aplicarInsets()

        auth = AuthController(this)

        binding.btnRegistro.setOnClickListener { intentarRegistro() }
        binding.tvIrLogin.setOnClickListener { finish() }
    }

    private fun intentarRegistro() {
        val nombre = binding.etNombre.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val pass = binding.etPassword.text.toString()
        val confirm = binding.etConfirm.text.toString()

        binding.tilNombre.error = null
        binding.tilEmail.error = null
        binding.tilPassword.error = null
        binding.tilConfirm.error = null

        var ok = true
        if (nombre.isEmpty()) {
            binding.tilNombre.error = getString(R.string.error_campo_vacio)
            ok = false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = getString(R.string.error_email_invalido)
            ok = false
        }
        val faltantes = PasswordValidator.faltantes(pass)
        if (faltantes.isNotEmpty()) {
            val detalle = faltantes.joinToString("\n• ", prefix = "• ") { getString(it) }
            binding.tilPassword.error = getString(R.string.pass_req_titulo) + "\n" + detalle
            ok = false
        }
        if (confirm != pass) {
            binding.tilConfirm.error = getString(R.string.error_pass_no_coincide)
            ok = false
        }
        if (!ok) return

        val rol = if (binding.rbDocente.isChecked) Constants.ROL_DOCENTE else Constants.ROL_ESTUDIANTE

        mostrarCarga(true)
        auth.registrar(nombre, email, pass, rol,
            onSuccess = {
                mostrarCarga(false)
                Toast.makeText(this, getString(R.string.registro_exitoso), Toast.LENGTH_LONG).show()
                finish()
            },
            onError = { mensaje ->
                mostrarCarga(false)
                Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
            })
    }

    private fun mostrarCarga(mostrar: Boolean) {
        binding.progress.visibility = if (mostrar) View.VISIBLE else View.GONE
        binding.btnRegistro.isEnabled = !mostrar
    }
}