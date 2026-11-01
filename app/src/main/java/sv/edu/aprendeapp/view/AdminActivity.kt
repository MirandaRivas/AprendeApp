package sv.edu.aprendeapp.view

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import sv.edu.aprendeapp.R
import sv.edu.aprendeapp.controller.AdminController
import sv.edu.aprendeapp.databinding.ActivityAdminBinding
import sv.edu.aprendeapp.databinding.DialogRecursoBinding
import sv.edu.aprendeapp.model.Recurso
import sv.edu.aprendeapp.util.aplicarInsets

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var controller: AdminController
    private lateinit var adapter: AdminAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.aplicarInsets()

        controller = AdminController(this)
        adapter = AdminAdapter(
            onEditar = { mostrarFormulario(it) },
            onEliminar = { confirmarEliminar(it) }
        )
        binding.rvAdmin.layoutManager = LinearLayoutManager(this)
        binding.rvAdmin.adapter = adapter
        binding.fabAgregar.setOnClickListener { mostrarFormulario(null) }

        cargar()
    }

    private fun cargar() {
        binding.progress.visibility = View.VISIBLE
        controller.cargar(
            onOk = {
                binding.progress.visibility = View.GONE
                adapter.setData(it)
                binding.tvVacio.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            },
            onError = {
                binding.progress.visibility = View.GONE
                toast(it)
            })
    }

    private fun mostrarFormulario(recurso: Recurso?) {
        val d = DialogRecursoBinding.inflate(LayoutInflater.from(this))
        if (recurso != null) {
            d.etTitulo.setText(recurso.titulo)
            d.etDescripcion.setText(recurso.descripcion)
            d.etTipo.setText(recurso.tipo)
            d.etEnlace.setText(recurso.enlace)
            d.etImagen.setText(recurso.imagen)
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle(if (recurso == null) R.string.dlg_nuevo else R.string.dlg_editar)
            .setView(d.root)
            .setPositiveButton(R.string.btn_guardar, null)
            .setNegativeButton(R.string.btn_cancelar, null)
            .create()
        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val titulo = d.etTitulo.text.toString().trim()
            val desc = d.etDescripcion.text.toString().trim()
            val tipo = d.etTipo.text.toString().trim()
            val enlace = d.etEnlace.text.toString().trim()
            val imagen = d.etImagen.text.toString().trim()

            d.tilTitulo.error = null; d.tilDescripcion.error = null; d.tilTipo.error = null
            d.tilEnlace.error = null; d.tilImagen.error = null

            var ok = true
            val vacio = getString(R.string.error_campo_vacio)
            val urlMala = getString(R.string.error_url_invalida)
            if (titulo.isEmpty()) { d.tilTitulo.error = vacio; ok = false }
            if (desc.isEmpty()) { d.tilDescripcion.error = vacio; ok = false }
            if (tipo.isEmpty()) { d.tilTipo.error = vacio; ok = false }
            if (!Patterns.WEB_URL.matcher(enlace).matches()) { d.tilEnlace.error = urlMala; ok = false }
            if (!Patterns.WEB_URL.matcher(imagen).matches()) { d.tilImagen.error = urlMala; ok = false }
            if (!ok) return@setOnClickListener

            val nuevo = Recurso(
                id = recurso?.id,
                titulo = titulo, descripcion = desc, tipo = tipo,
                enlace = enlace, imagen = imagen,
                ratingTotal = recurso?.ratingTotal ?: 0,
                ratingCount = recurso?.ratingCount ?: 0
            )
            dialog.dismiss()
            binding.progress.visibility = View.VISIBLE

            if (recurso == null) {
                controller.crear(nuevo,
                    onOk = { toast(getString(R.string.recurso_creado)); cargar() },
                    onError = { binding.progress.visibility = View.GONE; toast(it) })
            } else {
                controller.actualizar(recurso.id!!, nuevo,
                    onOk = { toast(getString(R.string.recurso_actualizado)); cargar() },
                    onError = { binding.progress.visibility = View.GONE; toast(it) })
            }
        }
    }

    private fun confirmarEliminar(r: Recurso) {
        AlertDialog.Builder(this)
            .setTitle(R.string.eliminar_titulo)
            .setMessage(R.string.eliminar_mensaje)
            .setPositiveButton(R.string.btn_eliminar) { _, _ ->
                binding.progress.visibility = View.VISIBLE
                controller.eliminar(r.id!!,
                    onOk = { toast(getString(R.string.recurso_eliminado)); cargar() },
                    onError = { binding.progress.visibility = View.GONE; toast(it) })
            }
            .setNegativeButton(R.string.btn_cancelar, null)
            .show()
    }

    private fun toast(m: String) = Toast.makeText(this, m, Toast.LENGTH_LONG).show()
}

