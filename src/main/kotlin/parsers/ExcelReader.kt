package parsers

import ISSReader
import org.apache.poi.ss.usermodel.*
import java.nio.file.Path

class ExcelReader(val path: Path) : ISSReader {

    override fun readToArray(): Array<Array<String>> {
        val workbook = WorkbookFactory.create(path.toFile())
        return readToArray(workbook.getSheetAt(0))
    }

    override fun readToArray(spreadsheet: String): Array<Array<String>> {
        val workbook = WorkbookFactory.create(path.toFile())
        return readToArray(workbook.getSheet(spreadsheet))
    }

    fun readToArray(spreadsheet: Sheet): Array<Array<String>> {
        val formatter = DataFormatter()
        val result  = mutableListOf<Array<String>>()
        for (row: Row in spreadsheet) {

            val rowResult : MutableList<String> = mutableListOf()
            for (cell: Cell in row) {
                val text: String = formatter.formatCellValue(cell)
                rowResult.add(text);
            }
            result.add(rowResult.toTypedArray())
        }
        return result.toTypedArray()
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
