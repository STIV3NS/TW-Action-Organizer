package io.github.stiv3ns.twactionorganizer.twao.parsers

import io.github.stiv3ns.twactionorganizer.twao.Resources

interface AllyParser {
    fun parseAndGetResources(): Resources
}