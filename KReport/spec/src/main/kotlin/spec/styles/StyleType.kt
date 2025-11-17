package spec.styles

/**
 * Enumerates different types of styling rules supported by the report system.
 *
 * These identifiers are used as keys inside CellStyle to describe styling
 * operations in an exporter-agnostic format.
 *
 * Exporters translate each type depending on their output format.
 *
 * BOLD       -> bold font weight
 * ITALIC     -> italic font style
 * COLOR_TEXT -> text color (hex format)
 */

enum class StyleType {
    BOLD,
    ITALIC,
    COLOR_TEXT
}