import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.opencsv.CSVReader
import java.io.FileReader
import java.io.InputStream
import java.io.InputStreamReader

class CsvDataReader {
    companion object {
        fun readCsvFromResources(resourcePath: String): Pair<List<String>, List<List<String>>> {
            val inputStream: InputStream = Thread.currentThread()
                .contextClassLoader
                .getResourceAsStream(resourcePath)
                ?: throw IllegalArgumentException("Resource not found: $resourcePath")


            val data: List<List<String>> = csvReader {
                delimiter = ';'
            }.readAll(inputStream)

            inputStream.close()

            return processData(data)
        }

        private fun processData(data: List<List<String>>): Pair<List<String>, List<List<String>>> {
            if (data.isEmpty()) {
                return emptyList<String>() to emptyList()
            }

            val header = data.first()
            val rows = data.drop(1)

            println("Header: $header")
            println("Rows count: ${rows.size}")
            if (rows.isNotEmpty()) {
                println("First row: ${rows.first()}")
            }

            return header to rows
        }
    }

}