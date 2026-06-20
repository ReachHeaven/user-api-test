package userapi.model

import java.util.UUID


data class NewUser(
    val username: String? = null,
    val email: String? = null,
    val password: String? = null,
) {
    companion object {
        fun random(): NewUser = UUID.randomUUID().toString().take(8).let { id ->
            NewUser("user_$id", "user_$id@example.com", "Secret123")
        }
    }
}
