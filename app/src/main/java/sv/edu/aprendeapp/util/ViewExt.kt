package sv.edu.aprendeapp.util

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

fun View.aplicarInsets() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val b = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        v.setPadding(b.left, b.top, b.right, b.bottom)
        insets
    }
}