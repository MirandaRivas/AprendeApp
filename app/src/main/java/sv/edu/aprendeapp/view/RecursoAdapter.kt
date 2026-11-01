package sv.edu.aprendeapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import sv.edu.aprendeapp.R
import sv.edu.aprendeapp.databinding.ItemRecursoBinding
import sv.edu.aprendeapp.model.Recurso

class RecursoAdapter(
    private val esFavorito: (Recurso) -> Boolean,
    private val onFavorito: (Recurso) -> Unit,
    private val onAbrir: (Recurso) -> Unit,
    private val onCalificar: (Recurso) -> Unit
) : RecyclerView.Adapter<RecursoAdapter.VH>() {

    private var items: List<Recurso> = emptyList()

    @android.annotation.SuppressLint("NotifyDataSetChanged")
    fun setData(nuevos: List<Recurso>) {
        items = nuevos
        notifyDataSetChanged()
    }

    class VH(val b: ItemRecursoBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemRecursoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val r = items[position]
        val ctx = holder.itemView.context
        holder.b.tvTitulo.text = r.titulo
        holder.b.tvTipo.text = r.tipo
        holder.b.tvDescripcion.text = r.descripcion
        holder.b.tvRating.text =
            if (r.ratingCount == 0) ctx.getString(R.string.sin_calificaciones)
            else ctx.getString(R.string.rating_formato, r.promedio(), r.ratingCount)

        if (r.imagen.isNotBlank()) {
            Picasso.get().load(r.imagen).into(holder.b.ivRecurso)
        } else {
            holder.b.ivRecurso.setImageDrawable(null)
        }

        holder.b.btnFav.text = if (esFavorito(r)) "❤️" else "🤍"
        holder.b.btnFav.setOnClickListener { onFavorito(r) }
        holder.b.tvRating.setOnClickListener { onCalificar(r) }
        holder.itemView.setOnClickListener { onAbrir(r) }
    }

    override fun getItemCount() = items.size
}