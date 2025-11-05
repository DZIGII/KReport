package pdf

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.font.Standard14Fonts
import spec.ReportGenerator
import java.io.File

class PdfExporter : ReportGenerator {
    override val implName: String = "PDF"

    override fun generateReport(
        header: List<String>,
        rows: List<List<String>>,
        destination: String?,
        title: String?,
        summary: Map<String, Any>
    ): String {
        val outputPath = destination ?: "report.pdf"

        PDDocument().use { document ->
            val page = PDPage(PDRectangle.A4)
            document.addPage(page)

            PDPageContentStream(document, page).use { contentStream ->
                var yPosition = 750f

                val boldFont = PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD)
                val normalFont = PDType1Font(Standard14Fonts.FontName.HELVETICA)


                val titleFontSize = 16f
                val headerFontSize = 11f
                val contentFontSize = 11f


                title?.let {
                    val titleWidth = calculateTextWidth(it, titleFontSize)
                    val xPosition = (page.mediaBox.width - titleWidth) / 2

                    contentStream.beginText()
                    contentStream.setFont(boldFont, titleFontSize)
                    contentStream.newLineAtOffset(xPosition, yPosition)
                    contentStream.showText(it)
                    contentStream.endText()
                    yPosition -= 30f


                    val lineLength = titleWidth + 40f
                    val lineX = (page.mediaBox.width - lineLength) / 2
                    contentStream.moveTo(lineX, yPosition)
                    contentStream.lineTo(lineX + lineLength, yPosition)
                    contentStream.stroke()
                    yPosition -= 20f
                }


                if (header.isNotEmpty() && rows.isNotEmpty()) {
                    val columnWidths = calculateExactColumnWidths(header, rows, contentFontSize)
                    val totalTableWidth = columnWidths.sum() + (columnWidths.size - 1) * 15f
                    val tableX = (page.mediaBox.width - totalTableWidth) / 2


                    contentStream.beginText()
                    contentStream.setFont(boldFont, headerFontSize)
                    contentStream.newLineAtOffset(tableX, yPosition)
                    contentStream.showText(formatTableRow(header, columnWidths))
                    contentStream.endText()
                    yPosition -= 20f


                    contentStream.moveTo(tableX, yPosition)
                    contentStream.lineTo(tableX + totalTableWidth, yPosition)
                    contentStream.stroke()
                    yPosition -= 15f


                    contentStream.setFont(normalFont, contentFontSize)
                    rows.forEach { row ->
                        if (yPosition < 100f) {
                            val newPage = PDPage(PDRectangle.A4)
                            document.addPage(newPage)
                            contentStream.beginText()
                            contentStream.setFont(normalFont, contentFontSize)
                            yPosition = 750f
                            contentStream.setFont(boldFont, headerFontSize)
                            contentStream.newLineAtOffset(tableX, yPosition)
                            contentStream.showText(formatTableRow(header, columnWidths))
                            contentStream.endText()
                            yPosition -= 35f
                            contentStream.setFont(normalFont, contentFontSize)
                        }

                        contentStream.beginText()
                        contentStream.newLineAtOffset(tableX, yPosition)
                        contentStream.showText(formatTableRow(row, columnWidths))
                        contentStream.endText()
                        yPosition -= 15f
                    }
                    yPosition -= 20f
                }

                if (summary.isNotEmpty()) {
                    val summaryTitle = "SUMMARY:"
                    val summaryWidth = calculateTextWidth(summaryTitle, headerFontSize)
                    val summaryX = (page.mediaBox.width - summaryWidth) / 2

                    contentStream.beginText()
                    contentStream.setFont(boldFont, headerFontSize)
                    contentStream.newLineAtOffset(summaryX, yPosition)
                    contentStream.showText(summaryTitle)
                    contentStream.endText()
                    yPosition -= 20f

                    contentStream.moveTo(50f, yPosition)
                    contentStream.lineTo(page.mediaBox.width - 50f, yPosition)
                    contentStream.stroke()
                    yPosition -= 15f

                    contentStream.setFont(normalFont, contentFontSize)
                    summary.forEach { (key, value) ->
                        if (yPosition < 50f) {
                            val newPage = PDPage(PDRectangle.A4)
                            document.addPage(newPage)
                            contentStream.beginText()
                            contentStream.setFont(normalFont, contentFontSize)
                            yPosition = 750f
                        }

                        contentStream.beginText()
                        contentStream.newLineAtOffset(50f, yPosition)
                        contentStream.showText("$key: $value")
                        contentStream.endText()
                        yPosition -= 15f
                    }
                }
            }

            document.save(outputPath)
        }

        val outputFile = File(outputPath)
        return if (outputFile.exists()) {
            "✅ PDF report successfully generated at: ${outputFile.absolutePath}"
        } else {
            "❌ PDF report generation failed!"
        }
    }

    private fun calculateTextWidth(text: String, fontSize: Float): Float {
        return text.length * fontSize * 0.6f
    }

    private fun calculateExactColumnWidths(header: List<String>, rows: List<List<String>>, fontSize: Float): List<Float> {
        val allRows = listOf(header) + rows
        return header.indices.map { colIndex ->
            val maxContentLength = allRows.maxOfOrNull { row ->
                row.getOrNull(colIndex)?.length ?: 0
            } ?: 0
            (maxContentLength.coerceAtLeast(3) * fontSize * 0.6f) + 10f
        }
    }

    private fun formatTableRow(row: List<String>, columnWidths: List<Float>): String {
        return row.mapIndexed { index, cell ->
            val estimatedWidth = (columnWidths.getOrNull(index) ?: 80f) / 0.6f / 10f
            val maxChars = estimatedWidth.toInt().coerceAtLeast(3)
            val displayText = if (cell.length > maxChars) cell.take(maxChars - 1) + "." else cell
            displayText.padEnd(maxChars)
        }.joinToString("  ")
    }
}