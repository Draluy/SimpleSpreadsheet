package parsers

import ISSReader
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import java.io.FileReader
import java.io.Reader
import java.nio.file.Path

class CSVReader(val path: Path) : ISSReader {

    override fun readToArray(): Array<Array<String>> {
        val inReader: Reader = FileReader(path.toFile())
        val records: Iterable<CSVRecord> = CSVFormat.DEFAULT.parse(inReader)

        val result = mutableListOf<Array<String>>()
        for (record in records) {
            val rowResult  = mutableListOf<String>()
            for (cell in record) {
                rowResult.add(cell)
            }
            result.add(rowResult.toTypedArray())
        }
        return result.toTypedArray()
    }

    override fun readToArray(spreadsheet: String): Array<Array<String>> {
        TODO("Not yet implemented")
    }

    override fun readToCollection(): Collection<Collection<String>> {
        TODO("Not yet implemented")
    }

    override fun readToCollection(spreadsheet: String): Collection<Collection<String>> {
        TODO("Not yet implemented")
    }

    override fun <T> readToObjects(): Collection<T> {
        TODO("Not yet implemented")
    }

    override fun <T> readToObjects(spreadsheet: String): Collection<T> {
        TODO("Not yet implemented")
    }
}