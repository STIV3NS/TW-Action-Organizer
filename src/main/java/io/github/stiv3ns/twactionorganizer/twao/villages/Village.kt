package io.github.stiv3ns.twactionorganizer.twao.villages

import io.github.stiv3ns.twactionorganizer.twao.World
import io.github.stiv3ns.twactionorganizer.twao.utils.exceptions.VillageNotFoundException
import kotlinx.serialization.*

import kotlinx.serialization.json.Json

interface Village {
    val x: Int
    val y: Int
    var id: Int?

    @Throws(VillageNotFoundException::class)
    fun initID(world: World) {
        id = world.fetchVillageID(this)
    }

    infix fun distanceTo(v2: Village): Int {
        fun Int.squared(): Int = this * this

        return (this.x - v2.x).squared() + (this.y - v2.y).squared()
    }
}

@ImplicitReflectionSerializer
@Serializer(forClass = Village::class)
object VillageSerializer {
    override val descriptor: SerialDescriptor
        get() = PrimitiveDescriptor("VillageSerializer", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Village {
        val map = Json.parseMap<String, Int>(decoder.decodeString())
        return object : Village {
            override val x = map["x"]!!
            override val y = map["y"]!!
            override var id: Int? = map["id"]!!

            override fun toString(): String = "$x|$y"
        }
    }

    override fun serialize(encoder: Encoder, v: Village) {
        val id = v.id ?: 0
        encoder.encodeString(Json.stringify( mapOf<String, Int>(
                "x" to v.x,
                "y" to v.y,
                "id" to id
        )))
    }
}
