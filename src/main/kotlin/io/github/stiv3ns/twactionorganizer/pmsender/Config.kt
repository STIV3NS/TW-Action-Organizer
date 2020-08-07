package io.github.stiv3ns.twactionorganizer.pmsender

import java.util.prefs.*

object Config {
    var topicCoordX: Int
        private set
    var topicCoordY: Int
        private set

    var receiverCoordX: Int
        private set
    var receiverCoordY: Int
        private set

    var textCoordX: Int
        private set
    var textCoordY: Int
        private set

    var delay: Int
        private set

    private val prefs = Preferences.userNodeForPackage(this.javaClass)

    /* Preferences keys */
    private const val TOPIC_X = "topic_x"
    private const val TOPIC_Y = "topic_y"

    private const val RECEIVER_X = "receiver_x"
    private const val RECEIVER_Y = "receiver_y"

    private const val TEXT_X = "text_x"
    private const val TEXT_Y = "text_y"

    private const val KEY_DELAY = "delay"

    init {
        topicCoordX = prefs.getInt(TOPIC_X, 0)
        topicCoordY = prefs.getInt(TOPIC_Y, 0)

        receiverCoordX = prefs.getInt(RECEIVER_X, 0)
        receiverCoordY = prefs.getInt(RECEIVER_Y, 0)

        textCoordX = prefs.getInt(TEXT_X, 0)
        textCoordY = prefs.getInt(TEXT_Y, 0)

        delay = prefs.getInt(KEY_DELAY, 0)

        if (delay > 0)
            PMSender.setAutoDelay(delay)
    }

    fun setTopicCoords(newX: Int, newY: Int) {
        topicCoordX = newX
        topicCoordY = newY

        prefs.putInt(TOPIC_X, newX)
        prefs.putInt(TOPIC_Y, newY)
        prefs.flush()
    }

    fun setReceiverCoords(newX: Int, newY: Int) {
        receiverCoordX = newX
        receiverCoordY = newY

        prefs.putInt(RECEIVER_X, newX)
        prefs.putInt(RECEIVER_Y, newY)
        prefs.flush()
    }

    fun setTextCoords(newX: Int, newY: Int) {
        textCoordX = newX
        textCoordY = newY

        prefs.putInt(TEXT_X, newX)
        prefs.putInt(TEXT_Y, newY)
        prefs.flush()
    }

    fun setDelay(newValue: Int) {
        delay = newValue

        prefs.putInt(KEY_DELAY, newValue)
        prefs.flush()

        PMSender.setAutoDelay(delay)
    }
}