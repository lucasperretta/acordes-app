package com.osito.acordes

import android.content.Context
import android.util.Log
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import java.io.*
import java.lang.StringBuilder

fun Context.getColorHex(@ColorRes id: Int) = "%x".format(ContextCompat.getColor(this, id)).drop(2)

fun Context.readFromFile(name: String): String {
    if (true) {
        return File(filesDir.absolutePath + "/" + name).readText(Charsets.UTF_8)
    }

    var ret = ""
    try {
        val inputStream: InputStream = openFileInput(name)
        val inputStreamReader = InputStreamReader(inputStream, Charsets.UTF_8)
        val bufferedReader = BufferedReader(inputStreamReader)
        var receiveString: String?
        val stringBuilder = StringBuilder()
        while (bufferedReader.readLine().also { receiveString = it } != null) {
            stringBuilder.append("\n").append(receiveString)
        }
        inputStream.close()
        ret = stringBuilder.toString()
    } catch (e: FileNotFoundException) {
        Log.e("login activity", "File not found: " + e.toString())
    } catch (e: IOException) {
        Log.e("login activity", "Can not read file: " + e.toString())
    }

    return ret
}

fun Context.writeToFile(name: String, data: String) {
    try {
        val outputStreamWriter =
            OutputStreamWriter(openFileOutput(name, Context.MODE_PRIVATE))
        outputStreamWriter.write(data)
        outputStreamWriter.close()
    } catch (e: IOException) {
        Log.e("Exception", "File write failed: $e")
    }
}