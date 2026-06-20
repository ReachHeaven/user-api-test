package userapi.model

import com.google.gson.annotations.SerializedName
import io.restassured.response.ValidatableResponse

data class UserDto(
    val id: Long? = null,
    val username: String? = null,
    val email: String? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("updated_at") val updatedAt: String? = null,
    val password: String? = null,
)

data class CreateResult(
    val success: Boolean = false,
    val message: String? = null,
    val details: UserDto? = null,
)

data class CreateError(
    val success: Boolean = false,
    val message: List<String> = emptyList(),
)

fun ValidatableResponse.asCreateResult(): CreateResult = extract().`as`(CreateResult::class.java)

fun ValidatableResponse.asCreateError(): CreateError = extract().`as`(CreateError::class.java)

fun ValidatableResponse.asUsers(): List<UserDto> = extract().`as`(Array<UserDto>::class.java).toList()
