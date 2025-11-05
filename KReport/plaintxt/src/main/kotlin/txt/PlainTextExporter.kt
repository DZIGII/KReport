package txt

import spec.ReportGenerator
import java.io.File

class PlainTextExporter : ReportGenerator {

    override val implName: String = "PlainText"

    override fun generateReport(
        header: List<String>,
        rows: List<List<String>>,
        destination: String?,
        title: String?,
        summary: Map<String, Any>
    ): String {
        val sb = StringBuilder()
        val lineWidth = 80

        title?.let {
            val centeredTitle = it.center(lineWidth)
            sb.appendLine(centeredTitle)
            sb.appendLine("=".repeat(lineWidth))
            sb.appendLine()
        }

        if (header.isNotEmpty() && rows.isNotEmpty()) {
            val columnWidths = calculateColumnWidths(header, rows)

            sb.appendLine(formatRow(header, columnWidths))
            sb.appendLine("-".repeat(columnWidths.sum() + (columnWidths.size - 1) * 3))

            rows.forEach { row ->
                sb.appendLine(formatRow(row, columnWidths))
            }
            sb.appendLine()
        }

        if (summary.isNotEmpty()) {
            sb.appendLine("SUMMARY:".center(lineWidth))
            sb.appendLine("-".repeat(lineWidth))
            summary.forEach { (key, value) ->
                val summaryLine = "$key: $value".padEnd(lineWidth)
                sb.appendLine(summaryLine)
            }
        }

        val reportText = sb.toString()

        destination?.let { path ->
            File(path).writeText(reportText)
        }

        return reportText
    }

    private fun calculateColumnWidths(header: List<String>, rows: List<List<String>>): List<Int> {
        val allRows = listOf(header) + rows
        return header.indices.map { colIndex ->
            allRows.maxOfOrNull { row ->
                row.getOrNull(colIndex)?.length ?: 0
            }?.coerceAtLeast(10) ?: 10
        }
    }

    private fun formatRow(row: List<String>, columnWidths: List<Int>): String {
        return row.mapIndexed { index, cell ->
            cell.padEnd(columnWidths.getOrNull(index) ?: 10)
        }.joinToString(" | ")
    }

    private fun String.center(width: Int): String {
        if (this.length >= width) return this
        val padding = width - this.length
        val leftPadding = padding / 2
        val rightPadding = padding - leftPadding
        return " ".repeat(leftPadding) + this + " ".repeat(rightPadding)
    }
}