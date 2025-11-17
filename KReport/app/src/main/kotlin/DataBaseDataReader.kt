import java.sql.DriverManager

object DataBaseDataReader {

    fun readTable(dbPath: String, tableName: String): Pair<List<String>, List<List<String>>> {

        val url = "jdbc:sqlite:$dbPath"

        DriverManager.getConnection(url).use { conn ->

            val header = conn.createStatement().use { st ->
                val rs = st.executeQuery("PRAGMA table_info($tableName)")
                val cols = mutableListOf<String>()
                while (rs.next()) {
                    cols.add(rs.getString("name"))
                }
                cols
            }

            val rows = conn.createStatement().use { st ->
                val rs = st.executeQuery("SELECT * FROM $tableName")
                val list = mutableListOf<List<String>>()

                while (rs.next()) {
                    val row = header.map { colName ->
                        rs.getString(colName) ?: ""
                    }
                    list.add(row)
                }

                list
            }

            return header to rows
        }
    }
}
