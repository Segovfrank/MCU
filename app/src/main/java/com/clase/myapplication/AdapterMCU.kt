package com.clase.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AdapterMCU(var heroes: List<Hero> = listOf()) : RecyclerView.Adapter<AdapterMCU.ViewHolder>() {
    class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val nameView: TextView = mView.findViewById(R.id.name)
        val noteView: TextView = mView.findViewById(R.id.notes)
        val heroImage: ImageView = mView.findViewById(R.id.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mcu, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return heroes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameView.text = heroes[position].name
        holder.noteView.text = heroes[position].notes
        heroes[position].thumbnail?.let{
            Glide.with(holder.heroImage)
                .load(it)
                .into(holder.heroImage)
        }
    }

    fun setData(dataHeroes: MutableList<Hero>) {
        heroes = dataHeroes
        notifyDataSetChanged()
    }
}