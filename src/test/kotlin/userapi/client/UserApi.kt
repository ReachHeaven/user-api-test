package userapi.client

import io.restassured.RestAssured.given
import io.restassured.response.Response
import userapi.model.NewUser

class UserApi : BaseClient() {

    fun create(user: NewUser, extra: Map<String, String> = emptyMap()): Response =
        given(request).apply {
            user.username?.let { multiPart("username", it) }
            user.email?.let { multiPart("email", it) }
            user.password?.let { multiPart("password", it) }
            extra.forEach { (name, value) -> multiPart(name, value) }
        }.post("/user/create")

    fun list(): Response = given(request).get("/user/get")
}
