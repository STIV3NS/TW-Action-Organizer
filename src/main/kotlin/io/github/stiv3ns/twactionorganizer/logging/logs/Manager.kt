package io.github.stiv3ns.twactionorganizer.logging.logs

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select

internal class Manager<T : Log>(
    bufferSize: Int = 32
) : CoroutineScope
{
    override val coroutineContext = Dispatchers.Default

    private val _subChannel = Channel<(T)->Unit>()
    private val _logChannel = Channel<T>(capacity = bufferSize)

    internal val subChannel: SendChannel<(T)->Unit> = _subChannel
    internal val logChannel: SendChannel<T> = _logChannel

    init {
        manage()
    }

    private fun manage() {
        launch {
            val subscriptions = mutableListOf<(T)->Unit>()

            while (true) {
                select<Unit> {
                    _subChannel.onReceive { callback -> subscriptions.add(callback) }
                    _logChannel.onReceive { log ->
                        subscriptions.forEach { callback -> callback(log) }
                    }
                }
            }
        }
    }
}