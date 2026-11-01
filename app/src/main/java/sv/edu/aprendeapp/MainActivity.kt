package sv.edu.aprendeapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import sv.edu.aprendeapp.controller.RecursoController
import sv.edu.aprendeapp.databinding.ActivityMainBinding
import sv.edu.aprendeapp.databinding.DialogRatingBinding
import sv.edu.aprendeapp.model.Recurso
import sv.edu.aprendeapp.util.FavoritosManager
import sv.edu.aprendeapp.util.SessionManager
import sv.edu.aprendeapp.util.aplicarInsets
import sv.edu.aprendeapp.view.AdminActivity
import sv.edu.aprendeapp.view.LoginActivity
import sv.edu.aprendeapp.view.RecursoAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var session: SessionManager
    private lateinit var favoritos: FavoritosManager
    private lateinit var controller: RecursoController
    private lateinit var adapter: RecursoAdapter

    private var todos: List<Recurso> = emptyList()
    private var consulta = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.aplicarInsets()

        session = SessionManager(this)
        if (!session.estaLogueado()) {
            irALogin()
            return
        }
        favoritos = FavoritosManager(this, session.userId())
        controller = RecursoController(this)

        binding.tvSaludo.text = getString(R.string.bienvenida_usuario, session.nombre())
        binding.btnAdmin.visibility = if (session.esDocente()) View.VISIBLE else View.GONE
        binding.btnAdmin.setOnClickListener {
            startActivity(Intent(this, AdminActivity::class.java))
        }
        binding.btnLogout.setOnClickListener {
            session.cerrar()
            Toast.makeText(this, R.string.sesion_cerrada, Toast.LENGTH_SHORT).show()
            irALogin()
        }

        adapter = RecursoAdapter(
            esFavorito = { favoritos.esFavorito(it.id) },
            onFavorito = { alternarFavorito(it) },
            onAbrir = { abrirEnlace(it) },
            onCalificar = { calificar(it) }
        )
        binding.rvRecursos.layoutManager = LinearLayoutManager(this)
        binding.rvRecursos.adapter = adapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                consulta = query; aplicarFiltros(); return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                consulta = newText; aplicarFiltros(); return true
            }
        })

        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: View?, pos: Int, id: Long) = aplicarFiltros()
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }
        binding.spTipo.onItemSelectedListener = listener
        binding.spOrden.onItemSelectedListener = listener
        binding.chipFav.setOnCheckedChangeListener { _, _ -> aplicarFiltros() }
    }

    override fun onResume() {
        super.onResume()
        if (::controller.isInitialized) cargarRecursos()
    }

    private fun cargarRecursos() {
        binding.progress.visibility = View.VISIBLE
        controller.cargar(
            onSuccess = {
                binding.progress.visibility = View.GONE
                todos = it
                aplicarFiltros()
            },
            onError = {
                binding.progress.visibility = View.GONE
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            })
    }

    private fun aplicarFiltros() {
        if (!::controller.isInitialized) return
        val tipo = if (binding.spTipo.selectedItemPosition == 0) null
        else binding.spTipo.selectedItem.toString()
        val resultado = controller.filtrar(
            todos, consulta, tipo,
            binding.chipFav.isChecked, favoritos.ids(),
            binding.spOrden.selectedItemPosition
        )
        adapter.setData(resultado)
        binding.tvVacio.visibility = if (resultado.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun alternarFavorito(r: Recurso) {
        val id = r.id ?: return
        val ahora = favoritos.alternar(id)
        Toast.makeText(
            this, if (ahora) R.string.fav_agregado else R.string.fav_quitado, Toast.LENGTH_SHORT
        ).show()
        aplicarFiltros()
    }

    private fun abrirEnlace(r: Recurso) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(r.enlace)))
        } catch (e: Exception) {
            Toast.makeText(this, R.string.error_abrir_enlace, Toast.LENGTH_SHORT).show()
        }
    }

    private fun calificar(r: Recurso) {
        if (session.esDocente()) {
            Toast.makeText(this, R.string.calificar_solo_estudiante, Toast.LENGTH_SHORT).show()
            return
        }
        val d = DialogRatingBinding.inflate(LayoutInflater.from(this))
        AlertDialog.Builder(this)
            .setTitle(R.string.calificar_titulo)
            .setView(d.root)
            .setPositiveButton(R.string.btn_guardar) { _, _ ->
                val estrellas = d.ratingBar.rating.toInt()
                if (estrellas > 0) {
                    binding.progress.visibility = View.VISIBLE
                    controller.calificar(r, estrellas,
                        onOk = {
                            Toast.makeText(this, R.string.calificar_gracias, Toast.LENGTH_SHORT).show()
                            cargarRecursos()
                        },
                        onError = {
                            binding.progress.visibility = View.GONE
                            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                        })
                }
            }
            .setNegativeButton(R.string.btn_cancelar, null)
            .show()
    }

    private fun irALogin() {
        val i = Intent(this, LoginActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }
}