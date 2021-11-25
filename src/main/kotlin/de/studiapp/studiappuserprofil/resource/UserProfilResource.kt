package de.studiapp.studiappuserprofil.resource

import org.springframework.web.bind.annotation.*

//Rest Endpoint
@RestController
@RequestMapping("/user")
//class UserProfilResource(val profileService: ProfileDataService) {
class UserProfilResource {
    @GetMapping
    fun getAllUserProfilData(): String {
       return "Liste"
    }
}


