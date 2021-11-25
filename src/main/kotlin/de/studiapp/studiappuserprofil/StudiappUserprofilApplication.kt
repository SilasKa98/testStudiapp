package de.studiapp.studiappuserprofil

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StudiappUserprofilApplication

fun main(args: Array<String>) {
    runApplication<StudiappUserprofilApplication>(*args)
}

//Zum erreichen der Swagger Documentation zur UserProfil Service API: http://localhost:8080/swagger-ui.html
