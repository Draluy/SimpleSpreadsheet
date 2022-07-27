import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import java.nio.file.Path
import java.nio.file.Paths

internal class SSReaderTest {


    @Nested
    @DisplayName("Simple naive reads")
    inner class SimpleReadTests {

        @Test
        fun readXlsx() {
            val path: Path = Paths.get(javaClass.getResource("/xls/WithTable.xlsx").path)
            val array = SSReader(path).readToArray()

            assertThat(array[0][0]).isEqualTo("a")
            assertThat(array[0][1]).isEqualTo("b")
            assertThat(array[1][0]).isEqualTo("1")
            assertThat(array[1][1]).isEqualTo("2")
        }

        @Test
        fun readCsv() {
            val path: Path = Paths.get(javaClass.getResource("/csv/addresses.csv").path)
            val array = SSReader(path).readToArray()

            assertThat(array[0][0]).isEqualTo("John")
            assertThat(array[0][1]).isEqualTo("Doe")
            assertThat(array[1][0]).isEqualTo("Jack")
            assertThat(array[1][1]).isEqualTo("McGinnis")
        }

        @Test
        fun readExcel() {
            val path: Path = Paths.get(javaClass.getResource("/xlsx/sample1.xlsx").path)
            val array = SSReader(path).readToArray()

            assertThat(array[0][0]).isEqualTo("Postcode")
            assertThat(array[0][1]).isEqualTo("Sales_Rep_ID")
            assertThat(array[1][0]).isEqualTo("2121")
            assertThat(array[1][1]).isEqualTo("456")
        }

    }
}