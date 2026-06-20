package userapi.tests

import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.Severity
import io.qameta.allure.SeverityLevel.BLOCKER
import io.qameta.allure.SeverityLevel.NORMAL
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import userapi.client.UserApi
import userapi.model.NewUser
import userapi.model.asCreateResult
import userapi.model.asUsers

@Epic("User API")
@Feature("List users")
class UserGetTest {

    private val api = UserApi()

    @Test
    @Severity(BLOCKER)
    @DisplayName("TC-19 created user appears in the list")
    fun `created user appears in the list`() {
        val user = NewUser.random()
        assertThat(api.create(user).then().asCreateResult().success).isTrue()

        val users = api.list().then().asUsers()

        assertThat(users).anyMatch { it.username == user.username }
    }

    @Test
    @Severity(BLOCKER)
    @DisplayName("TC-20 list returns 200 and a JSON array")
    fun `list returns json array`() {
        val users = api.list().then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .asUsers()

        assertThat(users).isNotNull()
    }

    @Test
    @Severity(BLOCKER)
    @DisplayName("TC-21 passwords are not exposed in the list")
    fun `password is not returned in the list`() {
        api.create(NewUser.random())

        val users = api.list().then().asUsers()

        assertThat(users).allMatch { it.password == null }
    }

    @Test
    @Severity(NORMAL)
    @DisplayName("TC-22 list item exposes the expected schema")
    fun `list item has expected schema`() {
        val user = NewUser.random()
        assertThat(api.create(user).then().asCreateResult().success).isTrue()

        val item = api.list().then().asUsers().first { it.username == user.username }

        assertThat(item.id).isNotNull()
        assertThat(item.email).isEqualTo(user.email)
        assertThat(item.createdAt).isNotNull()
        assertThat(item.updatedAt).isNotNull()
    }
}
