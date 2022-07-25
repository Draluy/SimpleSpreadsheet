import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import java.nio.file.Path
import java.nio.file.Paths

internal class SSReaderTest {


    @Nested
    @DisplayName("Simple naive tests")
    inner class SimpleTests {

        private var fileName = "/spreadsheet/WithTable.xlsx"

        @Test
        fun readToArray() {
            val path: Path = Paths.get(javaClass.getResource(fileName).path)
            val array = SSReader(path).readToArray()

            assertThat(array[0][0]).isEqualTo("a")
            assertThat(array[1][0]).isEqualTo("b")
            assertThat(array[0][1]).isEqualTo("1")
            assertThat(array[1][1]).isEqualTo("2")
        }

    }
}