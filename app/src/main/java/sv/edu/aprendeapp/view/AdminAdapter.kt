package sv.edu.aprendeapp.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sv.edu.aprendeapp.databinding.ItemAdminBinding
import sv.edu.aprendeapp.model.Recurso

class AdminAdapter(
    private val onEditar: (Recurso) -> Unit,
    private val onEliminar: (Recurso) -> Unit
) : RecyclerView.Adapter<AdminAdapter.VH>() {

    private var items: List<Recurso> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(nuevos: List<Recurso>) {
        items = nuevos
        notifyDataSetChanged()
    }

    class VH(val b: ItemAdminBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val r = items[position]
        holder.b.tvTitulo.text = r.titulo
        holder.b.tvTipo.text = r.tipo
        holder.b.btnEditar.setOnClickListener { onEditar(r) }
        holder.b.btnEliminar.setOnClickListener { onEliminar(r) }
    }

    override fun getItemCount() = items.size
}