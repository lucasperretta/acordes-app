package com.osito.acordes

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.osito.acordes.databinding.ActivityEditBinding
import java.io.File

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("file")) {
            val file = intent.getStringExtra("file")
            binding.title.setText(file)
            binding.content.setText(readFromFile(file!!))
        }

        if (intent.hasExtra(Intent.EXTRA_STREAM)) {
            val inputStream = contentResolver.openInputStream(intent.getParcelableExtra(Intent.EXTRA_STREAM)!!)
            val text = inputStream!!.bufferedReader().use { it.readText() }
            binding.content.setText(text)
        }

        binding.save.setOnClickListener {
            Toast.makeText(this, "Archivo Guardado", Toast.LENGTH_SHORT).show()
            var text = binding.content.text.toString()
            text = text
                .replace("&nbsp!", " ")
                .replace("\r", "")
            writeToFile(
                binding.title.text.toString(),
                text
            )
            finish()
        }

        binding.delete.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setMessage("Seguro que quiere eliminar el archivo?")
                setPositiveButton("Si") { _, _ ->
                    Toast.makeText(this@EditActivity, "Archivo Eliminado", Toast.LENGTH_SHORT).show()
                    File(filesDir.absolutePath + "/" + binding.title.text).delete()
                    finish()
                }
                setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            }
                .create()
                .show()
        }
    }

}