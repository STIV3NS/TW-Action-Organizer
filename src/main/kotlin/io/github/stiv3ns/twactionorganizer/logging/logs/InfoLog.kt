package io.github.stiv3ns.twactionorganizer.logging.logs

class InfoLog private constructor(val text: String) : Log() {
    companion object {
        private val manager = Manager<InfoLog>()

        suspend fun subscribe(callback: (InfoLog)->Unit) {
            manager
                .subChannel
                .send(callback)
        }

        suspend fun trigger(text: String) {
            manager
                .logChannel
                .send( InfoLog(text) )
        }
    }
}