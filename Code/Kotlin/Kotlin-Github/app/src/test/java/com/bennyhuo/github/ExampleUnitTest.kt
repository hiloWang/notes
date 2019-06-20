package com.bennyhuo.github

import okhttp3.HttpUrl
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {


    @Test
    fun addition_isCorrect() {

        val URL_PATTERN = """(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"""

        assertEquals(4, 2 + 2)

        val header = "<https://api.github.com/search/code?q=addClass+user%3Amozilla&page=15>; rel=\"next\",\n" +
                "  <https://api.github.com/search/code?q=addClass+user%3Amozilla&page=34>; rel=\"last\",\n" +
                "  <https://api.github.com/search/code?q=addClass+user%3Amozilla&page=1>; rel=\"first\",\n" +
                "  <https://api.github.com/search/code?q=addClass+user%3Amozilla&page=13>; rel=\"prev\""

        Regex("""<(${URL_PATTERN})>; rel="(\w+)"""").findAll(header).asIterable()
                .map { matchResult ->
                    val url = matchResult.groupValues[1]
                    println(url)
                    println(matchResult.groupValues)

                    if (url.contains("since")) {
                        HttpUrl.parse(url)?.queryParameter("since")?.let {
                            println(it.toInt())
                        }
                    }
                }

        /*
        //整体、第一组、第二组...第三组
[<https://api.github.com/search/code?q=addClass+user%3Amozilla&page=15>; rel="next", https://api.github.com/search/code?q=addClass+user%3Amozilla&page=15, https, next]
[<https://api.github.com/search/code?q=addClass+user%3Amozilla&page=34>; rel="last", https://api.github.com/search/code?q=addClass+user%3Amozilla&page=34, https, last]
[<https://api.github.com/search/code?q=addClass+user%3Amozilla&page=1>; rel="first", https://api.github.com/search/code?q=addClass+user%3Amozilla&page=1, https, first]
[<https://api.github.com/search/code?q=addClass+user%3Amozilla&page=13>; rel="prev", https://api.github.com/search/code?q=addClass+user%3Amozilla&page=13, https, prev]
       */

    }

}
