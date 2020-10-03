package io.github.stiv3ns.twactionorganizer.localization.pl

import io.github.stiv3ns.twactionorganizer.localization.interfaces.PMFormatterLocalization

object PL_PMFormatterLocalization
    : PMFormatterLocalization
{
    override val OPEN_SPOILER        = "[spoiler]"
    override val CLOSE_SPOILER       = "[/spoiler]"
    override val REQUIREMENTS_HEADER = "Wioski w których musisz postawić określoną liczbę szlachty:"
    override val NOBLE_HEADER        = "[unit]snob[/unit][unit]axe[/unit] [b]PELNY OFF + 1x SZLACHCIC[/b]"
    override val FAKENOBLE_HEADER    = "[unit]snob[/unit][unit]spy[/unit] [b]FAKE SZLACHCIC x1[/b]"
    override val OFF_HEADER          = "[unit]ram[/unit] [b]OFF[/b]"
    override val FAKE_HEADER         = "[unit]spy[/unit] [b]FAKE[/b]"
    override val DEMOLITION_HEADER   = "[unit]catapult[/unit] [b]BURZENIE[/b]"
    override val EXECUTION_TEXT      = "wykonaj"
}