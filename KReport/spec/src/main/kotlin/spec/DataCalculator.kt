package spec.calculations

object DataCalculator {

    fun count(rows: List<List<String>>): Int = rows.size

    fun sum(rows: List<List<String>>, columnIndex: Int): Double {
        return rows.mapNotNull { it.getOrNull(columnIndex)?.toDoubleOrNull() }.sum()
    }

    fun average(rows: List<List<String>>, columnIndex: Int): Double {
        val numericValues = rows.mapNotNull { it.getOrNull(columnIndex)?.toDoubleOrNull() }
        return if (numericValues.isNotEmpty()) numericValues.average() else 0.0
    }

    fun min(rows: List<List<String>>, columnIndex: Int): Double {
        return rows.mapNotNull { it.getOrNull(columnIndex)?.toDoubleOrNull() }.minOrNull() ?: 0.0
    }

    fun max(rows: List<List<String>>, columnIndex: Int): Double {
        return rows.mapNotNull { it.getOrNull(columnIndex)?.toDoubleOrNull() }.maxOrNull() ?: 0.0
    }

    fun countIf(rows: List<List<String>>, columnIndex: Int, condition: (String) -> Boolean): Int {
        return rows.count { row -> row.getOrNull(columnIndex)?.let(condition) ?: false }
    }

    fun sumIf(rows: List<List<String>>, columnIndex: Int, condition: (String) -> Boolean): Double {
        return rows
            .filter { row -> row.getOrNull(columnIndex)?.let(condition) ?: false }
            .mapNotNull { it.getOrNull(columnIndex)?.toDoubleOrNull() }
            .sum()
    }
}
