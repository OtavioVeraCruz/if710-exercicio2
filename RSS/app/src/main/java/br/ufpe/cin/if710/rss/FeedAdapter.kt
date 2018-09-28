package br.ufpe.cin.if710.rss

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.ufpe.cin.if710.rss.db.SQLiteRSSHelper
import kotlinx.android.synthetic.main.itemlista.view.*

class FeedAdapter (var items :List<ItemRSS>, private val c: Context) :
        RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    //pra cada item da lista, coloca as informacoes dentro dos Textviews
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)
        holder.bindView(item)
        //abre a página a partir do link
        holder.itemView.setOnClickListener{
            val db=SQLiteRSSHelper.getInstance(c.applicationContext)
            db.markAsRead(item.link)
            val uri = Uri.parse(item.link)
            c.startActivity(Intent(Intent.ACTION_VIEW, uri))
        }

    }
    //Infla o layout, para posteriormente associar aos componentes
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(c).inflate(R.layout.itemlista,parent,false)
        return ViewHolder(view)
    }

    //retorna o tamanho da lista
    override fun getItemCount(): Int = items.size

    //associa os valores dos itens aos textviews
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(item: ItemRSS) {
          itemView.item_titulo.text=item.title
          itemView.item_data.text=item.pubDate
        }
    }
}
