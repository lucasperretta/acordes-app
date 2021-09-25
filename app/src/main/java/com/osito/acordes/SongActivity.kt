package com.osito.acordes

import android.os.Bundle
import android.text.Spanned
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.osito.acordes.databinding.ActivitySongBinding
import java.util.*

class SongActivity : AppCompatActivity() {

    lateinit var binding: ActivitySongBinding
    var play = false
    var accumulator: Float = 0f
    var addValue: Float = 0.75f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Fullscren.....
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        // SeekBar.....
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.speedTextView.text = progress.toString()
                addValue = progress * 0.05f
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        binding.seekBar.progress = 20

        // Play Button.....
        binding.play.setOnClickListener {
            play = !play
            binding.play.setImageResource(if (play) R.drawable.ic_baseline_pause_24 else R.drawable.ic_baseline_play_arrow_24)
        }

        // Text.....
        val text = readFromFile(intent.getStringExtra("file")!!)

        binding.root.post {
            binding.textView.text = processText(removeUTF8BOM(text), countCharacters())
        }

        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (!play) return

                accumulator += addValue

                while (accumulator > 1) {
                    binding.scrollView.scrollBy(0, 1)
                    accumulator -= 1
                }
            }
        }, 0, (1000/60).toLong())

    }

    private fun removeUTF8BOM(string: String) = if (string.startsWith("\uFEFF")) { string.substring(1) } else string

    private fun processText(text: String, maxCharactersInLine: Int): Spanned {
        val color = getColorHex(R.color.color)
        var lines: MutableList<String?> = text.replace("\r", "").split("\n").toMutableList()
        lines = lines.mapIndexed { index, it ->
            if (it == null) return@mapIndexed null

            var line = it.replace(" ", "&nbsp;")
            if (it.startsWith("[") && it.endsWith("]")) {
                return@mapIndexed "<font color='#09AE20'><b>$line</b></font>"
            }

            if (!it.startsWith("£") && !it.contains(Regex("\\s{4,}"))) return@mapIndexed line

            // Linea de acordes
            val acordes = it
            val letra = lines[index + 1]

            if (letra!!.length > maxCharactersInLine) {
                val letraLines = letra.chunked(maxCharactersInLine)
                val acordesLines = acordes.chunked(maxCharactersInLine)

                line = ""
                letraLines.forEachIndexed { i, l ->
                    line += "<color><b>${(acordesLines.getOrNull(i) ?: "")}</b></font>"
                    line += "<br>"
                    line += l
                    line += "<br>"
                }

                line = line
                    .replace(" ", "&nbsp;")
                    .replace("<color>", "<font color='#$color'>")

                lines[index + 1] = null

                return@mapIndexed line
            } else {
                return@mapIndexed "<font color='#$color'><b>$line</b></font>"
            }

        } as MutableList<String?>
        lines.removeAll { it == null }

        lines = lines.map { it?.replace("€", "")?.replace("£", "") } as MutableList<String?>

        return HtmlCompat.fromHtml(lines.joinToString("<br>"), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun countCharacters(): Int {
        val viewWidth = binding.textView.width
        val characterWidth = binding.textView.paint.measureText("A")

        return (viewWidth / characterWidth).toInt()
    }

}