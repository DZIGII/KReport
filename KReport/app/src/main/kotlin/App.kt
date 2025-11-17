import spec.ReportGenerator
import spec.calculations.DataCalculator
import spec.calculations.DerivedColumnCalculator
import spec.styles.*
import java.util.*

object App {

    @JvmStatic
    fun main(args: Array<String>) {
        println("===== KReport Interactive CLI =====")

        val loader = ServiceLoader.load(ReportGenerator::class.java)
        val exporters = loader.associateBy { it.implName }

        println("Izaberi izvor podataka:")
        println("1) CSV fajl")
        println("2) Baza podataka")
        print("> ")

        val srcChoice = readLine()?.trim()
        val source = when (srcChoice) {
            "1" -> "csv"
            "2" -> "db"
            else -> error("Nepoznata opcija")
        }

        var header: List<String>
        var rows: List<List<String>>

        if (source == "csv") {
            print("Unesi putanju CSV fajla: ")
            val path = readLine()!!.trim()
            val (h, r) = CsvDataReader.readCsvFromResources(path)
            header = h
            rows = r
        } else {
            error("citanje iz baze jos nije implementirano")
        }

        println("\nIzaberi exporter:")
        exporters.keys.forEachIndexed { i, name ->
            println("${i + 1}) $name")
        }
        print("> ")

        val expChoice = readLine()!!.trim().toInt()
        val exporterName = exporters.keys.elementAt(expChoice - 1)
        val exporter = exporters[exporterName]!!

        println("Exporter izabran: $exporterName")

        print("\nDa li zelis naslov? (y/n): ")
        val title =
            if (readLine()?.trim()?.lowercase() == "y") {
                print("Unesi naslov: ")
                readLine()!!.trim()
            } else null


        val summary = mutableMapOf<String, Any>()

        print("\nDa li zelis summary? (y/n): ")
        if (readLine()?.trim()?.lowercase() == "y") {

            while (true) {
                println("\nIzaberi operaciju:")
                println("1) SUM kolone")
                println("2) AVERAGE kolone")
                println("3) MIN kolone")
                println("4) MAX kolone")
                println("5) COUNT svih redova")
                println("6) COUNT IF (po uslovu)")
                println("7) SUM IF (po uslovu)")
                println("ENTER za kraj")
                print("> ")

                val op = readLine()!!.trim()
                if (op.isEmpty()) break

                when (op) {

                    "1" -> {
                        println("Izaberi kolonu za SUM:")
                        header.forEachIndexed { i, h -> println("$i) $h") }
                        print("> ")
                        val col = readLine()!!.trim().toInt()
                        print("Unesi labelu za summary: ")
                        val lbl = readLine()!!.trim()
                        val result = DataCalculator.sum(rows, col)
                        summary[lbl] = result
                    }

                    "2" -> {
                        println("Izaberi kolonu za AVERAGE:")
                        header.forEachIndexed { i, h -> println("$i) $h") }
                        print("> ")
                        val col = readLine()!!.trim().toInt()
                        print("Unesi labelu: ")
                        val lbl = readLine()!!.trim()
                        val result = DataCalculator.average(rows, col)
                        summary[lbl] = result
                    }

                    "3" -> {
                        println("Izaberi kolonu za MIN:")
                        header.forEachIndexed { i, h -> println("$i) $h") }
                        print("> ")
                        val col = readLine()!!.trim().toInt()
                        print("Unesi labelu: ")
                        val lbl = readLine()!!.trim()
                        val result = DataCalculator.min(rows, col)
                        summary[lbl] = result
                    }

                    "4" -> {
                        println("Izaberi kolonu za MAX:")
                        header.forEachIndexed { i, h -> println("$i) $h") }
                        print("> ")
                        val col = readLine()!!.trim().toInt()
                        print("Unesi labelu: ")
                        val lbl = readLine()!!.trim()
                        val result = DataCalculator.max(rows, col)
                        summary[lbl] = result
                    }

                    "5" -> {
                        print("Unesi labelu za COUNT: ")
                        val lbl = readLine()!!.trim()
                        summary[lbl] = DataCalculator.count(rows)
                    }

                    "6" -> {
                        println("Izaberi kolonu za COUNT IF:")
                        header.forEachIndexed { i, h -> println("$i) $h") }
                        print("> ")
                        val col = readLine()!!.trim().toInt()

                        print("Unesi uslov (primer: >60, <10, ==5, >=100): ")
                        val cond = readLine()!!.trim()

                        val predicate = buildPredicate(cond)

                        print("Unesi labelu: ")
                        val lbl = readLine()!!.trim()

                        val result = DataCalculator.countIf(rows, col, predicate)
                        summary[lbl] = result
                    }

                    "7" -> {
                        println("Izaberi kolonu za SUM IF:")
                        header.forEachIndexed { i, h -> println("$i) $h") }
                        print("> ")
                        val col = readLine()!!.trim().toInt()

                        print("Unesi uslov (primer: >60, <10, ==5, >=100): ")
                        val cond = readLine()!!.trim()

                        val predicate = buildPredicate(cond)

                        print("Unesi labelu: ")
                        val lbl = readLine()!!.trim()

                        val result = DataCalculator.sumIf(rows, col, predicate)
                        summary[lbl] = result
                    }

                    else -> println("Nepoznata opcija.")
                }
            }
        }


        val styles = StyleConfig()

        print("\nDa li zelis stilove? (y/n): ")
        if (readLine()?.trim()?.lowercase() == "y") {
            println("Unesi stilove (npr. header:0:bold:red), ENTER za kraj:")
            while (true) {
                print("> ")
                val line = readLine()!!.trim()
                if (line.isEmpty()) break
                applyStyleArg(line, styles)
            }
        }


        print("\nDa li zelis izvedene kolone? (y/n): ")
        if (readLine()?.trim()?.lowercase() == "y") {
            println("Primer: Total = ESPB + Godine")
            println("Unesi izvedene kolone, ENTER za kraj:")
            while (true) {
                print("> ")
                val expr = readLine()!!.trim()
                if (expr.isEmpty()) break

                val parts = expr.split("=")
                if (parts.size != 2) error("Format mora biti: NewCol = expression")

                val newName = parts[0].trim()
                val formula = parts[1].trim()

                val (nh, nr) = DerivedColumnCalculator.addDerivedColumn(
                    header,
                    rows,
                    formula,
                    newName
                )

                header = nh
                rows = nr
            }
        }


        print("\nUnesi output putanju fajla: ")
        val outputPath = readLine()!!.trim()



        exporter.generateReport(
            header = header,
            rows = rows,
            destination = outputPath,
            title = title,
            summary = summary,
            styles = styles
        )

        println("Izvestaj sacuvan u: $outputPath")
    }

    private fun applyStyleArg(arg: String, styles: StyleConfig) {
        val parts = arg.split(":")
        if (parts.size < 3) error("Neispravan format: $arg")

        val target = parts[0]
        val index = parts[1].toInt()
        val styleParts = parts.drop(2)

        val style = CellStyle()

        styleParts.forEach {
            when {
                it.equals("bold", true) -> style.bold()
                it.equals("italic", true) -> style.italic()
                it.startsWith("#") -> style.textColor(it)
                else -> error("Nepoznata stil opcija: $it")
            }
        }

        when (target) {
            "header" -> styles.headerStyles[index] = style
            "col" -> styles.columnStyles[index] = style
            else -> error("Nepoznat target: $target")
        }
    }

    private fun buildPredicate(expr: String): (String) -> Boolean {
        val ops = listOf(">=", "<=", "==", "!=", ">", "<")

        val op = ops.firstOrNull { expr.contains(it) }
            ?: error("Invalid condition: $expr")

        val parts = expr.split(op)
        if (parts.size != 2) error("Invalid condition: $expr")

        val value = parts[1].trim().toDoubleOrNull()
            ?: error("Right side must be numeric")

        return { cell ->
            val num = cell.toDoubleOrNull()
            if (num == null) false
            else when (op) {
                ">"  -> num > value
                "<"  -> num < value
                ">=" -> num >= value
                "<=" -> num <= value
                "==" -> num == value
                "!=" -> num != value
                else -> false
            }
        }
    }



}
