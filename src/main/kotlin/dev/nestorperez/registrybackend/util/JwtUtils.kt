package dev.nestorperez.registrybackend.util

import com.google.gson.Gson
import java.util.*

/**
 * Checks for exp field on the jwt token for expiration.
 * If is expired returns true, otherwise false.
 *
 * gson is required in order to decode the token paylod.
 */
fun String.isJwtExpired(gson: Gson): Boolean {
    val payload = this.split(".")[1]
    val decodedPayload = Base64.getDecoder().decode(payload)
    val payloadMap = gson.fromJson(String(decodedPayload), Map::class.java)
    val iat = payloadMap["exp"] as Double
    val now = Date().time / 1000
    return iat < now
}