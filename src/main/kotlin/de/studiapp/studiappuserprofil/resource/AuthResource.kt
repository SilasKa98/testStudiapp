package de.studiapp.studiappuserprofil.resource

import com.auth0.jwk.Jwk;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import de.studiapp.studiappuserprofil.service.JwtService
import de.studiapp.studiappuserprofil.service.KeycloakRestService

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.interfaces.RSAPublicKey;
import java.util.*;



//import von eigener Klasse?! --> evtl. mal auskommentieren @Torsten

//Rest Endpoint
@RestController
@RequestMapping("/auth")
class AuthResource(val authDataService: KeycloakRestService) {

    private val logger: Logger = LoggerFactory.getLogger(AuthResource::class.java)

    @Autowired
    private val restService: KeycloakRestService? = null

    @Autowired
    private val jwtService: JwtService? = null

    @GetMapping("/student")
    fun student(@RequestHeader("Authorization") authHeader: String?): HashMap<*, *>? {
        return try {
            val roles: List<String?>? = restService!!.getRoles(authHeader!!) as List<String?>
            if (!roles!!.contains("student")) throw Exception("not a student role")
            object : HashMap<Any?, Any?>() {
                init {
                    put("role", "student")
                }
            }
        } catch (e: Exception) {
            logger.error("exception : {} ", e.message)
            object : HashMap<Any?, Any?>() {
                init {
                    put("status", "forbidden")
                }
            }
        }
    }


    @GetMapping("/teacher")
    fun teacher(@RequestHeader("Authorization") authHeader: String): HashMap<*, *>? {
        return try {
            val jwt = JWT.decode(authHeader.replace("Bearer", "").trim { it <= ' ' })

            // check JWT is valid
            val jwk: Jwk = jwtService!!.getJwk()
            val algorithm: Algorithm = Algorithm.RSA256(jwk.publicKey as RSAPublicKey, null)
            algorithm.verify(jwt)

            // check JWT role is correct
            val roles: List<String>? = jwt.getClaim("realm_access").asMap()["roles"] as List<String>?
            if (!roles!!.contains("teacher")) throw Exception("not a teacher role")

            // check JWT is still active
            val expiryDate = jwt.expiresAt
            if (expiryDate.before(Date())) throw Exception("token is expired")

            // all validation passed
            object : HashMap<Any?, Any?>() {
                init {
                    put("role", "teacher")
                }
            }
        } catch (e: Exception) {
            logger.error("exception : {} ", e.message)
            object : HashMap<Any?, Any?>() {
                init {
                    put("status", "forbidden")
                }
            }
        }
    }


    @GetMapping("/valid")
    fun valid(@RequestHeader("Authorization") authHeader: String?): HashMap<*, *>? {
        return try {
            restService!!.checkValidity(authHeader!!)
            object : HashMap<Any?, Any?>() {
                init {
                    put("is_valid", "true")
                }
            }
        } catch (e: Exception) {
            logger.error("token is not valid, exception : {} ", e.message)
            object : HashMap<Any?, Any?>() {
                init {
                    put("is_valid", "false")
                }
            }
        }
    }

    @PostMapping(value = ["/login"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun login(username: String?, password: String?): String? {
        return restService!!.login(username, password)
    }

    @PostMapping(value = ["/logout"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun logout(@RequestParam(value = "refresh_token", name = "refresh_token") refreshToken: String?): Map<*, *>? {
        return try {
            restService!!.logout(refreshToken)
            object : HashMap<Any?, Any?>() {
                init {
                    put("logout", "true")
                }
            }
        } catch (e: Exception) {
            logger.error("unable to logout, exception : {} ", e.message)
            object : HashMap<Any?, Any?>() {
                init {
                    put("logout", "false")
                }
            }
        }
    }
}