package spec.styles

/**
 * Container for all styling rules applied to a report.
 *
 * Styles can be applied independently to:
 *  - Header cells (by column index)
 *  - Data columns (by column index)
 *
 * Usage example:
 * val config = StyleConfig().apply {
 *     headerStyles[0] = CellStyle().apply { bold() }
 *     columnStyles[2] = CellStyle().apply { italic(); textColor("#008000") }
 * }
 *
 * Exporters query StyleConfig during rendering:
 * styles.headerStyles[columnIndex]
 * styles.columnStyles[columnIndex]
 *
 * If a style is not provided for a given index, default styling is used.
 */

data class StyleConfig(
    val headerStyles: MutableMap<Int, CellStyle> = mutableMapOf(),
    val columnStyles: MutableMap<Int, CellStyle> = mutableMapOf()
)