package model

import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

data class ItensLista(
    val imgFoto: ImageView?,
    val txtDescricao: EditText,
    val txtDia: TextView,
    val btnFoto: Button?
)
