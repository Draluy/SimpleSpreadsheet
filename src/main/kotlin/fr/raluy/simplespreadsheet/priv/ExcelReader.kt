package fr.raluy.simplespreadsheet.priv

import fr.raluy.simplespreadsheet.reader.ISSReader
import org.apache.poi.ss.usermodel.*
import java.nio.file.Path
import kotlin.reflect.KClass

class ExcelReader(val path: Path, var skipHeaders: Boolean) : ISSReader {

    override fun readToArray(): Array<Array<String?>> {
        val workbook = WorkbookFactory.create(path.toFile())
        return readToArray(workbook.getSheetAt(0))
    }

    override fun readToArray(spreadsheet: String): Array<Array<String?>> {
        val workbook = WorkbookFactory.create(path.toFile())
        return readToArray(workbook.getSheet(spreadsheet))
    }

    fun readToArray(spreadsheet: Sheet): Array<Array<String?>> {
        val formatter = DataFormatter()
        val result = mutableListOf<Array<String?>>()
        for (row: Row in spreadsheet) {

            val rowResult: MutableList<String> = mutableListOf()
            for (cell: Cell in row) {
                val text: String = formatter.formatCellValue(cell)
                rowResult.add(text);
            }
            result.add(rowResult.toTypedArray())
        }
        return result.toTypedArray()
    }

    override fun readToCollection(): List<List<String>> {
        TODO("Not yet implemented")
    }

    override fun readToCollection(spreadsheet: String): List<List<String>> {
        TODO("Not yet implemented")
    }

    override fun <T : Any> readToObjects(kClass: KClass<T>): List<T> {
        TODO("Not yet implemented")
    }

    override fun <T : Any> readToObjects(spreadsheet: String, kClass: KClass<T>): List<T> {
        TODO("Not yet implemented")
    }

}
