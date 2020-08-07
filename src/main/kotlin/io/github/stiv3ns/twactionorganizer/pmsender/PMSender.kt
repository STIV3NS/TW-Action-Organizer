package io.github.stiv3ns.twactionorganizer.pmsender

import java.awt.Robot
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent

object PMSender {
    private val robot = Robot()
    private val clipboard = Toolkit.getDefaultToolkit().systemClipboard

    fun send(topic: String, receiver: String, text: String) {
        setupField(Config.topicCoordX, Config.topicCoordY, topic)
        setupField(Config.receiverCoordX, Config.receiverCoordY, receiver)
        setupField(Config.textCoordX, Config.textCoordY, text)
    }

    private fun setupField(x: Int, y: Int, value: String) {
        copyToClipBoard(value)
        mouseMove(x, y)
        pasteClipboard()
    }

    private fun mouseMove(x: Int, y: Int) {
        robot.mouseMove(x, y)
    }

    private fun copyToClipBoard(value: String) {
        val stringSelection = StringSelection(value)

        clipboard.setContents(stringSelection, stringSelection)
    }

    private fun pasteClipboard() {
        robot.mousePress(MouseEvent.BUTTON1_MASK)
        robot.mouseRelease(MouseEvent.BUTTON1_MASK)

        robot.keyPress(KeyEvent.VK_CONTROL)
        robot.keyPress(KeyEvent.VK_V)

        robot.keyRelease(KeyEvent.VK_V)
        robot.keyRelease(KeyEvent.VK_CONTROL)
    }

    internal fun setAutoDelay(delay: Int) {
        robot.autoDelay = delay
    }
}