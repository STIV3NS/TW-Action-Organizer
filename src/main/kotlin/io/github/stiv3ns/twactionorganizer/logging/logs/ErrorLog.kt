package io.github.stiv3ns.twactionorganizer.logging.logs

class ErrorLog private constructor(val text: String) : Log() {
    companion object {
        private val manager = Manager<ErrorLog>()

        suspend fun subscribe(callback: (ErrorLog)->Unit) {
            manager
                .subChannel
                .send(callback)
        }

        suspend fun trigger(text: String) {
            manager
                .logChannel
                .send( ErrorLog(text) )
        }
    }
}