package de.studiapp.studiappuserprofil.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

@Service
class KeycloakRestService {
    @Autowired
    private val restTemplate: RestTemplate? = null

    @Value("\${keycloak.token-uri}")
    private val keycloakTokenUri: String? = null

    @Value("\${keycloak.user-info-uri}")
    private val keycloakUserInfo: String? = null

    @Value("\${keycloak.logout}")
    private val keycloakLogout: String? = null

    @Value("\${keycloak.client-id}")
    private val clientId: String? = null

    @Value("\${keycloak.authorization-grant-type}")
    private val grantType: String? = null

    @Value("\${keycloak.client-secret}")
    private val clientSecret: String? = null

    @Value("\${keycloak.scope}")
    private val scope: String? = null

    /**
     * login by using username and password to keycloak, and capturing token on response body
     *
     * @param username
     * @param password
     * @return
     */
    fun login(username: String?, password: String?): String? {
        val map: MultiValueMap<String, String> = LinkedMultiValueMap()
        map.add("username", username)
        map.add("password", password)
        map.add("client_id", clientId)
        map.add("grant_type", grantType)
        map.add("client_secret", clientSecret)
        map.add("scope", scope)
        val request = HttpEntity(map, HttpHeaders())
        return restTemplate!!.postForObject(keycloakTokenUri!!, request, String::class.java)
    }

    /**
     * a successful user token will generate http code 200, other than that will create an exception
     *
     * @param token
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun checkValidity(token: String): String? {
        return getUserInfo(token)
    }

    /**
     * logging out and disabling active token from keycloak
     *
     * @param refreshToken
     */
    @Throws(Exception::class)
    fun logout(refreshToken: String?) {
        val map: MultiValueMap<String, String> = LinkedMultiValueMap()
        map.add("client_id", clientId)
        map.add("client_secret", clientSecret)
        map.add("refresh_token", refreshToken)
        val request = HttpEntity(map, null)
        restTemplate!!.postForObject(keycloakLogout!!, request, String::class.java)
    }

    @Throws(Exception::class)
    fun getRoles(token: String): List<String?>? {
        val response = getUserInfo(token)
        println("response: $response")

        // get roles
        //val map: kotlin.collections.Map<String, *>
        val map = jacksonObjectMapper().readValue(response, HashMap::class.java)
        return map.get("roles") as List<String>
    }

    private fun getUserInfo(token: String): String? {
        val headers: MultiValueMap<String, String> = LinkedMultiValueMap()
        headers.add("Authorization", token)
        val map: MultiValueMap<String, String> = LinkedMultiValueMap()
        map.add("client_id", clientId)
        map.add("client_secret", clientSecret)
        val request = HttpEntity(map, headers)
        return restTemplate!!.postForObject(keycloakUserInfo!!, request, String::class.java)
    }
}



