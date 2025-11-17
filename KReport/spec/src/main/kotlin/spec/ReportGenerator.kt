package spec

import spec.styles.StyleConfig

/**
 * Represents the core specification for all report generator components.
 *
 * This interface defines a unified contract that multiple report generator
 * implementations must follow. Each implementation is expected to export
 * tabular data into a specific output format (e.g., Plain Text, PDF, HTML,
 * Markdown, etc.).
 *
 * Implementations of this interface are discovered dynamically at runtime
 * using Java's [java.util.ServiceLoader] mechanism. To register a new exporter,
 * the implementer must:
 *  1. Provide a unique [implName].
 *  2. Implement the [generateReport] function.
 *  3. Declare the implementation in:
 *     `META-INF/services/spec.ReportGenerator`.
 *
 * This allows the system to remain modular and extensible without requiring
 * recompilation of the main application when new exporters are added.
 */

interface ReportGenerator {

    /**
     * A unique human-readable identifier for this implementation.
     *
     * Examples:
     *  - `"PlainText"` for a plain text exporter
     *  - `"PDF"` for a PDFBox-based exporter
     *  - `"HTML"` for an HTML generator
     *  - `"Markdown"` for a Markdown exporter
     *
     * The [implName] is used as a key inside factory logic or dynamic
     * runtime selection mechanisms to choose the appropriate exporter.
     */

    abstract val implName: String

    /**
     * Generates a formatted report using the provided data and optional settings.
     *
     * @param header
     *  A list of column names representing the header row of the report.
     *  Must be the same size as each row in [rows].
     *
     * @param rows
     *  A two-dimensional list representing the table body.
     *  Each inner list corresponds to one data row.
     *
     * @param destination
     *  Optional file system path where the generated report should be saved.
     *  If `null`, the exporter may choose not to write to disk and only return
     *  the generated report content as a [String].
     *
     * @param title
     *  Optional title displayed at the beginning of the report.
     *  Exporters may format the title differently depending on the file type.
     *
     * @param summary
     *  A map containing calculated summary metrics (e.g., count, sum, average).
     *  Keys represent metric names, and values represent computed results.
     *  The exporter is responsible for formatting and including these values
     *  in the output document.
     *
     * @return
     *   A string representation of the generated report. For binary formats
     *   (e.g., PDF), this typically returns a textual confirmation message
     *   while writing the actual file to [destination].
     *
     * ### Example usage:
     * ```
     * val exporter = ReportFactory.get("PlainText")
     * val result = exporter.generateReport(header, rows, "report.txt", "Title", summary)
     * ```
     */

    fun generateReport(
        header: List<String>,
        rows: List<List<String>>,
        destination: String?,
        title: String?,
        summary: Map<String, Any>,
        styles: StyleConfig = StyleConfig()
    ): String


}