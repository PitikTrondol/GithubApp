package com.mobile.android.dependencies.http

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.gson.jsonBody
import com.mobile.android.dependencies.logging.Trace
import com.mobile.android.handler.result.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

internal class FuelAdapter: HttpClient {

    private lateinit var requestClient : Request
    override fun get(url: String): HttpClient = apply {
        requestClient = Fuel.get(url)
    }

    override fun log(tag: String): HttpClient = apply{
        requestClient.also {request->
            Trace.e(tag, "$request")
        }
    }

    override fun header(header: String, value: Any): HttpClient = apply {
        requestClient.header(header, value)
    }

    override fun body(payload: Any): HttpClient = apply {
        requestClient.jsonBody(payload)
    }

    override fun dispatch(): Result<String> {
        return requestClient.responseString().third.fold(
            success = {json->
                Result.Success(json)
            },
            failure = {error->
                Result.Failure(error.extractError())
            }
        )
    }

    override fun dispatchAsFlow(): Flow<Result<String>> {
        return flow<Result<String>> {
            requestClient.responseString().third.fold(
                success = {json->
                    emit(Result.Success(json))
                },
                failure = {error->
                    emit(Result.Failure(error.extractError()))
                }
            )
        }.catch {
            emit(Result.Failure(NetworkError(it.message?:"Exception on Dispatch")))
        }
    }
}

internal fun FuelError.extractError(): ErrorEntity {

    // If any
    val body = this.response.body().asString("application/json")
    if(body.contains("<html>", true)){
        return ServiceUnavailable("Service Unavailable at the moment")
    }

    return when{
        body.contains("400", true)-> {
            BadRequest("Check Your Http Parameter")
        }
        body.contains("404", true)->{
            AccessDenied("Not Found")
        }
        body.contains("500", true)->{
            ServiceUnavailable("Service you've requested is not found")
        }
        else-> {
            Unknown("Unknown Error Please Contact the Police")
        }
    }
}