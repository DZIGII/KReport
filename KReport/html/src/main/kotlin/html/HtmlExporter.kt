package html

import spec.ReportGenerator
import spec.styles.CellStyle
import spec.styles.StyleConfig
import spec.styles.StyleType
import java.io.File

class HtmlExporter : ReportGenerator {

    override val implName: String = "HTML"

    override fun generateReport(
        header: List<String>,
        rows: List<List<String>>,
        destination: String?,
        title: String?,
        summary: Map<String, Any>,
        styles: StyleConfig
    ): String {

        val html = buildString {
            appendLine("<html>")
            appendLine("<head>")
            appendLine("<style>")
            appendLine("""
                body { font-family: Arial, sans-serif; padding: 20px; }
                h1 { text-align:center; }
                table { width:100%; border-collapse: collapse; margin-top:20px; }
                th, td { border:1px solid #aaa; padding:8px; text-align:left; }
                th { background:#f0f0f0; }
                .summary { margin-top:25px; }
                .summary h2 { margin-bottom:10px; }
            """.trimIndent())
            appendLine("</style>")
            appendLine("</head>")
            appendLine("<body>")

            appendLine("<h1>${title ?: "Report"}</h1>")

            appendLine("<table>")
            appendLine("<tr>")
            header.forEachIndexed { index, column ->
                val style = styles.headerStyles[index]
                appendLine("  <th style='${htmlStyle(style)}'>$column</th>")
            }


            rows.forEach { row ->
                appendLine("<tr>")
                row.forEachIndexed { colIndex, cell ->
                    val style = styles.columnStyles[colIndex]
                    appendLine("  <td style='${htmlStyle(style)}'>$cell</td>")
                }
                appendLine("</tr>")
            }
            appendLine("</table>")

            if (summary.isNotEmpty()) {
                appendLine("<div class='summary'>")
                appendLine("<h2>Summary</h2>")
                summary.forEach { (k, v) ->
                    appendLine("<p><strong>$k:</strong> $v</p>")
                }
                appendLine("</div>")
            }

            appendLine("</body>")
            appendLine("</html>")
        }

        destination?.let { File(it).writeText(html) }

        return html
    }

    private fun htmlStyle(style: CellStyle?): String {
        if (style == null) return ""

        val builder = StringBuilder()

        style.styles.forEach { (type, value) ->
            when (type) {
                StyleType.BOLD -> builder.append("font-weight: bold;")
                StyleType.ITALIC -> builder.append("font-style: italic;")
                StyleType.COLOR_TEXT -> builder.append("color: $value;")
            }
        }
        return builder.toString()
    }

}
