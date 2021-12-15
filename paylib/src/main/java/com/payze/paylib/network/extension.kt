package com.payze.paylib.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

fun <T : Any> Call<T>.sendRequest(
    success: (() -> Unit)? = null,
    successWithData: ((data: T?) -> Unit)? = null,
    failure: ((t: Throwable) -> Unit),
    responseFailure: (code: Int, error: String) -> Unit = { _: Int, _: String -> },
    onConnectionFailure: (Throwable) -> Unit,
    finally: ((success: Boolean) -> Unit)? = null
) {
    enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>, t: Throwable) {
            finally?.invoke(false)
            if (t is IOException) {
                onConnectionFailure(t)
            } else {
                failure.invoke(t)
            }
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            finally?.invoke(true)
            if (response.isSuccessful) {
                success?.invoke()
                val body = response.body()
                if (successWithData != null)
                    successWithData(body)
            } else {
                responseFailure(response.code(), response.message())
            }
        }
    })
}

