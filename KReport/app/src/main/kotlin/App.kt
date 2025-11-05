import spec.ReportGenerator
import spec.calculations.DataCalculator
import java.util.ServiceLoader

object App {
    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val serviceLoader = ServiceLoader.load(ReportGenerator::class.java)
            val exportServices = mutableMapOf<String, ReportGenerator>()
            serviceLoader.forEach { service ->
                exportServices[service.implName] = service
            }

            println("Pronađeni eksporteri: ${exportServices.keys}")

            val (header, rows) = CsvDataReader.readCsvFromResources("test.csv")

            val exporter = exportServices["PlainText"]
                ?: error("PlainTextExporter nije registrovan!")

            exporter.generateReport(
                header = header,
                rows = rows,
                destination = "report.txt",
                title = "Employee Report",
                summary = mapOf("Count" to rows.size)
            )

            val (header2, rows2) = CsvDataReader.readCsvFromResources("test-csv.csv")
            val exporter2 = exportServices["PlainText"]
            exporter2?.generateReport(header2, rows2, "report2.txt", null, summary = mapOf("Count" to rows2.size))

            val exporter3 = exportServices["PDF"] ?: error("PDFExporter nije registrovan!")
            exporter3?.generateReport(header, rows, null, "Studenti", summary = mapOf("Count" to rows2.size))

            val exporter4 = exportServices["HTML"] ?: error("HTMLExporter nije registrovan!")
            val esbpCol = header.indexOf("ESPB")
            exporter4?.generateReport(header2, rows2, "reports.html", "Studenti", summary = mapOf(
                "Count" to DataCalculator.count(rows),
                "Average espb" to DataCalculator.average(rows, esbpCol),
                "Max espb" to DataCalculator.max(rows, esbpCol),
                "Espb over 60" to DataCalculator.countIf(rows, esbpCol) { it.toIntOrNull()?.let { espb -> espb > 60 } ?: false }
            ))

            val exporter5 = exportServices["Markdown"] ?: error("markdown nije registrovan!")
            exporter5?.generateReport(header, rows, "report.md",  "Studenti", summary = mapOf("Count" to rows2.size));

        } catch (e: Exception) {
            println("Greska: ${e.message}")
            e.printStackTrace()
        }
    }
}