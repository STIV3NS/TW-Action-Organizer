package io.github.stiv3ns.twactionorganizer.localization.en

import io.github.stiv3ns.twactionorganizer.localization.interfaces.PMFormatterLocalization

object EN_PMFormatterLocalization
    : PMFormatterLocalization
{
    override val OPEN_SPOILER        = "[spoiler]"
    override val CLOSE_SPOILER       = "[/spoiler]"
    override val REQUIREMENTS_HEADER = "Villages in which you have to have certain number of nobles:"
    override val NOBLE_HEADER        = "[unit]snob[/unit][unit]axe[/unit] [b]OFF + 1x NOBLE[/b]"
    override val FAKENOBLE_HEADER    = "[unit]snob[/unit][unit]spy[/unit] [b]FAKE NOBLE x1[/b]"
    override val OFF_HEADER          = "[unit]ram[/unit] [b]OFF[/b]"
    override val FAKE_HEADER         = "[unit]spy[/unit] [b]FAKE[/b]"
    override val DEMOLITION_HEADER   = "[unit]catapult[/unit] [b]DEMOLITION[/b]"
    override val EXECUTION_TEXT      = "execute"
}