package tech.pauly.fooder.data

import tech.pauly.fooder.data.domain.model.FoodError
import java.lang.Exception

// Domain level wrapper for any local/remote data operation. Essentially an Arrow `either` but with
// custom standard error type and fewer operations for simplicity/readability

sealed interface DataOperation<T> {
    data class Success<T>(val data: T): DataOperation<T>
    data class Failure<T>(val error: FoodError): DataOperation<T>

    fun <R> mapSuccess(transform: (T) -> R): DataOperation<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Failure -> Failure(error)
        }
    }

    fun onSuccess(block: (T) -> Unit): DataOperation<T> {
        if (this is Success) block(data)
        return this
    }

    fun onFailure(block: (FoodError) -> Unit): DataOperation<T> {
        if (this is Failure) block(error)
        return this
    }
}