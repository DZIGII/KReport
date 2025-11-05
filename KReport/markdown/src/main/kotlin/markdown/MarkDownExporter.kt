package markdown

import org.commonmark.node.*
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import spec.ReportGenerator
import java.io.File

class MarkDownExporter : ReportGenerator {
    override val implName: String = "Markdown"

    override fun generateReport(
        header: List<String>,
        rows: List<List<String>>,
        destination: String?,
        title: String?,
        summary: Map<String, Any>
    ): String {
        val markdownContent = buildString {
            title?.let {
                appendLine("# $it")
                appendLine()
            }

            if (header.isNotEmpty() && rows.isNotEmpty()) {
                appendLine()

                appendLine("| ${header.joinToString(" | ")} |")

                val alignments = header.map { ":---:" }
                appendLine("|${alignments.joinToString("|")}|")

                rows.forEach { row ->
                    val formattedRow = row.map { cell ->
                        cell.replace("|", "\\|")
                    }
                    appendLine("| ${formattedRow.joinToString(" | ")} |")
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
}