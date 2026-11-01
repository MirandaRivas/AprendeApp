package sv.edu.aprendeapp.util

import android.content.Context

class FavoritosManager(context: Context, userId: String) {
    private val prefs = context.getSharedPreferences("fav_$userId", Context.MODE_PRIVATE)

    fun ids(): Set<String> = HashSet(prefs.getStringSet("ids", emptySet()) ?: emptySet())

    fun esFavorito(id: String?): Boolean = id != null && ids().contains(id)

    fun alternar(id: String): Boolean {
        val actual = ids().toMutableSet()
        val ahoraFav = if (actual.contains(id)) { actual.remove(id); false } else { actual.add(id); true }
        prefs.edit().putStringSet("ids", actual).apply()
        return ahoraFav
    }
}