package fr.raluy.simplespreadsheet.priv

import java.nio.file.Files
import java.nio.file.Path

enum class FileType(val mimeTypes: Array<String>) {
    CSV(arrayOf("text/csv")),
    XLS(
        arrayOf(
            "application/excel",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        )
    ),
    XLSX(
        arrayOf(
            "application/x-excel",
            "application/x-msexcel"
        )
    );

    companion object {
        fun guess(path: Path): FileType {
            val probeContentType : String= Files.probeContentType(path)
            if (CSV.mimeTypes.contains(probeContentType)){
                return CSV;
            }
            if (XLS.mimeTypes.contains(probeContentType)){
                return XLS;
            }
            if (XLSX.mimeTypes.contains(probeContentType)){
                return XLSX;
            }
            throw IllegalArgumentException("The file type is unknown: $probeContentType")
        }
    }
}