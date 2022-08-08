package fr.raluy.simplespreadsheet.priv

import fr.raluy.simplespreadsheet.reader.ISSReader
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import java.io.FileReader
import java.io.Reader
import java.nio.file.Path
import kotlin.reflect.KClass


class CSVReader(val path: Path, var skipHeaders: Boolean) : ISSReader {

    companion object {
        const val CSV_ERR_SHEET_PROVIDED = "This is a CSV file, there are no sheets to chose from."
    }

    override fun readToArray(): Array<Array<String?>> {
        val inReader: Reader = FileReader(path.toFile())
        val records: Iterable<CSVRecord> = CSVFormat.DEFAULT.parse(inReader)

        val result = mutableListOf<Array<String?>>()
        for (record in records) {
            if (skipHeaders) {
                skipHeaders = false
                continue;
            }
            val rowResult = mutableListOf<String?>()
            for (cell in record) {
                rowResult.add(if (cell.isEmpty()) null else cell.trim())
            }
            result.add(rowResult.toTypedArray())
        }
        return result.toTypedArray()
    }

    override fun readToArray(spreadsheet: String): Array<Array<String?>> {
        throw UnsupportedOperationException(CSV_ERR_SHEET_PROVIDED)
    }

    override fun readToCollection(): List<List<String?>> {
        val inReader: Reader = FileReader(path.toFile())
        val records: Iterable<CSVRecord> = CSVFormat.DEFAULT.parse(inReader)

        val result = mutableListOf<List<String?>>()
        for (record in records) {
            if (skipHeaders) {
                skipHeaders = false
                continue;
            }
            val rowResult = mutableListOf<String?>()
            for (cell in record) {
                rowResult.add(if (cell.isEmpty()) null else cell.trim())
            }
            result.add(rowResult)
        }
        return result.toList()
    }

    override fun readToCollection(spreadsheet: String): List<List<String?>> {
        throw UnsupportedOperationException(CSV_ERR_SHEET_PROVIDED)
    }


    override fun <T : Any> readToObjects(kClass: KClass<T>): List<T> {
        val inReader: Reader = FileReader(path.toFile())
        val records: Iterable<CSVRecord> = CSVFormat.DEFAULT.parse(inReader)

        val result = mutableListOf<T>()
        for (record in records) {
            if (skipHeaders) {
                skipHeaders = false
                continue;
            }
            var params: Array<String?> = record.map { if (it.isEmpty()) null else it.trim() }.toTypedArray()
            result.add(ObjectInstantiator.createObject(kClass, params))
        }
        return result
    }

    override fun <T : Any> readToObjects(spreadsheet: String, kClass: KClass<T>): List<T> {
        throw UnsupportedOperationException(CSV_ERR_SHEET_PROVIDED)
    }
}