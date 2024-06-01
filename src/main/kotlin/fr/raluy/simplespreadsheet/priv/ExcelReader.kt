package fr.raluy.simplespreadsheet.priv

import fr.raluy.simplespreadsheet.reader.ISSReader
import org.apache.poi.ss.usermodel.*
import java.io.BufferedInputStream
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import kotlin.reflect.KClass


class ExcelReader(private val iStream: InputStream, var skipHeaders: Boolean) : ISSReader {

    constructor(path: Path, skipHeaders: Boolean) : this(BufferedInputStream(Files.newInputStream(path)), skipHeaders)

    private val evaluator: FormulaEvaluator

    private val workbook: Workbook = WorkbookFactory.create(iStream)

    init {
        this.evaluator = workbook.creationHelper.createFormulaEvaluator()
    }

    private fun readDataToStream(sheetAt: Sheet, evaluator: FormulaEvaluator) = sequence {

        for (row: Row in sheetAt) {
            if (skipHeaders) {
                skipHeaders = false
                continue;
            }

            val rowResult: MutableList<String?> = mutableListOf()
            for (cell: Cell in row) {

                rowResult.add(
                    evaluateCell(evaluator, cell, cell.cellType)?.trim()
                )
            }

            if (lineIsEmpty(rowResult)) {
                continue
            }

            // Excel is annoying in that empty cells can be actually empty, or be "empty", with a type empty, and the two are different. So trim extra nulls at the end to avoid that
            val trimmedResult = trimEndNulls(rowResult)

            yield(trimmedResult.toTypedArray())
        }
    }

    private fun trimEndNulls(rowResult: MutableList<String?>): List<String?> {
        val itr = rowResult.listIterator(rowResult.size)
        val result = mutableListOf<String?>();
        var firstNonNullValueFound = false;
        while (itr.hasPrevious()) {
            val value = itr.previous()
            if (value == null && !firstNonNullValueFound) {
                continue;
            } else {
                firstNonNullValueFound = true
            }
            result.add(value);
        }
        return result.reversed()
    }

    private fun evaluateCell(
        evaluator: FormulaEvaluator,
        cell: Cell,
        celltype: CellType
    ): String? {
        val fmt = DataFormatter()

        return when (celltype) {

            CellType.BOOLEAN -> cell.booleanCellValue.toString()
            CellType.NUMERIC -> if (DateUtil.isCellDateFormatted(cell)) {
                cell.localDateTimeCellValue.toString()
            } else fmt.formatCellValue(cell, evaluator)

            CellType.STRING -> cell.stringCellValue.toString()
            CellType._NONE -> cell.stringCellValue
            CellType.FORMULA -> evaluateCell(evaluator, cell, evaluator.evaluateFormulaCell(cell))
            CellType.BLANK -> null
            CellType.ERROR -> cell.errorCellValue.toString()
        }
    }

    private fun lineIsEmpty(line: MutableList<String?>): Boolean {
        return line.all { it.isNullOrEmpty() }
    }

    override fun readToArray(): Array<Array<String?>> = readSheetToArray(workbook.getSheetAt(0))

    override fun readToArray(spreadsheet: String) = readSheetToArray(workbook.getSheet(spreadsheet))

    private fun readSheetToArray(spreadsheet: Sheet): Array<Array<String?>> {
        val result = mutableListOf<Array<String?>>()
        for (line in readDataToStream(spreadsheet, evaluator).asIterable()) {
            result.add(line)
        }
        return result.toTypedArray()
    }

    override fun readToCollection() = readSheetToCollection(workbook.getSheetAt(0))

    private fun readSheetToCollection(spreadsheet: Sheet): List<List<String?>> {
        val result = mutableListOf<List<String?>>()
        for (line in readDataToStream(spreadsheet, evaluator).asIterable()) {
            result.add(listOf(* line))
        }
        return result
    }

    override fun readToCollection(spreadsheet: String) = readSheetToCollection(workbook.getSheet(spreadsheet))

    override fun <T : Any> readToObjects(kClass: KClass<T>) = readSheetToObjects(kClass, workbook.getSheetAt(0))

    private fun <T : Any> readSheetToObjects(kClass: KClass<T>, spreadsheet: Sheet): List<T> {
        val result = mutableListOf<T>()
        for (line in readDataToStream(spreadsheet, evaluator).asIterable()) {
            val objectCreated = KotlinObjectInstantiator<T>().createObject(kClass, line)
            result.add(objectCreated)
        }

        return result
    }

    override fun <T : Any> readToObjects(spreadsheet: String, kClass: KClass<T>) = readSheetToObjects(kClass, workbook.getSheet(spreadsheet))

    override fun <T : Any> readToObjects(jClass: Class<T>) = readSheetToObjects(jClass, workbook.getSheetAt(0))

    private fun <T : Any> readSheetToObjects(jClass: Class<T>, spreadsheet: Sheet): List<T> {
        val result = mutableListOf<T>()
        for (line in readDataToStream(spreadsheet, evaluator).asIterable()) {
            val objectCreated = JavaObjectInstantiator<T>().createObject(jClass, line)
            result.add(objectCreated)
        }

        return result
    }

    override fun <T : Any> readToObjects(spreadsheet: String, jClass: Class<T>) = readSheetToObjects(jClass, workbook.getSheet(spreadsheet))

}
