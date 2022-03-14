package org.alberto97.ouilookup.tools

object StringInspector {
    private val delimiters = listOf(',', ';', ' ', '\n')

    /**
     * Split the input string with a set of predefined delimiters
     */
    fun splitForList(text: String): List<String> {
        return text.split(*delimiters.toCharArray()).filter { item -> item.isNotEmpty() }
    }

    /**
     * Given an input list of string returns how may items could be OUIs/MAC Addresses
     */
    fun countSearchable(list: List<String>): Int {
        val macAddressList = list.filter { clip -> OctetTool.isOui(clip) || OctetTool.isMacAddress(clip) }
        return macAddressList.count()
    }
}
