package fr.raluy.simplespreadsheet.reader

import fr.raluy.simplespreadsheet.priv.CSVReader
import fr.raluy.simplespreadsheet.priv.ExcelReader
import fr.raluy.simplespreadsheet.priv.FileType
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import kotlin.reflect.KClass


class SSReader(private var iStream: InputStream, private val fileName: String) : ISSReader {

    constructor(path: Path, fileName: String) : this(BufferedInputStream(Files.newInputStream(path)), fileName)
    constructor(bytes: ByteArray, fileName: String) : this(ByteArrayInputStream(bytes), fileName)

    var skipHeaders: Boolean = false;

    fun skipHeaders(): SSReader {
        skipHeaders = true
        return this
    }

    override fun readToArray(): Array<Array<String?>> {

        val fileType: FileType = FileType.guess(iStream, fileName)
        return when (fileType) {
            FileType.XLS, FileType.XLSX -> ExcelReader(iStream, skipHeaders).readToArray()
            FileType.CSV -> CSVReader(iStream, skipHeaders).readToArray()
        }
    }

    override fun readToArray(spreadsheet: String): Array<Array<String?>> {
        val fileType: FileType = FileType.guess(iStream, fileName)

        return when (fileType) {
            FileType.XLS, FileType.XLSX -> ExcelReader(iStream, skipHeaders).readToArray(spreadsheet)
            FileType.CSV -> CSVReader(iStream, skipHeaders).readToArray(spreadsheet)
        }
    }

    override fun readToCollection(): List<List<String?>> {
        val fileType: FileType = FileType.guess(iStream, fileName)

        return when (fileType) {
            FileType.XLS, FileType.XLSX -> ExcelReader(iStream, skipHeaders).readToCollection()
            FileType.CSV -> CSVReader(iStream, skipHeaders).readToCollection()
        }
    }

    override fun readToCollection(spreadsheet: String): List<List<String?>> {
        val fileType: FileType = FileType.guess(iStream, fileName)

        return when (fileType) {
            FileType.XLS, FileType.XLSX -> ExcelReader(iStream, skipHeaders).readToCollection(spreadsheet)
            FileType.CSV -> CSVReader(iStream, skipHeaders).readToCollection(spreadsheet)
        }
    }

    override fun <T : Any> readToObjects(kClass: KClass<T>): List<T> {
        val fileType: FileType = FileType.guess(iStream, fileName)

        return when (fileType) {
            FileType.XLS, FileType.XLSX -> ExcelReader(iStream, skipHeaders).readToObjects(kClass)
            FileType.CSV -> CSVReader(iStream, skipHeaders).readToObjects(kClass)
        }
    }

    override fun <T : Any> readToObjects(jClass: Class<T>): List<T> {
        val fileType: FileType = FileType.guess(iStream, fileName)

        return when (fileType) {
            FileType.XLS, FileType.XLSX -> ExcelReader(iStream, skipHeaders).readToObjects(jClass)
            FileType.CSV -> CSVReader(iStream, skipHeaders).readToObjects(jClass)
        }
    }

    override fun <T : Any> readToObjects(spreadsheet: String, jClass: Class<T>): List<T> {
        val fileType: FileType = FileType.guess(iStream, fileName)

        return when (fileType) {
            FileType.XLS, FileType.XLSX -> ExcelReader(iStream, skipHeaders).readToObjects(spreadsheet, jClass)
            FileType.CSV -> CSVReader(iStream, skipHeaders).readToObjects(spreadsheet, jClass)
        }
    }

    override fun <T : Any> readToObjects(spreadsheet: String, kClass: KClass<T>): List<T> {
        val fileType: FileType = FileType.guess(iStream, fileName)

        return when (fileType) {
            FileType.XLS, FileType.XLSX -> ExcelReader(iStream, skipHeaders).readToObjects(spreadsheet, kClass)
            FileType.CSV -> CSVReader(iStream, skipHeaders).readToObjects(spreadsheet, kClass)
        }
    }
}
