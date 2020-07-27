package io.github.stiv3ns.twactionorganizer.core.parsers

import io.github.stiv3ns.twactionorganizer.core.Resources

interface AllyParser {
    fun parseAndGetResources(): Resources
}