package io.github.stiv3ns.twactionorganizer.logging.logs

class WarnLog private constructor(val text: String) : Log() {
    companion object {
        private val manager = Manager<WarnLog>()

        suspend fun subscribe(callback: (WarnLog)->Unit) {
            manager
                .subChannel
                .send(callback)
        }

        suspend fun trigger(text: String) {
            manager
                .logChannel
                .send( WarnLog(text) )
        }
    }
}