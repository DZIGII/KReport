package spec.calculations

/**
 * Provides functionality for creating new derived columns in a tabular dataset.
 *
 * A derived column is produced by evaluating a mathematical expression that
 * references existing column names. The result of the expression is appended
 * as a new column for each row.
 *
 * Example:
 * expression = "ESPB + Godina * 2"
 * newColumnName = "Score"
 *
 * Steps performed:
 * 1) Tokenize the expression ("ESPB", "+", "Godina", "*", "2")
 * 2) Resolve column references using the provided header
 * 3) Evaluate the expression for every row
 * 4) Append the computed value to each row
 *
 * Supported operators:
 * +, -, *, /
 *
 * Column names must match exactly the names in header list.
 * All referenced column values must be numeric.
 */

object DerivedColumnCalculator {

    /**
     * Adds a derived column to the dataset based on a given expression.
     *
     * @param header         Existing list of column names
     * @param rows           Rows of the dataset
     * @param expression     Expression referencing existing columns
     * @param newColumnName  Name of the new column to append
     *
     * @return Pair(newHeader, newRows)
     *         newHeader = old header + new column name
     *         newRows = each row + computed value
     */

    fun addDerivedColumn(
        header: List<String>,
        rows: List<List<String>>,
        expression: String,
        newColumnName: String
    ): Pair<List<String>, List<List<String>>> {

        val colIndex = header.withIndex().associate { it.value to it.index }
        val tokens = tokenize(expression)
        val newHeader = header + newColumnName

        val newRows = rows.map { row ->

            val value = evaluateExpression(tokens) { colName ->
                val index = colIndex[colName]
                    ?: throw IllegalArgumentException("Nepoznata kolona: $colName")

                row[index].toDoubleOrNull()
                    ?: throw IllegalArgumentException("Vrednost u koloni nije broj: $colName")
            }

            row + value.toString()
        }

        return newHeader to newRows
    }

    /**
     * Splits an expression into tokens (identifiers, numbers, operators).
     *
     * Example:
     * "ESPB + Godina * 2" -> ["ESPB", "+", "Godina", "*", "2"]
     */

    private fun tokenize(expr: String): List<String> {
        val noSpaces = expr.replace(" ", "")
        val result = mutableListOf<String>()

        var current = ""

        for (c in noSpaces) {
            if (c.isLetterOrDigit() || c == '_') {
                current += c
            } else {
                if (current.isNotEmpty()) {
                    result.add(current)
                    current = ""
                }
                result.add(c.toString())
            }
        }

        if (current.isNotEmpty())
            result.add(current)

        return result
    }

    /**
     * Evaluates a list of tokens into a numeric result.
     *
     * Column names are resolved using the provided lambda:
     * columnResolver("ESPB") -> Double value of that column
     *
     * The evaluator performs:
     *  1) Multiplication/division left-to-right
     *  2) Addition/subtraction left-to-right
     *
     * No parentheses are supported.
     */

    private fun evaluateExpression(
        tokens: List<String>,
        columnResolver: (String) -> Double
    ): Double {

        val nums = tokens.map { token ->
            when {
                token in listOf("+", "-", "*", "/") -> token
                token.matches(Regex("[A-Za-z_][A-Za-z0-9_]*")) ->
                    columnResolver(token)
                else -> token.toDoubleOrNull() ?: error("nepoznat token: $token")
            }
        }.toMutableList()

        var i = 0
        while (i < nums.size) {
            if (nums[i] == "*" || nums[i] == "/") {
                val op = nums[i] as String
                val left = nums[i - 1] as Double
                val right = nums[i + 1] as Double

                val result = if (op == "*") left * right else left / right

                nums[i - 1] = result
                nums.removeAt(i)
                nums.removeAt(i)
            } else {
                i++
            }
        }

        i = 0
        while (i < nums.size) {
            if (nums[i] == "+" || nums[i] == "-") {
                val op = nums[i] as String
                val left = nums[i - 1] as Double
                val right = nums[i + 1] as Double

                val result = if (op == "+") left + right else left - right

                nums[i - 1] = result
                nums.removeAt(i)
                nums.removeAt(i)
            } else {
                i++
            }
        }

        return nums[0] as Double
    }
}
