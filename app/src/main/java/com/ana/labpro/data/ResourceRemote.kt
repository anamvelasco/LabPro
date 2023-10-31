package com.ana.labpro.data

//Darle mejor gestión a Firebase
sealed class ResourceRemote<T>(
    var data: T? = null,
    var message: String? = null,
    var status: Status? = null,
    var errorCode: Int? = null
) {
    class Success<T>(data:T): ResourceRemote<T>(data = data, status = Status.Success)

    class Loading<T>(message: String = ""): ResourceRemote<T>(message = message, status = Status.Loading)

    class Error<T>(errorCode: Int? = null, message: String? = null): ResourceRemote <T>(errorCode = errorCode, message = message, status = Status.Error )
}

enum class Status{
    Success{
        override fun toString(): String {
            return this.name
        }

    },
    Error{
        override fun toString(): String {
            return this.name
        }
    },

    Loading{
        override fun toString(): String {
            return this.name
        }
    }
}