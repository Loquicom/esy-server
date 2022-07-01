package com.loqui.esy.utils

import com.loqui.esy.exception.EsyTokenException
import com.loqui.esy.service.dto.UserDTO
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.io.Encoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*


@Component
class JWTUtil(
    @Value("\${security.jwt.key}") private val base64key: String? = null
) {

    private val key: Key by lazy {
        val key = this.base64key ?: createKey()
        readKey(key)
    }

    fun generate(user: UserDTO): String {
        // Calc expiration date
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        // Build JWT token
        val builder = Jwts.builder()
        builder.setId(user.id.toString() + Date().time)
            .setSubject("User")
            .claim("id", user.id.toString())
            .claim("login", user.login)
            .claim("email", user.email)
            .claim("role", user.role.joinToString { "," })
            .setExpiration(calendar.time)
            .signWith(this.key, SignatureAlgorithm.HS256)
        return builder.compact()
    }

    fun verify(token: String): Boolean {
        val parser = Jwts.parserBuilder()
        return try {
            parser.setSigningKey(this.key)
                .build()
                .parse(token)
            true
        } catch (ex: JwtException) {
            false
        }
    }

    fun refresh(token: String): String {
        val user = getUser(token)
        return generate(user)
    }

    fun getUser(token: String, validate: Boolean = true): UserDTO {
        val payload = getPayload(token, validate)
        if (payload == null) {
            // TODO logger
            throw EsyTokenException("Invalid JWT: $token")
        }
        // Mapping user
        return UserDTO(
            UUID.fromString(payload.get("id", String::class.java)),
            payload.get("login", String::class.java),
            payload.get("email", String::class.java),
            payload.get("role", String::class.java).split(",")
        )
    }

    fun getKey(): String {
        return Encoders.BASE64.encode(this.key.encoded)
    }

    private fun createKey(): String {
        val key = Keys.secretKeyFor(SignatureAlgorithm.HS256)
        return Encoders.BASE64.encode(key.encoded)
    }

    private fun readKey(base64key: String): Key {
        val bytes = Decoders.BASE64.decode(base64key)
        return Keys.hmacShaKeyFor(bytes)
    }

    private fun getPayload(token: String, validate: Boolean = true): Claims? {
        if (token.isBlank()) {
            return null
        }
        return try {
            Jwts.parserBuilder().setSigningKey(this.key).build().parseClaimsJws(token).body
        } catch (ex: ExpiredJwtException) {
            var result: Claims? = null
            if (!validate) {
                result = ex.claims
            }
            result
        } catch (ex: JwtException) {
            // TODO logger
            null
        }
    }

}
