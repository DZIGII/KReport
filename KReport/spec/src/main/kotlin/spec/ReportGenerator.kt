package spec

interface ReportGenerator {

    abstract val implName: String

    fun generateReport(
        header: List<String>,
        rows: List<List<String>>,
        destination: String?,
        title: String?,
        summary: Map<String, Any>
    ): String

    fun reportTitle(title: String) {

    }

    fun addTable(header: List<String>?, rows: List<List<String>>) {

    }

    fun addSummary(summary: Map<String, Any>) {

    }

}