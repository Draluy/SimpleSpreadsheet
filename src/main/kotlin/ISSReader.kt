interface ISSReader {
    fun readToArray(): Array<Array<String>>
    fun readToArray(spreadsheet: String): Array<Array<String>>

    fun readToCollection(): Collection<Collection<String>>
    fun readToCollection(spreadsheet: String): Collection<Collection<String>>

    fun <T> readToObjects(): Collection<T>
    fun <T> readToObjects(spreadsheet: String): Collection<T>
}
