package com.osito.acordes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.osito.acordes.databinding.ActivityHomeBinding
import com.osito.acordes.databinding.ItemSongBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var fileNames: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()

        fileNames = filesDir.listFiles().map { it.name }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = RecyclerViewAdapter()

        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(this, EditActivity::class.java))
        }
    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(layoutInflater.inflate(R.layout.item_song, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.binding.textView.text = fileNames[position]
            holder.binding.root.setOnClickListener {
                startActivity(Intent(this@HomeActivity, SongActivity::class.java).putExtra("file", fileNames[position]))
            }
            holder.binding.imageView.setOnClickListener {
                startActivity(Intent(this@HomeActivity, EditActivity::class.java).putExtra("file", fileNames[position]))
            }
        }

        override fun getItemCount() = fileNames.size

        inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
            val binding = ItemSongBinding.bind(itemView)
        }

    }

}