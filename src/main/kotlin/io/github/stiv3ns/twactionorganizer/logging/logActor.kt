import io.github.stiv3ns.twactionorganizer.logging.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlin.reflect.KClass

@ObsoleteCoroutinesApi
internal fun CoroutineScope.logActor() = actor<LogMessage>(capacity = 128) {
    val subscriptions =
        mutableMapOf<KClass<out LogMessage>, MutableList<SendChannel<LogMessage>>>()

    for (msg in channel) {
        when (msg) {
            is SubscribeRequest ->
                subscriptions.getOrPut(
                    key = msg.messageType,
                    defaultValue = { mutableListOf() }
                ).add(msg.channel)

            is UnsubscribeRequest ->
                subscriptions[msg.messageType]?.remove(channel)

            else ->
                subscriptions[msg::class]?.forEach { it.send(msg) }
        }
    }
}