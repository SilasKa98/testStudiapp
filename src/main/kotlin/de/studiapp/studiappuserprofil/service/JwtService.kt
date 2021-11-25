package de.studiapp.studiappuserprofil.service

import com.auth0.jwk.Jwk
import com.auth0.jwk.UrlJwkProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.cache.annotation.Cacheable;

import java.net.URL

@Service
class JwtService {
    @Value("\${keycloak.jwk-set-uri}")
    private val jwksUrl: String? = null

    @Value("\${keycloak.certs-id}")
    private val certsId: String? = null

    @Cacheable(value = ["jwkCache"])
    @Throws(Exception::class)
    fun getJwk(): Jwk {
        return UrlJwkProvider(URL(jwksUrl))[certsId]
    }
}