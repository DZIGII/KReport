package spec.calculations

/**
 * Provides calculation utilities used across all report generator implementations.
 *
 * This component offers common statistical operations over tabular dataset rows.
 * All functions operate on a 2D list of strings, where each inner list represents
 * a table row and each column must be convertible to a numeric value for numeric
 * operations (e.g., SUM, AVERAGE, MIN, MAX).
 *
 * These utilities are format-agnostic and can be reused by any exporter (PDF, HTML,
 * PlainText, Markdown...). Their purpose is to centralize business-logic related to
 * data analysis, ensuring consistency across all output formats.
 */

object DataCalculator {

    /**
     * Returns the total number of data rows.
     *
     * @param rows A list of table rows.
     * @return Number of rows.
     */

    fun count(rows: List<List<String>>): Int = rows.size

    /**
     * Computes the sum of all numeric values in the given column.
     *
     * Non-numeric values and missing cells are ignored.
     *
     * @param rows         Table rows.
     * @param columnIndex  Target column index.
     * @return Sum as a Double. Returns 0.0 if no valid numeric values are found.
     */

    fun sum(rows: List<List<String>>, columnIndex: Int): Double {
        return rows.mapNotNull { it.getOrNull(columnIndex)?.toDoubleOrNull() }.sum()
    }

    /**
     * Computes the arithmetic mean of values in a given column.
     *
     * @param rows         Table rows.
     * @param columnIndex  Target column index.
     * @return Average value or 0.0 if the column has no numeric entries.
     */

    fun average(rows: List<List<String>>, columnIndex: Int): Double {
        val numericValues = rows.mapNotNull { it.getOrNull(columnIndex)?.toDoubleOrNull() }
        return if (numericValues.isNotEmpty()) numericValues.average() else 0.0
    }

    /**
     * Returns the smallest numeric value in the given column.
     *
     * @param rows         Table rows.
     * @param columnIndex  Target column.
     * @return Minimum value, or 0.0 if the column has no numeric entries.
     */

    fun min(rows: List<List<String>>, columnIndex: Int): Double {
        return rows.mapNotNull { it.getOrNull(columnIndex)?.toDoubleOrNull() }.minOrNull() ?: 0.0
    }

    /**
     * Returns the largest numeric value in the given column.
     *
     * @param rows         Table rows.
     * @param columnIndex  Target column.
     * @return Maximum value, or 0.0 if the column has no numeric entries.
     */

    fun max(rows: List<List<String>>, columnIndex: Int): Double {
        return rows.mapNotNull { it.getOrNull(columnIndex)?.toDoubleOrNull() }.maxOrNull() ?: 0.0
    }

    /**
     * Counts how many rows satisfy a given condition on a specific column.
     *
     * Example:
     * ```
     * countIf(rows, 2) { it.toInt() > 40 }
     * ```
     *
     * @param rows         Table rows.
     * @param columnIndex  Target column.
     * @param condition    Predicate applied to each cell.
     * @return Number of rows matching the condition.
     */

    fun countIf(rows: List<List<String>>, columnIndex: Int, condition: (String) -> Boolean): Int {
        return rows.count { row -> row.getOrNull(columnIndex)?.let(condition) ?: false }
    }

    /**
     * Computes the sum of numeric entries that satisfy a given condition.
     *
     * Example:
     * ```
     * sumIf(rows, 1) { it.toDouble() >= 50_000 }
     * ```
     *
     * @param rows         Table rows.
     * @param columnIndex  Target column.
     * @param condition    Predicate controlling which values to include.
     * @return Sum of values that pass the condition.
     */

    fun sumIf(rows: List<List<String>>, columnIndex: Int, condition: (String) -> Boolean): Double {
        return rows
            .filter { row -> row.getOrNull(columnIndex)?.let(condition) ?: false }
            .mapNotNull { it.getOrNull(columnIndex)?.toDoubleOrNull() }
            .sum()
    }
}