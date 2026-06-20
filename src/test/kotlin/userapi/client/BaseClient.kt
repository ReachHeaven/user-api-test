package userapi.client

import io.qameta.allure.restassured.AllureRestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification
import userapi.config.Config

abstract class BaseClient {
    protected val request: RequestSpecification = RequestSpecBuilder()
        .setBaseUri(Config.baseUri)
        .addFilter(AllureRestAssured())
        .build()
}
