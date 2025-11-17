package markdown

import spec.ReportGenerator
import spec.styles.CellStyle

import spec.styles.StyleConfig
import spec.styles.StyleType
import java.io.File

class MarkDownExporter : ReportGenerator {
    override val implName: String = "Markdown"

    override fun generateReport(
        header: List<String>,
        rows: List<List<String>>,
        destination: String?,
        title: String?,
        summary: Map<String, Any>,
        styles: StyleConfig
    ): String {
        val markdownContent = buildString {
            title?.let {
                appendLine("# $it")
                appendLine()
            }

            if (header.isNotEmpty() && rows.isNotEmpty()) {
                appendLine()

                val headerMd = header.mapIndexed { index, col ->
                    mdFormat(col, styles.headerStyles[index])
                }
                appendLine("| ${headerMd.joinToString(" | ")} |")


                val alignments = header.map { ":---:" }
                appendLine("|${alignments.joinToString("|")}|")

                rows.forEach { row ->
                    val rowMd = row.mapIndexed { colIndex, cell ->
                        mdFormat(cell, styles.columnStyles[colIndex])
                    }
                    appendLine("| ${rowMd.joinToString(" | ")} |")
                }
                appendLine()
            }

            if (summary.isNotEmpty()) {
                appendLine("## Summary")
                appendLine()

                summary.forEach { (key, value) ->
                    appendLine("- **${key.replace(":", "\\:")}:** `$value`")
                }
                appendLine()
            }

            if (rows.isNotEmpty()) {
                appendLine("## 📈 Statistics")
                appendLine()
                appendLine("- **Total Records:** `${rows.size}`")
                appendLine("- **Columns:** `${header.size}`")
                appendLine("- **Generated:** `${java.time.LocalDate.now()}`")
                appendLine()
            }

            appendLine("---")
            appendLine("*Generated on ${java.time.LocalDateTime.now()}*")
            appendLine("*Format: Markdown*")
        }

        val outputPath = destination ?: "report.md"
        File(outputPath).writeText(markdownContent)

        println("Markdown report generated: ${File(outputPath).absolutePath}")

        return markdownContent
    }

    private fun mdFormat(cell: String, style: CellStyle?): String {
        var text = cell

        style?.styles?.forEach { (type, value) ->
            when (type) {
                StyleType.BOLD -> text = "**$text**"
                StyleType.ITALIC -> text = "*$text*"
                StyleType.COLOR_TEXT -> text = "<span style=\"color:$value\">$text</span>"
            }
        }

        return text
    }

}