package fr.raluy.simplespreadsheet.reader

import fr.raluy.simplespreadsheet.priv.CSVReader
import fr.raluy.simplespreadsheet.priv.ExcelReader
import fr.raluy.simplespreadsheet.priv.FileType
import java.nio.file.Path
import kotlin.reflect.KClass


class SSReader(val path: Path) : ISSReader {

    var skipHeaders: Boolean  = false;

    fun skipHeaders() : SSReader {
        skipHeaders = true
        return this
    }

    override fun readToArray(): Array<Array<String?>> {
        val fileType: FileType = FileType.guess(path)

        return when(fileType) {
            FileType.XLS, FileType.XLSX -> ExcelReader(path, skipHeaders).readToArray()
            FileType.CSV -> CSVReader(path, skipHeaders).readToArray()
        }
    }

    override fun readToArray(spreadsheet: String): Array<Array<String?>> {
        val fileType: FileType = FileType.guess(path)

        return when(fileType) {
            FileType.XLS, FileType.XLSX -> ExcelReader(path, skipHeaders).readToArray(spreadsheet)
            FileType.CSV -> CSVReader(path, skipHeaders).readToArray(spreadsheet)
        }
    }

    override fun readToCollection(): List<List<String?>> {
        val fileType: FileType = FileType.guess(path)

        return when(fileType) {
            FileType.XLS, FileType.XLSX -> ExcelReader(path, skipHeaders).readToCollection()
            FileType.CSV -> CSVReader(path, skipHeaders).readToCollection()
        }
    }

    override fun readToCollection(spreadsheet: String): List<List<String?>> {
        val fileType: FileType = FileType.guess(path)

        return when(fileType) {
            FileType.XLS, FileType.XLSX -> ExcelReader(path, skipHeaders).readToCollection(spreadsheet)
            FileType.CSV -> CSVReader(path, skipHeaders).readToCollection(spreadsheet)
        }
    }

    override fun <T : Any> readToObjects(kClass: KClass<T>): List<T> {
        val fileType: FileType = FileType.guess(path)

        return when(fileType) {
            FileType.XLS, FileType.XLSX -> ExcelReader(path, skipHeaders).readToObjects(kClass)
            FileType.CSV -> CSVReader(path, skipHeaders).readToObjects(kClass)
        }
    }

    override fun <T : Any> readToObjects(jClass: Class<T>): List<T> {
        val fileType: FileType = FileType.guess(path)

        return when(fileType) {
            FileType.XLS, FileType.XLSX -> ExcelReader(path, skipHeaders).readToObjects(jClass)
            FileType.CSV -> CSVReader(path, skipHeaders).readToObjects(jClass)
        }
    }

    override fun <T : Any> readToObjects(spreadsheet: String, jClass: Class<T>): List<T> {
        val fileType: FileType = FileType.guess(path)

        return when(fileType) {
            FileType.XLS, FileType.XLSX -> ExcelReader(path, skipHeaders).readToObjects(spreadsheet, jClass)
            FileType.CSV -> CSVReader(path, skipHeaders).readToObjects(spreadsheet, jClass)
        }
    }

    override fun <T : Any> readToObjects(spreadsheet: String, kClass: KClass<T>): List<T> {
        val fileType: FileType = FileType.guess(path)

        return when(fileType) {
            FileType.XLS, FileType.XLSX -> ExcelReader(path, skipHeaders).readToObjects(spreadsheet, kClass)
            FileType.CSV -> CSVReader(path, skipHeaders).readToObjects(spreadsheet, kClass)
        }
    }
}