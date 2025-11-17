package pdf

import com.lowagie.text.*
import com.lowagie.text.pdf.PdfPCell
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.PdfWriter
import spec.ReportGenerator
import spec.styles.*
import java.awt.Color
import java.io.FileOutputStream

class PdfExporter : ReportGenerator {

    override val implName = "PDF"

    override fun generateReport(
        header: List<String>,
        rows: List<List<String>>,
        destination: String?,
        title: String?,
        summary: Map<String, Any>,
        styles: StyleConfig
    ): String {

        val outputPath = destination ?: "report.pdf"
        val document = Document(PageSize.A4, 40f, 40f, 40f, 40f)
        PdfWriter.getInstance(document, FileOutputStream(outputPath))

        document.open()

        if (!title.isNullOrEmpty()) {
            val f = Font(Font.HELVETICA, 18f, Font.BOLD)
            val p = Paragraph(title, f)
            p.alignment = Element.ALIGN_CENTER
            p.spacingAfter = 20f
            document.add(p)
        }


        val table = PdfPTable(header.size)
        table.widthPercentage = 100f

        header.forEachIndexed { index, col ->
            val style = styles.headerStyles[index]
            val cellFont = buildFont(style, defaultBold = true)
            val cell = PdfPCell(Phrase(col, cellFont))
            table.addCell(cell)
        }


        rows.forEach { row ->
            row.forEachIndexed { colIndex, cellText ->
                val style = styles.columnStyles[colIndex]
                val cellFont = buildFont(style)
                val cell = PdfPCell(Phrase(cellText, cellFont))
                cell.paddingTop = 4f
                cell.paddingBottom = 4f
                table.addCell(cell)
            }
        }

        document.add(table)

        if (summary.isNotEmpty()) {
            val subtitle = Paragraph("Summary", Font(Font.HELVETICA, 14f, Font.BOLD))
            subtitle.spacingBefore = 16f
            subtitle.spacingAfter = 8f
            document.add(subtitle)

            val sumTable = PdfPTable(2)
            sumTable.widthPercentage = 50f

            summary.forEach { (key, value) ->
                sumTable.addCell(Phrase(key))
                sumTable.addCell(Phrase(value.toString()))
            }

            document.add(sumTable)
        }

        document.close()
        return "PDF generated at: $outputPath"
    }

    private fun buildFont(style: CellStyle?, defaultBold: Boolean = false): Font {

        var fontStyle = Font.NORMAL

        if (defaultBold) fontStyle = fontStyle or Font.BOLD
        if (style?.styles?.containsKey(StyleType.BOLD) == true) fontStyle = fontStyle or Font.BOLD
        if (style?.styles?.containsKey(StyleType.ITALIC) == true) fontStyle = fontStyle or Font.ITALIC

        val font = Font(Font.HELVETICA, 11f, fontStyle)

        val colorHex = style?.styles?.get(StyleType.COLOR_TEXT)
        if (colorHex != null) font.color = Color.decode(colorHex)

        return font
    }
}
