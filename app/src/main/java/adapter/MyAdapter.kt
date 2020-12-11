package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.desafioestagiobloomidea.R
import kotlinx.android.synthetic.main.custom_itens_row.view.*
import model.ItensLista

class MyAdapter(val listItems: ArrayList<ItensLista>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    // Funcao responsável por inflar o nosso custom_itens_row layout e passar para o nosso ViewHolder, que irá representar
    // cada contâiner de item demonstrado na RecyclerView.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.custom_itens_row,parent,false)
        return MyViewHolder(v)
    }

    // Funcao que receberá cada item da nossa data Class associadas aos respectivos ID´s do custom layout.
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(listItems[position])

    }

    // Funcao que retorna o tamanho total da nossa lista.
    override fun getItemCount(): Int {
        return listItems.size
    }

    // Nesta inner Class abaixo, iremos criar novos objetos que irão receber cada Id associado a cada objeto do layout custom_itens_row,
    // e os mesmos serão recebidos como parâmetros no Adapter da classe MyAdapter.
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imgFoto = view.findViewById<ImageView>(R.id.fotos)
        var txtDescricoes = view.txtDescricaoFoto
        var txtDias = view.txtDiaFoto
        var btnFotos = view.btnAbrirFoto


        // Funcao que será responsável por receber os valores da inner Classe MyViewHolder e associá-los a nossa data Class de ItensLista
        // que anteriormente foram associados a cada Id do custom layout que deverá representar cada linha do RecyclerView.
        fun bindItems(itensLista: ItensLista) {
            imgFoto = itensLista.imgFoto
            txtDescricoes = itensLista.txtDescricao
            txtDias = itensLista.txtDia
            btnFotos = itensLista.btnFoto
        }
    }
}