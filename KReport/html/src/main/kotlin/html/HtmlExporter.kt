package html

import spec.ReportGenerator
import java.io.File

class HtmlExporter : ReportGenerator {
    override val implName: String = "HTML"

    override fun generateReport(
        header: List<String>,
        rows: List<List<String>>,
        destination: String?,
        title: String?,
        summary: Map<String, Any>
    ): String {
        val htmlContent = buildString {
            appendLine("<!DOCTYPE html>")
            appendLine("<html lang='en'>")
            appendLine("<head>")
            appendLine("    <meta charset='UTF-8'>")
            appendLine("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>")
            appendLine("    <title>${title ?: "Report"}</title>")
            appendLine("    <style>")
            appendLine("        * { margin: 0; padding: 0; box-sizing: border-box; }")
            appendLine("        body { ")
            appendLine("            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;")
            appendLine("            line-height: 1.6;")
            appendLine("            color: #333;")
            appendLine("            background-color: #f8f9fa;")
            appendLine("            padding: 20px;")
            appendLine("        }")
            appendLine("        .report-container {")
            appendLine("            max-width: 1200px;")
            appendLine("            margin: 0 auto;")
            appendLine("            background: white;")
            appendLine("            border-radius: 10px;")
            appendLine("            box-shadow: 0 2px 10px rgba(0,0,0,0.1);")
            appendLine("            overflow: hidden;")
            appendLine("        }")
            appendLine("        .report-header {")
            appendLine("            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);")
            appendLine("            color: white;")
            appendLine("            padding: 30px;")
            appendLine("            text-align: center;")
            appendLine("        }")
            appendLine("        .report-title {")
            appendLine("            font-size: 2.5em;")
            appendLine("            font-weight: 300;")
            appendLine("            margin-bottom: 10px;")
            appendLine("        }")
            appendLine("        .report-subtitle {")
            appendLine("            font-size: 1.1em;")
            appendLine("            opacity: 0.9;")
            appendLine("        }")
            appendLine("        .table-section {")
            appendLine("            padding: 30px;")
            appendLine("        }")
            appendLine("        .section-title {")
            appendLine("            font-size: 1.5em;")
            appendLine("            color: #667eea;")
            appendLine("            margin-bottom: 20px;")
            appendLine("            padding-bottom: 10px;")
            appendLine("            border-bottom: 2px solid #e9ecef;")
            appendLine("        }")
            appendLine("        .data-table {")
            appendLine("            width: 100%;")
            appendLine("            border-collapse: collapse;")
            appendLine("            margin: 20px 0;")
            appendLine("            font-size: 0.95em;")
            appendLine("        }")
            appendLine("        .data-table th {")
            appendLine("            background: #667eea;")
            appendLine("            color: white;")
            appendLine("            padding: 15px;")
            appendLine("            text-align: left;")
            appendLine("            font-weight: 600;")
            appendLine("            text-transform: uppercase;")
            appendLine("            letter-spacing: 0.5px;")
            appendLine("        }")
            appendLine("        .data-table td {")
            appendLine("            padding: 12px 15px;")
            appendLine("            border-bottom: 1px solid #e9ecef;")
            appendLine("        }")
            appendLine("        .data-table tr:nth-child(even) {")
            appendLine("            background: #f8f9fa;")
            appendLine("        }")
            appendLine("        .data-table tr:hover {")
            appendLine("            background: #e3f2fd;")
            appendLine("            transition: background 0.3s ease;")
            appendLine("        }")
            appendLine("        .summary-section {")
            appendLine("            padding: 30px;")
            appendLine("            background: #f8f9fa;")
            appendLine("            border-top: 1px solid #e9ecef;")
            appendLine("        }")
            appendLine("        .summary-grid {")
            appendLine("            display: grid;")
            appendLine("            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));")
            appendLine("            gap: 15px;")
            appendLine("            margin-top: 20px;")
            appendLine("        }")
            appendLine("        .summary-item {")
            appendLine("            background: white;")
            appendLine("            padding: 20px;")
            appendLine("            border-radius: 8px;")
            appendLine("            box-shadow: 0 1px 3px rgba(0,0,0,0.1);")
            appendLine("            text-align: center;")
            appendLine("        }")
            appendLine("        .summary-label {")
            appendLine("            font-size: 0.9em;")
            appendLine("            color: #6c757d;")
            appendLine("            margin-bottom: 5px;")
            appendLine("            text-transform: uppercase;")
            appendLine("            letter-spacing: 0.5px;")
            appendLine("        }")
            appendLine("        .summary-value {")
            appendLine("            font-size: 1.5em;")
            appendLine("            font-weight: bold;")
            appendLine("            color: #667eea;")
            appendLine("        }")
            appendLine("        .footer {")
            appendLine("            text-align: center;")
            appendLine("            padding: 20px;")
            appendLine("            color: #6c757d;")
            appendLine("            font-size: 0.9em;")
            appendLine("            border-top: 1px solid #e9ecef;")
            appendLine("        }")
            appendLine("        @media (max-width: 768px) {")
            appendLine("            .data-table {")
            appendLine("                font-size: 0.8em;")
            appendLine("            }")
            appendLine("            .data-table th,")
            appendLine("            .data-table td {")
            appendLine("                padding: 8px 10px;")
            appendLine("            }")
            appendLine("            .report-title {")
            appendLine("                font-size: 2em;")
            appendLine("            }")
            appendLine("        }")
            appendLine("    </style>")
            appendLine("</head>")
            appendLine("<body>")

            appendLine("    <div class='report-container'>")

            appendLine("        <div class='report-header'>")
            appendLine("            <h1 class='report-title'>${title ?: "Report"}</h1>")
            appendLine("            <div class='report-subtitle'>Generated Report</div>")
            appendLine("        </div>")

            if (header.isNotEmpty() && rows.isNotEmpty()) {
                appendLine("        <div class='table-section'>")
                appendLine("            <h2 class='section-title'>Data Table</h2>")
                appendLine("            <table class='data-table'>")
                appendLine("                <thead>")
                appendLine("                    <tr>")
                header.forEach { column ->
                    appendLine("                        <th>$column</th>")
                }
                appendLine("                    </tr>")
                appendLine("                </thead>")
                appendLine("                <tbody>")

                rows.forEach { row ->
                    appendLine("                    <tr>")
                    row.forEach { cell ->
                        appendLine("                        <td>$cell</td>")
                    }
                    appendLine("                    </tr>")
                }

                appendLine("                </tbody>")
                appendLine("            </table>")
                appendLine("        </div>")
            }

            if (summary.isNotEmpty()) {
                appendLine("        <div class='summary-section'>")
                appendLine("            <h2 class='section-title'>Summary</h2>")
                appendLine("            <div class='summary-grid'>")

                summary.forEach { (key, value) ->
                    appendLine("                <div class='summary-item'>")
                    appendLine("                    <div class='summary-label'>$key</div>")
                    appendLine("                    <div class='summary-value'>$value</div>")
                    appendLine("                </div>")
                }

                appendLine("            </div>")
                appendLine("        </div>")
            }

            appendLine("        <div class='footer'>")
            appendLine("            Generated on ${java.time.LocalDateTime.now()}")
            appendLine("        </div>")

            appendLine("    </div>")
            appendLine("</body>")
            appendLine("</html>")
        }

        destination?.let { path ->
            File(path).writeText(htmlContent)
        }

        return htmlContent
    }
}