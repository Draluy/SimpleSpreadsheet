package fr.raluy.simplespreadsheet.reader

import kotlin.reflect.KClass

interface ISSReader {
    fun readToArray(): Array<Array<String?>>
    fun readToArray(spreadsheet: String): Array<Array<String?>>

    fun readToCollection(): List<List<String?>>
    fun readToCollection(spreadsheet: String): List<List<String?>>

    /**
     * This project will try to create the objects by passing them strings.
     * So you can either provide an object:
     * - that has a constructor accepting only strings as parameters
     * - that has a default constructor, and that has public parameters that are Strings (parameters will be set  using reflection)
     *
     * If any data is missing in the file read, nulls will be passed instead, so make sure the relevant fields are nullable in that case.
     */
    fun <T : Any> readToObjects(kClass: KClass<T>): List<T>
    fun <T : Any> readToObjects(spreadsheet: String, kClass: KClass<T>): List<T>
    fun <T : Any> readToObjects(jClass: Class<T>): List<T>
    fun <T : Any> readToObjects(spreadsheet: String, jClass: Class<T>): List<T>
}
