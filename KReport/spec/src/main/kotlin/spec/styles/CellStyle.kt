package spec.styles

/**
 * Represents a set of visual style attributes applied to either a header cell
 * or a data column cell in report exporters (PDF, HTML, Markdown, PlainText).
 *
 * A CellStyle holds styling rules such as bold, italic and text color.
 * Each rule is internally stored inside the 'styles' map using StyleType keys.
 *
 * Usage example:
 * val style = CellStyle().apply {
 *     bold()
 *     italic()
 *     textColor("#FF0000")
 * }
 *
 * Exporters read these styles and translate them into appropriate output:
 * - PDF: font weight, font style, RGB color
 * - HTML: CSS inline styling
 * - Markdown: bold/italic markers (colors ignored)
 * - PlainText: styles ignored
 */

data class CellStyle(
    val styles: MutableMap<StyleType, String> = mutableMapOf()
) {
    fun bold() = apply { styles[StyleType.BOLD] = "true" }
    fun italic() = apply { styles[StyleType.ITALIC] = "true" }
    fun textColor(colorHex: String) = apply { styles[StyleType.COLOR_TEXT] = colorHex }
}
