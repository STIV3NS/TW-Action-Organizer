package io.github.stiv3ns.twactionorganizer.logging

import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.runBlocking
import logActor
import kotlin.reflect.KClass

@ObsoleteCoroutinesApi
object Logger {
    private val log: SendChannel<LogMessage> by lazy {
        runBlocking { logActor() }
    }

    suspend fun info(text: String) {
        log.send(Info(text))
    }

    suspend fun warn(text: String) {
        log.send(Warn(text))
    }

    suspend fun error(text: String) {
        log.send(Error(text))
    }

    suspend fun subscribe(messageType: KClass<LogMessage>, channel: SendChannel<LogMessage>) {
        log.send( SubscribeRequest(messageType, channel) )
    }

    suspend fun unSubscribe(messageType: KClass<LogMessage>, channel: SendChannel<LogMessage>) {
        log.send( UnsubscribeRequest(messageType, channel) )
    }
}