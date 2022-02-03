package org.alberto97.ouilookup.tools

object OuiSanitizer {
    fun sanitize(oui: String): String {
        return oui.removeOctetDelimiters().removeNic()
    }

    private fun String.removeOctetDelimiters(): String {
        return this.filterNot { c -> ":-".contains(c)}
    }

    // Only keep the OUI
    private fun String.removeNic(): String {
        return this.take(6)
    }
}
