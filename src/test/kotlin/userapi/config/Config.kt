package userapi.config

object Config {
    val baseUri: String =
        (System.getProperty("baseUri") ?: System.getenv("BASE_URI"))
            ?.trim()
            ?.takeIf { it.isNotBlank() }
            ?: error("Base URI is not set. Pass -DbaseUri=<url> or set the BASE_URI environment variable.")
}
