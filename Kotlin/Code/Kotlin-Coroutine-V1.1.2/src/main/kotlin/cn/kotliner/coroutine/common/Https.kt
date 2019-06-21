package cn.kotliner.coroutine.common

/**
 * Created by benny on 5/20/17.
 */
object HttpError{
    const val HTTP_ERROR_NO_DATA = 999
    const val HTTP_ERROR_UNKNOWN = 998
}

data class HttpException(val code: Int): Exception()