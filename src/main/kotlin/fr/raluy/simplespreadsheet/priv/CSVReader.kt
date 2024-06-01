package fr.raluy.simplespreadsheet.priv

import fr.raluy.simplespreadsheet.reader.ISSReader
import java.io.InputStream
import kotlin.reflect.KClass


class CSVReader(val iStream: InputStream, var skipHeaders: Boolean) : ISSReader {

    companion object {
        const val CSV_ERR_SHEET_PROVIDED = "This is a CSV file, there are no sheets to chose from."
    }

    private fun readDataToStream() = sequence {
        iStream.bufferedReader(Charsets.UTF_8).use { reader ->
            com.opencsv.CSVReader(reader).use { csvReader ->
                var line: Array<String?>?
                while (csvReader.readNext().also { line = it } != null) {
                    if (skipHeaders) {
                        skipHeaders = false
                        continue;
                    }

                    if (lineIsEmpty(line)) {
                        continue
                    }

                    line?.let {
                        val trimmedArray = it.map { it2 ->
                            if (it2!!.isBlank()) null else it2.trim()
                        }
                        yield(trimmedArray.toTypedArray())
                    }
                }
            }
        }
    }

    private fun lineIsEmpty(line: Array<String?>?): Boolean {
        return line != null && line.all { it.isNullOrEmpty() }
    }

    override fun readToArray(): Array<Array<String?>> {
        val result = mutableListOf<Array<String?>>()
        for (line in readDataToStream().asIterable()) {
            result.add(line)
        }

        return result.toTypedArray()
    }

    override fun readToArray(spreadsheet: String): Array<Array<String?>> {
        throw UnsupportedOperationException(CSV_ERR_SHEET_PROVIDED)
    }

    override fun readToCollection(): List<List<String?>> {
        val result = mutableListOf<List<String?>>()
        for (line in readDataToStream().asIterable()) {
            result.add(listOf(* line))
        }

        return result
    }

    override fun readToCollection(spreadsheet: String): List<List<String?>> {
        throw UnsupportedOperationException(CSV_ERR_SHEET_PROVIDED)
    }


    override fun <T : Any> readToObjects(kClass: KClass<T>): List<T> {
        val result = mutableListOf<T>()
        for (line in readDataToStream().asIterable()) {
            val objectCreated = KotlinObjectInstantiator<T>().createObject(kClass, line)
            result.add(objectCreated)
        }

        return result
    }

    override fun <T : Any> readToObjects(spreadsheet: String, kClass: KClass<T>): List<T> {
        throw UnsupportedOperationException(CSV_ERR_SHEET_PROVIDED)
    }

    override fun <T : Any> readToObjects(jClass: Class<T>): List<T> {
        val result = mutableListOf<T>()
        for (line in readDataToStream().asIterable()) {
            val objectCreated = JavaObjectInstantiator<T>().createObject(jClass, line)
            result.add(objectCreated)
        }

        return result
    }

    override fun <T : Any> readToObjects(spreadsheet: String, jClass: Class<T>): List<T> {
        throw UnsupportedOperationException(CSV_ERR_SHEET_PROVIDED)
    }
}
