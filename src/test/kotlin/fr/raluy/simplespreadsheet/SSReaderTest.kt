package fr.raluy.simplespreadsheet

import fr.raluy.simplespreadsheet.priv.CSVReader.Companion.CSV_ERR_SHEET_PROVIDED
import fr.raluy.simplespreadsheet.priv.ObjectInstantiator
import fr.raluy.simplespreadsheet.reader.SSReader
import fr.raluy.simplespreadsheet.testObjects.Address
import fr.raluy.simplespreadsheet.testObjects.AirTravel
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.reflect.KClass

internal class SSReaderTest {


    @Nested
    @DisplayName("Simple naive reads")
    inner class SimpleReadTests {

        @Test
        fun readXlsx() {
            val path: Path = getResource("/xls/WithTable.xlsx")
            val array = SSReader(path).readToArray()

            assertThat(array[0][0]).isEqualTo("a")
            assertThat(array[0][1]).isEqualTo("b")
            assertThat(array[1][0]).isEqualTo("1")
            assertThat(array[1][1]).isEqualTo("2")
        }

        @Test
        fun readCsv() {
            val path: Path = getResource("/csv/addresses.csv")
            val array = SSReader(path).readToArray()

            assertThat(array[0][0]).isEqualTo("John")
            assertThat(array[0][1]).isEqualTo("Doe")
            assertThat(array[1][0]).isEqualTo("Jack")
            assertThat(array[1][1]).isEqualTo("McGinnis")
        }

        @Test
        fun readExcel() {
            val path: Path = getResource("/xlsx/sample1.xlsx")
            val array = SSReader(path).readToArray()

            assertThat(array[0][0]).isEqualTo("Postcode")
            assertThat(array[0][1]).isEqualTo("Sales_Rep_ID")
            assertThat(array[1][0]).isEqualTo("2121")
            assertThat(array[1][1]).isEqualTo("456")
        }

    }


    @Nested
    @DisplayName("Read Csv files")
    inner class ReadCsvFiles {

        @Test
        fun checkCsvReadingFailsIfSheetProvided() {
            val path: Path = getResource("/csv/addresses.csv")
            assertThatThrownBy {
                SSReader(path).readToArray("firstSheet")
            }.isInstanceOf(UnsupportedOperationException::class.java)
                .hasMessageContaining(CSV_ERR_SHEET_PROVIDED)
            assertThatThrownBy {
                SSReader(path).readToCollection("firstSheet")
            }.isInstanceOf(UnsupportedOperationException::class.java)
                .hasMessageContaining(CSV_ERR_SHEET_PROVIDED)
            assertThatThrownBy {
                SSReader(path).readToObjects("firstSheet", Address::class)
            }.isInstanceOf(UnsupportedOperationException::class.java)
                .hasMessageContaining(CSV_ERR_SHEET_PROVIDED)
        }

        @Test
        fun readAddresses() {
            val path: Path = getResource("/csv/addresses.csv")
            val array = SSReader(path).readToArray()
            val collection = SSReader(path).readToCollection()
            val objects = SSReader(path).readToObjects(Address::class)

            val expectedArray: Array<Array<String?>> = arrayOf(
                arrayOf("John", "Doe", "120 jefferson st.", "Riverside", "NJ", "08075"),
                arrayOf("Jack", "McGinnis", "220 hobo Av.", "Phila", "PA", "09119"),
                arrayOf("John \"Da Man\"", "Repici", "120 Jefferson St.", "Riverside", "NJ", "08075"),
                arrayOf("Stephen", "Tyler", "7452 Terrace \"At the Plaza\" road", "SomeTown", "SD", "91234"),
                arrayOf(null, "Blankman", null, "SomeTown", "SD", "00298"),
                arrayOf("Joan \"the bone\", Anne", "Jet", "9th, at Terrace plc", "Desert City", "CO", "00123")
            )

            checkResults(array, collection, objects, expectedArray, Address::class)
        }

        @Test
        fun readAirTravel() {
            val path: Path = getResource("/csv/airtravel.csv")
            val array = SSReader(path).readToArray()
            val collection = SSReader(path).readToCollection()
            val objects = SSReader(path).readToObjects(AirTravel::class)

            val expectedArray: Array<Array<String?>> = arrayOf(
                arrayOf("Month", "1958", "1959", "1960"),
                arrayOf("JAN", "340", "360", "417"),
                arrayOf("FEB", "318", "342", "391"),
                arrayOf("MAR", "362", "406", "419"),
                arrayOf("APR", "348", "396", "461"),
                arrayOf("MAY", "363", "420", "472"),
                arrayOf("JUN", "435", "472", "535"),
                arrayOf("JUL", "491", "548", "622"),
                arrayOf("AUG", "505", "559", "606"),
                arrayOf("SEP", "404", "463", "508"),
                arrayOf("OCT", "359", "407", "461"),
                arrayOf("NOV", "310", "362", "390"),
                arrayOf("DEC", "337", "405", "432")
            )

            checkResults(array, collection, objects, expectedArray, AirTravel::class)
        }
    }

    private fun <T : Any> checkResults(
        array: Array<Array<String?>>,
        collection: List<List<String?>>,
        objects: List<T>,
        expectedArray: Array<Array<String?>>,
        kClass: KClass<T>
    ) {
        checkArray(
            array, expectedArray
        )

        checkCollection(
            collection, toList(expectedArray)
        )

        checkObjects(
            objects, toObjects(expectedArray, kClass)
        )
    }

    private fun <T : Any> toObjects(expectedArray: Array<Array<String?>>, x: KClass<T>): List<T> {
        return expectedArray.map { arr ->
            ObjectInstantiator.createObject(x, arr)
        }
    }

    private fun <T : Any> checkObjects(objects: List<T>, toObjects: List<T>) {
        assertThat(objects).usingRecursiveComparison().isEqualTo(toObjects);
    }

    private fun checkArray(array: Array<Array<String?>>, expected: Array<Array<String?>>) {
        assertThat(array).isEqualTo(expected)
    }

    private fun checkCollection(collection: List<List<String?>>, expected: List<List<String?>>) {
        assertThat(collection).isEqualTo(expected)
    }

    private fun toList(expectedArray: Array<Array<String?>>): List<List<String?>> {
        return expectedArray.map { i -> i.toList() }
    }

    private fun getResource(path: String): Path = Paths.get(javaClass.getResource(path)?.path ?: "error")
}