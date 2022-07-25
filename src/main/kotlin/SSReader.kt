import java.nio.file.Path

class SSReader (val path : Path): ISSReader{

    override fun readToArray(): Array<Array<String>> {
        TODO("Not yet implemented")
    }

    override fun readToArray(spreadsheet: String): Array<Array<String>> {
        TODO("Not yet implemented")
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