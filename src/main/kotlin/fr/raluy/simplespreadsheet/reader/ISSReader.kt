package fr.raluy.simplespreadsheet.reader

import kotlin.reflect.KClass

interface ISSReader {
    fun readToArray(): Array<Array<String?>>
    fun readToArray(spreadsheet: String): Array<Array<String?>>

    fun readToCollection(): List<List<String?>>
    fun readToCollection(spreadsheet: String): List<List<String?>>

    fun <T : Any> readToObjects(kClass: KClass<T>): List<T>
    fun <T : Any> readToObjects(spreadsheet: String, kClass: KClass<T>): List<T>
}
