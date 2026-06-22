package userapi.tests

import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.Severity
import io.qameta.allure.SeverityLevel.BLOCKER
import io.qameta.allure.SeverityLevel.CRITICAL
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.not
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import userapi.client.UserApi
import userapi.model.NewUser
import userapi.model.asCreateError
import userapi.model.asCreateResult

@Epic("User API")
@Feature("Create user")
class UserCreateTest {
    private val api = UserApi()

    @Test
    @Severity(BLOCKER)
    @DisplayName("TC-01 valid data registers the user")
    fun `valid data registers the user`() {
        val result = api.create(NewUser.random()).then().statusCode(201).asCreateResult()

        assertThat(result.success).isTrue()
        assertThat(result.details?.id).isNotNull()
    }

    @Test
    @Severity(BLOCKER)
    @DisplayName("TC-02 password is not returned on create")
    fun `password is not returned on create`() {
        val result = api.create(NewUser.random()).then().asCreateResult()

        assertThat(result.details?.password).isNull()
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidRequiredFields")
    @Severity(CRITICAL)
    @DisplayName("TC-04/05/08/11 missing or empty required field is rejected")
    fun `required field is enforced`(case: String, user: NewUser) {
        val error = api.create(user).then().statusCode(400).asCreateError()

        assertThat(error.success).isFalse()
        assertThat(error.message).isNotEmpty()
    }

    @Test
    @Severity(CRITICAL)
    @DisplayName("TC-07 duplicate username is rejected")
    fun `duplicate username is rejected`() {
        val existing = NewUser.random()
        assertThat(api.create(existing).then().asCreateResult().success).isTrue()

        api.create(NewUser.random().copy(username = existing.username)).then().statusCode(400)
    }

    @Test
    @Severity(CRITICAL)
    @DisplayName("TC-09 malformed email is rejected")
    fun `malformed email is rejected`() {
        val user = NewUser.random()
        api.create(user.copy(email = "not-an-email-${user.username}")).then().statusCode(400)
    }

    @Test
    @Severity(CRITICAL)
    @DisplayName("TC-10 duplicate email is rejected")
    fun `duplicate email is rejected`() {
        val existing = NewUser.random()
        assertThat(api.create(existing).then().asCreateResult().success).isTrue()

        api.create(NewUser.random().copy(email = existing.email)).then().statusCode(400)
    }

    @Test
    @Severity(CRITICAL)
    @DisplayName("TC-12 password shorter than minimum is rejected")
    fun `short password is rejected`() {
        api.create(NewUser.random().copy(password = "x")).then().statusCode(400)
    }

    @Test
    @Severity(CRITICAL)
    @DisplayName("TC-14 unknown field is rejected without a 500 or DB-internals leak")
    fun `unknown field does not crash the server or leak internals`() {
        val body = api.create(NewUser.random(), extra = mapOf("role" to "admin"))
            .then().statusCode(not(500))
            .extract().body().asString()

        assertThat(body).doesNotContain("SQLITE", "no column")
    }

    companion object {
        @JvmStatic
        fun invalidRequiredFields(): List<Arguments> {
            val valid = NewUser.random()
            return listOf(
                Arguments.of("TC-04 username missing", valid.copy(username = null)),
                Arguments.of("TC-05 username empty", valid.copy(username = "")),
                Arguments.of("TC-08 email missing", valid.copy(email = null)),
                Arguments.of("TC-11 password missing", valid.copy(password = null)),
            )
        }
    }
}
