package fr.raluy.simplespreadsheet

import fr.raluy.simplespreadsheet.priv.CSVReader.Companion.CSV_ERR_SHEET_PROVIDED
import fr.raluy.simplespreadsheet.priv.ObjectInstantiator
import fr.raluy.simplespreadsheet.reader.SSReader
import fr.raluy.simplespreadsheet.testObjects.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.test.fail

internal class SSReaderTest {


    @Nested
    @DisplayName("Simple naive reads")
    inner class SimpleReadTests {

        @Test
        fun readXlsx() {
            val path: Path = getResource("/xlsx/WithTable.xlsx")
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
            val path: Path = getResource("/xls/XRefCalc.xls")
            val array = SSReader(path).readToArray()

            assertThat(array[0][0]).isEqualTo("Quantity")
            assertThat(array[0][1]).isEqualTo("PartNumber")
            assertThat(array[1][0]).isEqualTo("2")
            assertThat(array[1][1]).isEqualTo("x123")
        }

    }


    @Nested
    @DisplayName("Read Xlsx files")
    inner class ReadXlsxFiles {
        @Test
        fun read1_NoIden() {
            val path: Path = getResource("/xlsx/1_NoIden.xlsx")
            val array = SSReader(path).readToArray()
            val collection = SSReader(path).readToCollection()
            val objects = SSReader(path).readToObjects(GenericLine::class)

            val expectedArray: Array<Array<String?>> = arrayOf(
                arrayOf("가나다"),
                arrayOf("하천관리과", "서울 재난 체험관 운영", "치수관리"),
                arrayOf("하천관리과", "하천 위기상황관리시스템 유지관리", "치수총괄"),
                arrayOf("하천관리과", "하천 예경보시설 신설 및 교체", "치수총괄"),
                arrayOf("하천관리과", "하천진출입로 차단시설 설치(구로구 도림천)", "치수총괄"),
                arrayOf("하천관리과", "하천정비사업 시설부대비"),
                arrayOf("하천관리과", "하천사용료징수포상금", "청계천관리", "123456.0", "abcedfgh"),
                arrayOf("하천관리과", "하천 편입토지 보상", "청계천관리")
            )

            checkResults(array, collection, objects, expectedArray, GenericLine::class)
        }

        @Test
        fun read45430() {
            val path: Path = getResource("/xlsx/45430.xlsx")
            val array = SSReader(path).readToArray()
            val collection = SSReader(path).readToCollection()
            val objects = SSReader(path).readToObjects(GenericLine::class)

            val expectedArray: Array<Array<String?>> = arrayOf()

            checkResults(array, collection, objects, expectedArray, GenericLine::class)
        }

        @Test
        fun read45540() {
            val path: Path = getResource("/xlsx/45540_classic_Footer.xlsx")
            val sheetName = "Top Six Functions"
            val array = SSReader(path).readToArray(sheetName)
            val collection = SSReader(path).readToCollection(sheetName)
            val objects = SSReader(path).readToObjects(sheetName, GenericLine::class)

            val expectedArray: Array<Array<String?>> = arrayOf(
                arrayOf("The University of Chicago Graduate School of Business"),
                arrayOf("Employment Statistics:  2005-2006"),
                arrayOf("Top Six Functions - Intern"),
                arrayOf("2006-09-11T00:00"),
                arrayOf("Function", "Percent of Hires", "Number of Hires(1)"),
                arrayOf("Top Six Functions", "78.1%", "424"),
                arrayOf("Finance - Investment Banking", "21.7%", "118"),
                arrayOf("Consulting", "16.4%", "89"),
                arrayOf("Finance - Investment Management/ Research", "11.8%", "64"),
                arrayOf("Marketing", "10.1%", "55"),
                arrayOf("Finance - Company Finance (Analysis/ Treasury)", "9.0%", "49"),
                arrayOf("Finance - Sales and Trading", "9.0%", "49"),
                arrayOf("All Others", "21.9%", "119"),
                arrayOf("Total", "100.0%", "543"),
                arrayOf("(1)   Eleven students in the Class of 2007 reported having 2 internships; one student had 3 internships.")
            )

            checkResults(array, collection, objects, expectedArray, GenericLine::class)
        }

        @Test
        fun read46535() {
            val path: Path = getResource("/xlsx/46535.xlsx")
            val sheetName = "Test"
            val array = SSReader(path).readToArray(sheetName)
            val collection = SSReader(path).readToCollection(sheetName)
            val objects = SSReader(path).readToObjects(sheetName, GenericLine::class)

            val expectedArray: Array<Array<String?>> = arrayOf(
                arrayOf("ABD", "HKG", "Hong Kong International", "Abadan")
            )

            checkResults(array, collection, objects, expectedArray, GenericLine::class)
        }

        @Test
        fun read46536() {
            val path: Path = getResource("/xlsx/46536.xlsx")
            val sheetName = "Test"
            val array = SSReader(path).readToArray(sheetName)
            val collection = SSReader(path).readToCollection(sheetName)
            val objects = SSReader(path).readToObjects(sheetName, GenericLine::class)

            val expectedArray: Array<Array<String?>> = arrayOf(
                arrayOf("row1", "1", "2", "3", "6"),
                arrayOf("row2", "4", "5", "6", "15"),
                arrayOf("row3", "7", "8", "9", "24")
            )

            checkResults(array, collection, objects, expectedArray, GenericLine::class)
        }

        @Test
        fun read48703() {
            val path: Path = getResource("/xlsx/48703.xlsx")
            val array = SSReader(path).readToArray()
            val collection = SSReader(path).readToCollection()
            val objects = SSReader(path).readToObjects(GenericLine::class)

            val expectedArray: Array<Array<String?>> = arrayOf(
                arrayOf("Method 1: Works with POI: =SUM(Sheet1!C1,Sheet2!C1,Sheet3!C1,Sheet4!C1)", "20", "2"),
                arrayOf("Method 2: Doesn't work with POI: =SUM(Sheet1:Sheet4!C1)", "20"),
                arrayOf("Both methods SUM the C1 cells from all 4 sheets.")
            )

            checkResults(array, collection, objects, expectedArray, GenericLine::class)
        }

        @Test
        fun read49928() {
            val path: Path = getResource("/xlsx/49928.xlsx")
            val array = SSReader(path).readToArray()
            val collection = SSReader(path).readToCollection()
            val objects = SSReader(path).readToObjects(GenericLine::class)

            val expectedArray: Array<Array<String?>> = arrayOf(
                arrayOf("£1")
            )

            checkResults(array, collection, objects, expectedArray, GenericLine::class)
        }

        @Test
        fun read49966() {
            val path: Path = getResource("/xlsx/49966.xlsx")
            val array = SSReader(path).readToArray()
            val collection = SSReader(path).readToCollection()
            val objects = SSReader(path).readToObjects(GenericLine::class)

            val expectedArray: Array<Array<String?>> = arrayOf(
                arrayOf("1", "2", "3", "3", "3")
            )

            checkResults(array, collection, objects, expectedArray, GenericLine::class)
        }

        @Test
        fun read50755() {
            val path: Path = getResource("/xlsx/50755_workday_formula_example.xlsx")
            val array = SSReader(path).readToArray()
            val collection = SSReader(path).readToCollection()
            val objects = SSReader(path).readToObjects(GenericLine::class)

            val expectedArray: Array<Array<String?>> = arrayOf(
                arrayOf("Inflow Date", "Due Date"),
                arrayOf("2011-02-01T00:00", "2011-02-08T00:00"),
                arrayOf("2011-02-02T00:00", "2011-02-09T00:00"),
                arrayOf("2011-02-03T00:00", "2011-02-10T00:00")
            )

            checkResults(array, collection, objects, expectedArray, GenericLine::class)
        }

        @Test
        fun read57236() {
            val path: Path = getResource("/xlsx/57236.xlsx")
            val array = SSReader(path).readToArray()
            val collection = SSReader(path).readToCollection()
            val objects = SSReader(path).readToObjects(GenericLine::class)

            val expectedArray: Array<Array<String?>> = arrayOf(
                arrayOf("3E-104"),
                arrayOf("5E-104"),
                arrayOf("5E-106")
            )

            checkResults(array, collection, objects, expectedArray, GenericLine::class)
        }

        @Test
        fun read57828() {
            val path: Path = getResource("/xlsx/57828.xlsx")
            val array = SSReader(path).readToArray()
            val collection = SSReader(path).readToCollection()
            val objects = SSReader(path).readToObjects(GenericLine::class)

            val expectedArray: Array<Array<String?>> = arrayOf(
                arrayOf("This", "is", "a", "test"),
                arrayOf("been", "forgotten", "by", "POI"),
                arrayOf("Lets", "fix", "this", "easy")
            )

            checkResults(array, collection, objects, expectedArray, GenericLine::class)
        }
    }

    @Nested
    @DisplayName("Read Xls files")
    inner class ReadXlsFiles {
        @Test
        fun readAddresses() {
            val path: Path = getResource("/xls/3dFormulas.xls")
            val array = SSReader(path).readToArray()
            val collection = SSReader(path).readToCollection()
            val objects = SSReader(path).readToObjects(GenericLine::class)

            val expectedArray: Array<Array<String?>> = arrayOf(
                arrayOf("11", "22", "33"),
                arrayOf("11", "22", "33"),
                arrayOf("S2-A1", "S2-A2"),
                arrayOf("165")
            )

            checkResults(array, collection, objects, expectedArray, GenericLine::class)
        }

        @Test
        fun read1900DateWindowing() {
            val path: Path = getResource("/xls/1900DateWindowing.xls")
            val array = SSReader(path).readToArray()
            val collection = SSReader(path).readToCollection()
            val objects = SSReader(path).readToObjects(GenericLine::class)

            val expectedArray: Array<Array<String?>> = arrayOf(
                arrayOf("2000-01-01T00:00", "2000-01-01T00:00")
            )

            checkResults(array, collection, objects, expectedArray, GenericLine::class)
        }

        @Test
        fun read13224() {
            val path: Path = getResource("/xls/13224.xls")
            val array = SSReader(path).readToArray()
            val collection = SSReader(path).readToCollection()
            val objects = SSReader(path).readToObjects(GenericLine::class)

            val expectedArray: Array<Array<String?>> = arrayOf(
                arrayOf("1"),
                arrayOf("1")
            )

            checkResults(array, collection, objects, expectedArray, GenericLine::class)
        }

        @Test
        fun read13796ShouldErrorOnUnresolvableExternalLink() {

            val path: Path = getResource("/xls/13796.xls")
            val errMsg = "Could not resolve external workbook name"
            assertThatThrownBy {
                SSReader(path).readToArray("Sheet1")
            }.isInstanceOf(RuntimeException::class.java)
                .hasMessageContaining(errMsg)
            assertThatThrownBy {
                SSReader(path).readToCollection("Sheet1")
            }.isInstanceOf(RuntimeException::class.java)
                .hasMessageContaining(errMsg)
            assertThatThrownBy {
                SSReader(path).readToObjects("Sheet1", Address::class)
            }.isInstanceOf(RuntimeException::class.java)
                .hasMessageContaining(errMsg)
        }

        @Test
        fun read143302() {
            val path: Path = getResource("/xls/14330-2.xls")
            val array = SSReader(path).readToArray()
            val collection = SSReader(path).readToCollection()
            val objects = SSReader(path).readToObjects(GenericLine::class)

            val expectedArray: Array<Array<String?>> = arrayOf(
                arrayOf("A", "A"),
                arrayOf("Test bold text")
            )

            checkResults(array, collection, objects, expectedArray, GenericLine::class)
        }

        @Test
        fun read14460() {
            val path: Path = getResource("/xls/14460.xls")
            val array = SSReader(path).readToArray()
            val collection = SSReader(path).readToCollection()
            val objects = SSReader(path).readToObjects(GenericLine::class)

            val expectedArray: Array<Array<String?>> = arrayOf(
                arrayOf("Base Spend", "0"),
                arrayOf(null, "Price Up", "- 0"),
                arrayOf(null, "Price Down", "- 0"),
                arrayOf(null, "Net Price", "- 0")
            )

            checkResults(array, collection, objects, expectedArray, GenericLine::class)
        }

        @Test
        fun read153375() {
            val path: Path = getResource("/xls/15375.xls")
            val array = SSReader(path).readToArray()
            val collection = SSReader(path).readToCollection()
            val objects = SSReader(path).readToObjects(GenericLine::class)

            val expectedArray: Array<Array<String?>> = arrayOf(
                arrayOf("Delaware", "100", "155", "5%", "90", "120", "4%", "10", "35", "1%"),
                arrayOf("Maryland", "200", "200", "6%", "150", "380", "6%", "50", "-180", "0%"),
                arrayOf("Florida", "150", "400", "4%", "140", "160", "2%", "10", "240", "2%"),
                arrayOf("Total NGD", "450", "755", "4%", "380", "660", "3%", "70", "95", "1%")
            )

            checkResults(array, collection, objects, expectedArray, GenericLine::class)
        }

        @Test
        fun read15573() {
            val path: Path = getResource("/xls/15573.xls")
            val array = SSReader(path).readToArray()
            val collection = SSReader(path).readToCollection()
            val objects = SSReader(path).readToObjects(GenericLine::class)

            val expectedArray: Array<Array<String?>> = arrayOf(
                arrayOf("TITLE", "DN"),
                arrayOf("Title", "DN", "55"),
                arrayOf("Title", "DN", "55"),
                arrayOf("Title", "DN", "55")
            )

            checkResults(array, collection, objects, expectedArray, GenericLine::class)
        }

        @Test
        fun read24207() {
            val path: Path = getResource("/xls/24207.xls")
            val array = SSReader(path).readToArray()
            val collection = SSReader(path).readToCollection()
            val objects = SSReader(path).readToObjects(GenericLine::class)

            val expectedArray: Array<Array<String?>> = arrayOf()

            checkResults(array, collection, objects, expectedArray, GenericLine::class)
        }

        @Test
        fun read25183() {
            val path: Path = getResource("/xls/25183.xls")
            val array = SSReader(path).readToArray()
            val collection = SSReader(path).readToCollection()
            val objects = SSReader(path).readToObjects(GenericLine::class)

            val expectedArray: Array<Array<String?>> = arrayOf(
                arrayOf(null, "0.25", "0.50", "0.75", "1.00", "1.25"),
                arrayOf("0.25", "12516.00", "20670.00", "26106.00", "29892.00", "32346.00"),
                arrayOf("0.50", "13386.00", "23370.00", "30840.00", "36552.00", "40572.00"),
                arrayOf("0.75", "13710.00", "24450.00", "32826.00", "39474.00", "44292.00"),
                arrayOf("1.00", "13884.00", "25056.00", "33978.00", "41196.00", "46530.00"),
                arrayOf("1.25", "14136.00", "26034.00", "35886.00", "44034.00", "50184.00"),
                arrayOf("1.50", "14292.00", "26754.00", "37524.00", "46824.00", "54198.00"),
                arrayOf("1.75", "14388.00", "27180.00", "38508.00", "48522.00", "56706.00"),
                arrayOf("2.00", "14454.00", "27468.00", "39168.00", "49680.00", "58428.00"),
                arrayOf("2.25", "14502.00", "27678.00", "39648.00", "50532.00", "59706.00"),
                arrayOf("2.50", "14538.00", "27840.00", "40020.00", "51180.00", "60690.00"),
                arrayOf("2.75", "14568.00", "27972.00", "40314.00", "51702.00", "61482.00"),
                arrayOf("3.00", "14592.00", "28074.00", "40554.00", "52122.00", "62124.00"),
                arrayOf("3.25", "14610.00", "28158.00", "40752.00", "52476.00", "62664.00"),
                arrayOf("3.50", "14628.00", "28236.00", "40920.00", "52770.00", "63120.00"),
                arrayOf("3.75", "14640.00", "28296.00", "41064.00", "53028.00", "63510.00"),
                arrayOf("4.00", "14652.00", "28350.00", "41190.00", "53250.00", "63852.00"),
                arrayOf("4.25", "14664.00", "28398.00", "41298.00", "53442.00", "64158.00"),
                arrayOf("4.50", "14676.00", "28440.00", "41400.00", "53616.00", "64422.00"),
                arrayOf("4.75", "14682.00", "28482.00", "41484.00", "53772.00", "64662.00"),
                arrayOf("5.00", "14694.00", "28512.00", "41562.00", "53910.00", "64872.00")
            )

            checkResults(array, collection, objects, expectedArray, GenericLine::class)
        }

        @Test
        fun read25695() {
            val path: Path = getResource("/xls/25695.xls")
            val array = SSReader(path).readToArray()
            val collection = SSReader(path).readToCollection()
            val objects = SSReader(path).readToObjects(GenericLine::class)

            val expectedArray: Array<Array<String?>> = arrayOf(
                arrayOf("สห", "สห"),
                arrayOf("XYZ", "XYZ"),
                arrayOf("สหXYZ", "สหXYZ"),
                arrayOf("XYZสห", "XYZสห"),
            )

            checkResults(array, collection, objects, expectedArray, GenericLine::class)
        }

        @Test
        fun read26100() {
            val path: Path = getResource("/xls/26100.xls")
            val array = SSReader(path).readToArray()
            val collection = SSReader(path).readToCollection()
            val objects = SSReader(path).readToObjects(GenericLine::class)

            val expectedArray: Array<Array<String?>> = arrayOf(
                arrayOf("1", "2"),
                arrayOf("Message Box:"),
                arrayOf(
                    "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678", null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "\"12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678\""
                ),
            )

            checkResults(array, collection, objects, expectedArray, GenericLine::class)
        }

        @Test
        fun read27349() {
            val path: Path = getResource("/xls/27349-vlookupAcrossSheets.xls")
            val array = SSReader(path).readToArray("TEST")
            val collection = SSReader(path).readToCollection("TEST")
            val objects = SSReader(path).readToObjects("TEST", GenericLine::class)

            val expectedArray: Array<Array<String?>> = arrayOf(
                arrayOf("3"),
                arrayOf("3"),
                arrayOf("Key", "Value"),
                arrayOf("1", "3"),
                arrayOf("3", "6"),
                arrayOf("5", "9"),
            )

            checkResults(array, collection, objects, expectedArray, GenericLine::class)
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

        @Test
        fun readBioStats() {
            val path: Path = getResource("/csv/biostats.csv")
            val array = SSReader(path).readToArray()
            val collection = SSReader(path).readToCollection()
            val objects = SSReader(path).readToObjects(BioStat::class)

            val expectedArray: Array<Array<String?>> = arrayOf(
                arrayOf("Name", "Sex", "Age", "Height (in)", "Weight (lbs)"),
                arrayOf("Alex", "M", "41", "74", "170"),
                arrayOf("Bert", "M", "42", "68", "166"),
                arrayOf("Carl", "M", "32", "70", "155"),
                arrayOf("Dave", "M", "39", "72", "167"),
                arrayOf("Elly", "F", "30", "66", "124"),
                arrayOf("Fran", "F", "33", "66", "115"),
                arrayOf("Gwen", "F", "26", "64", "121"),
                arrayOf("Hank", "M", "30", "71", "158"),
                arrayOf("Ivan", "M", "53", "72", "175"),
                arrayOf("Jake", "M", "32", "69", "143"),
                arrayOf("Kate", "F", "47", "69", "139"),
                arrayOf("Luke", "M", "34", "72", "163"),
                arrayOf("Myra", "F", "23", "62", "98"),
                arrayOf("Neil", "M", "36", "75", "160"),
                arrayOf("Omar", "M", "38", "70", "145"),
                arrayOf("Page", "F", "31", "67", "135"),
                arrayOf("Quin", "M", "29", "71", "176"),
                arrayOf("Ruth", "F", "28", "65", "131"),
            )

            checkResults(array, collection, objects, expectedArray, BioStat::class)
        }

        @Test
        fun readCities() {
            val path: Path = getResource("/csv/cities.csv")
            val array = SSReader(path).readToArray()
            val collection = SSReader(path).readToCollection()
            val objects = SSReader(path).readToObjects(City::class)

            val expectedArray: Array<Array<String?>> = arrayOf(
                arrayOf("LatD", "LatM", "LatS", "NS", "LonD", "LonM", "LonS", "EW", "City", "State"),
                arrayOf("41", "5", "59", "N", "80", "39", "0", "W", "Youngstown", "OH"),
                arrayOf("42", "52", "48", "N", "97", "23", "23", "W", null, "SD"),
                arrayOf("46", "35", "59", "N", "120", "30", "36", "W", "Yakima", "WA"),
                arrayOf("42", "16", "12", "N", "71", "48", "0", "W", "Worcester", "MA"),
                arrayOf("43", "37", "48", "N", "89", "46", "11", "W", "Wisconsin Dells", "WI"),
                arrayOf("36", "5", "59", "N", "80", "15", "0", "W", "Winston-Salem", "NC"),
                arrayOf("49", "52", "48", "N", "97", "9", "0", "W", "Winnipeg", "MB"),
                arrayOf("39", "11", "23", "N", "78", "9", "36", "W", "Winchester", "VA"),
                arrayOf("34", "14", "24", "N", "77", "55", "11", "W", "Wilmington", "NC"),
                arrayOf("39", "45", "0", "N", "75", "33", "0", "W", "Wilmington", "DE"),
                arrayOf("48", "9", "0", "N", "103", "37", "12", "W", "Williston", "ND"),
                arrayOf("41", "15", "0", "N", "77", "0", "0", "W", "Williamsport", "PA"),
                arrayOf("37", "40", "48", "N", "82", "16", "47", "W", "Williamson", "WV"),
                arrayOf("33", "54", "0", "N", "98", "29", "23", "W", "Wichita Falls", "TX"),
                arrayOf("37", "41", "23", "N", "97", "20", "23", "W", "Wichita", "KS"),
                arrayOf("40", "4", "11", "N", "80", "43", "12", "W", "Wheeling", "WV"),
                arrayOf("26", "43", "11", "N", "80", "3", "0", "W", "West Palm Beach", "FL"),
                arrayOf("47", "25", "11", "N", "120", "19", "11", "W", "Wenatchee", "WA"),
                arrayOf("41", "25", "11", "N", "122", "23", "23", "W", "Weed", "CA"),
                arrayOf("31", "13", "11", "N", "82", "20", "59", "W", "Waycross", "GA"),
                arrayOf("44", "57", "35", "N", "89", "38", "23", "W", "Wausau", "WI"),
                arrayOf("42", "21", "36", "N", "87", "49", "48", "W", "Waukegan", "IL"),
                arrayOf("44", "54", "0", "N", "97", "6", "36", "W", "Watertown", "SD"),
                arrayOf("43", "58", "47", "N", "75", "55", "11", "W", "Watertown", "NY"),
                arrayOf("42", "30", "0", "N", "92", "20", "23", "W", "Waterloo", "IA"),
                arrayOf("41", "32", "59", "N", "73", "3", "0", "W", "Waterbury", "CT"),
                arrayOf("38", "53", "23", "N", "77", "1", "47", "W", "Washington", "DC"),
                arrayOf("41", "50", "59", "N", "79", "8", "23", "W", "Warren", "PA"),
                arrayOf("46", "4", "11", "N", "118", "19", "48", "W", "Walla Walla", "WA"),
                arrayOf("31", "32", "59", "N", "97", "8", "23", "W", "Waco", "TX"),
                arrayOf("38", "40", "48", "N", "87", "31", "47", "W", "Vincennes", "IN"),
                arrayOf("28", "48", "35", "N", "97", "0", "36", "W", "Victoria", "TX"),
                arrayOf("32", "20", "59", "N", "90", "52", "47", "W", "Vicksburg", "MS"),
                arrayOf("49", "16", "12", "N", "123", "7", "12", "W", "Vancouver", "BC"),
                arrayOf("46", "55", "11", "N", "98", "0", "36", "W", "Valley City", "ND"),
                arrayOf("30", "49", "47", "N", "83", "16", "47", "W", "Valdosta", "GA"),
                arrayOf("43", "6", "36", "N", "75", "13", "48", "W", "Utica", "NY"),
                arrayOf("39", "54", "0", "N", "79", "43", "48", "W", "Uniontown", "PA"),
                arrayOf("32", "20", "59", "N", "95", "18", "0", "W", "Tyler", "TX"),
                arrayOf("42", "33", "36", "N", "114", "28", "12", "W", "Twin Falls", "ID"),
                arrayOf("33", "12", "35", "N", "87", "34", "11", "W", "Tuscaloosa", "AL"),
                arrayOf("34", "15", "35", "N", "88", "42", "35", "W", "Tupelo", "MS"),
                arrayOf("36", "9", "35", "N", "95", "54", "36", "W", "Tulsa", "OK"),
                arrayOf("32", "13", "12", "N", "110", "58", "12", "W", "Tucson", "AZ"),
                arrayOf("37", "10", "11", "N", "104", "30", "36", "W", "Trinidad", "CO"),
                arrayOf("40", "13", "47", "N", "74", "46", "11", "W", "Trenton", "NJ"),
                arrayOf("44", "45", "35", "N", "85", "37", "47", "W", "Traverse City", "MI"),
                arrayOf("43", "39", "0", "N", "79", "22", "47", "W", "Toronto", "ON"),
                arrayOf("39", "2", "59", "N", "95", "40", "11", "W", "Topeka", "KS"),
                arrayOf("41", "39", "0", "N", "83", "32", "24", "W", "Toledo", "OH"),
                arrayOf("33", "25", "48", "N", "94", "3", "0", "W", "Texarkana", "TX"),
                arrayOf("39", "28", "12", "N", "87", "24", "36", "W", "Terre Haute", "IN"),
                arrayOf("27", "57", "0", "N", "82", "26", "59", "W", "Tampa", "FL"),
                arrayOf("30", "27", "0", "N", "84", "16", "47", "W", "Tallahassee", "FL"),
                arrayOf("47", "14", "24", "N", "122", "25", "48", "W", "Tacoma", "WA"),
                arrayOf("43", "2", "59", "N", "76", "9", "0", "W", "Syracuse", "NY"),
                arrayOf("32", "35", "59", "N", "82", "20", "23", "W", "Swainsboro", "GA"),
                arrayOf("33", "55", "11", "N", "80", "20", "59", "W", "Sumter", "SC"),
                arrayOf("40", "59", "24", "N", "75", "11", "24", "W", "Stroudsburg", "PA"),
                arrayOf("37", "57", "35", "N", "121", "17", "24", "W", "Stockton", "CA"),
                arrayOf("44", "31", "12", "N", "89", "34", "11", "W", "Stevens Point", "WI"),
                arrayOf("40", "21", "36", "N", "80", "37", "12", "W", "Steubenville", "OH"),
                arrayOf("40", "37", "11", "N", "103", "13", "12", "W", "Sterling", "CO"),
                arrayOf("38", "9", "0", "N", "79", "4", "11", "W", "Staunton", "VA"),
                arrayOf("39", "55", "11", "N", "83", "48", "35", "W", "Springfield", "OH"),
                arrayOf("37", "13", "12", "N", "93", "17", "24", "W", "Springfield", "MO"),
                arrayOf("42", "5", "59", "N", "72", "35", "23", "W", "Springfield", "MA"),
                arrayOf("39", "47", "59", "N", "89", "39", "0", "W", "Springfield", "IL"),
                arrayOf("47", "40", "11", "N", "117", "24", "36", "W", "Spokane", "WA"),
                arrayOf("41", "40", "48", "N", "86", "15", "0", "W", "South Bend", "IN"),
                arrayOf("43", "32", "24", "N", "96", "43", "48", "W", "Sioux Falls", "SD"),
                arrayOf("42", "29", "24", "N", "96", "23", "23", "W", "Sioux City", "IA"),
                arrayOf("32", "30", "35", "N", "93", "45", "0", "W", "Shreveport", "LA"),
                arrayOf("33", "38", "23", "N", "96", "36", "36", "W", "Sherman", "TX"),
                arrayOf("44", "47", "59", "N", "106", "57", "35", "W", "Sheridan", "WY"),
                arrayOf("35", "13", "47", "N", "96", "40", "48", "W", "Seminole", "OK"),
                arrayOf("32", "25", "11", "N", "87", "1", "11", "W", "Selma", "AL"),
                arrayOf("38", "42", "35", "N", "93", "13", "48", "W", "Sedalia", "MO"),
                arrayOf("47", "35", "59", "N", "122", "19", "48", "W", "Seattle", "WA"),
                arrayOf("41", "24", "35", "N", "75", "40", "11", "W", "Scranton", "PA"),
                arrayOf("41", "52", "11", "N", "103", "39", "36", "W", "Scottsbluff", "NB"),
                arrayOf("42", "49", "11", "N", "73", "56", "59", "W", "Schenectady", "NY"),
                arrayOf("32", "4", "48", "N", "81", "5", "23", "W", "Savannah", "GA"),
                arrayOf("46", "29", "24", "N", "84", "20", "59", "W", "Sault Sainte Marie", "MI"),
                arrayOf("27", "20", "24", "N", "82", "31", "47", "W", "Sarasota", "FL"),
                arrayOf("38", "26", "23", "N", "122", "43", "12", "W", "Santa Rosa", "CA"),
                arrayOf("35", "40", "48", "N", "105", "56", "59", "W", "Santa Fe", "NM"),
                arrayOf("34", "25", "11", "N", "119", "41", "59", "W", "Santa Barbara", "CA"),
                arrayOf("33", "45", "35", "N", "117", "52", "12", "W", "Santa Ana", "CA"),
                arrayOf("37", "20", "24", "N", "121", "52", "47", "W", "San Jose", "CA"),
                arrayOf("37", "46", "47", "N", "122", "25", "11", "W", "San Francisco", "CA"),
                arrayOf("41", "27", "0", "N", "82", "42", "35", "W", "Sandusky", "OH"),
                arrayOf("32", "42", "35", "N", "117", "9", "0", "W", "San Diego", "CA"),
                arrayOf("34", "6", "36", "N", "117", "18", "35", "W", "San Bernardino", "CA"),
                arrayOf("29", "25", "12", "N", "98", "30", "0", "W", "San Antonio", "TX"),
                arrayOf("31", "27", "35", "N", "100", "26", "24", "W", "San Angelo", "TX"),
                arrayOf("40", "45", "35", "N", "111", "52", "47", "W", "Salt Lake City", "UT"),
                arrayOf("38", "22", "11", "N", "75", "35", "59", "W", "Salisbury", "MD"),
                arrayOf("36", "40", "11", "N", "121", "39", "0", "W", "Salinas", "CA"),
                arrayOf("38", "50", "24", "N", "97", "36", "36", "W", "Salina", "KS"),
                arrayOf("38", "31", "47", "N", "106", "0", "0", "W", "Salida", "CO"),
                arrayOf("44", "56", "23", "N", "123", "1", "47", "W", "Salem", "OR"),
                arrayOf("44", "57", "0", "N", "93", "5", "59", "W", "Saint Paul", "MN"),
                arrayOf("38", "37", "11", "N", "90", "11", "24", "W", "Saint Louis", "MO"),
                arrayOf("39", "46", "12", "N", "94", "50", "23", "W", "Saint Joseph", "MO"),
                arrayOf("42", "5", "59", "N", "86", "28", "48", "W", "Saint Joseph", "MI"),
                arrayOf("44", "25", "11", "N", "72", "1", "11", "W", "Saint Johnsbury", "VT"),
                arrayOf("45", "34", "11", "N", "94", "10", "11", "W", "Saint Cloud", "MN"),
                arrayOf("29", "53", "23", "N", "81", "19", "11", "W", "Saint Augustine", "FL"),
                arrayOf("43", "25", "48", "N", "83", "56", "24", "W", "Saginaw", "MI"),
                arrayOf("38", "35", "24", "N", "121", "29", "23", "W", "Sacramento", "CA"),
                arrayOf("43", "36", "36", "N", "72", "58", "12", "W", "Rutland", "VT"),
                arrayOf("33", "24", "0", "N", "104", "31", "47", "W", "Roswell", "NM"),
                arrayOf("35", "56", "23", "N", "77", "48", "0", "W", "Rocky Mount", "NC"),
                arrayOf("41", "35", "24", "N", "109", "13", "48", "W", "Rock Springs", "WY"),
                arrayOf("42", "16", "12", "N", "89", "5", "59", "W", "Rockford", "IL"),
                arrayOf("43", "9", "35", "N", "77", "36", "36", "W", "Rochester", "NY"),
                arrayOf("44", "1", "12", "N", "92", "27", "35", "W", "Rochester", "MN"),
                arrayOf("37", "16", "12", "N", "79", "56", "24", "W", "Roanoke", "VA"),
                arrayOf("37", "32", "24", "N", "77", "26", "59", "W", "Richmond", "VA"),
                arrayOf("39", "49", "48", "N", "84", "53", "23", "W", "Richmond", "IN"),
                arrayOf("38", "46", "12", "N", "112", "5", "23", "W", "Richfield", "UT"),
                arrayOf("45", "38", "23", "N", "89", "25", "11", "W", "Rhinelander", "WI"),
                arrayOf("39", "31", "12", "N", "119", "48", "35", "W", "Reno", "NV"),
                arrayOf("50", "25", "11", "N", "104", "39", "0", "W", "Regina", "SA"),
                arrayOf("40", "10", "48", "N", "122", "14", "23", "W", "Red Bluff", "CA"),
                arrayOf("40", "19", "48", "N", "75", "55", "48", "W", "Reading", "PA"),
                arrayOf("41", "9", "35", "N", "81", "14", "23", "W", "Ravenna", "OH")
            )

            checkResults(array, collection, objects, expectedArray, City::class)
        }

        @Test
        fun readFaithfulWithNoHeader() {
            val path: Path = getResource("/csv/faithful.csv")
            val array = SSReader(path).skipHeaders().readToArray()
            val collection = SSReader(path).skipHeaders().readToCollection()
            val objects = SSReader(path).skipHeaders().readToObjects(Faithful::class)

            val expectedArray: Array<Array<String?>> = arrayOf(
                arrayOf("1", "3.600", "79"),
                arrayOf("2", "1.800", "54"),
                arrayOf("3", "3.333", "74"),
                arrayOf("4", "2.283", "62"),
                arrayOf("5", "4.533", "85"),
                arrayOf("6", "2.883", "55"),
                arrayOf("7", "4.700", "88"),
                arrayOf("8", "3.600", "85"),
                arrayOf("9", "1.950", "51"),
                arrayOf("10", "4.350", "85")
            )

            checkResults(array, collection, objects, expectedArray, Faithful::class)
        }

        @Test
        fun readNewsDecline() {
            val path: Path = getResource("/csv/news_decline.csv")
            val array = SSReader(path).readToArray()
            val collection = SSReader(path).readToCollection()
            val objects = SSReader(path).readToObjects(NewsDecline::class)

            val expectedArray: Array<Array<String?>> = arrayOf(
                arrayOf("Show", "2009", "2010", "2011"),
                arrayOf("60 Minutes", "7.6", "7.4", "7.3"),
                arrayOf("48 Hours Mystery", "4.1", "3.9", "3.6"),
                arrayOf("20/20", "4.1", "3.7", "3.3"),
                arrayOf("Nightline", "2.7", "2.6", "2.7"),
                arrayOf("Dateline Friday", "4.1", "4.1", "3.9"),
                arrayOf("Dateline Sunday", "3.5", "3.2", "3.1")
            )

            checkResults(array, collection, objects, expectedArray, NewsDecline::class)
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
        objects.forEach {
            val props = it.javaClass.kotlin.memberProperties
            val nbNullOrEmpty = props.map { prop ->
                val value = prop.get(it)
                if (value == null || (value is String && value.isBlank()))
                    1
                else
                    0
            }.sum()

            if (nbNullOrEmpty == props.size)
                fail("The nb of properties match the nb of null and empty fields. Objects are probably not loaded correctly.")
        }
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