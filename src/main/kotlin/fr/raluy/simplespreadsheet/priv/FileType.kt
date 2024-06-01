package fr.raluy.simplespreadsheet.priv

import org.apache.tika.Tika
import java.io.InputStream
import java.net.URLConnection


enum class FileType(val mimeTypes: Array<String>) {

    CSV(arrayOf("text/csv", "text/plain")),
    XLS(
        arrayOf(
            "application/excel",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/x-tika-msoffice"
        )
    ),
    XLSX(
        arrayOf(
            "application/x-tika-ooxml",
            "application/x-excel",
            "application/x-msexcel"
        )
    );

    companion object {
        private val tika = Tika()
        fun guess(iStream: InputStream, fileName: String): FileType {
            val typeStr = tika.detect(iStream, fileName)
            if (CSV.mimeTypes.contains(typeStr)){
                return CSV;
            }
            if (XLS.mimeTypes.contains(typeStr)){
                return XLS;
            }
            if (XLSX.mimeTypes.contains(typeStr)){
                return XLSX;
            }
            throw IllegalArgumentException("The file type is unknown: $typeStr")
        }
    }
}
