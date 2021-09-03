package com.vad.bashexe.bashreader

import java.io.File

class BashReader {

    fun bufferReadFile(file: String) = File(file).bufferedReader().readLine().toString()

}