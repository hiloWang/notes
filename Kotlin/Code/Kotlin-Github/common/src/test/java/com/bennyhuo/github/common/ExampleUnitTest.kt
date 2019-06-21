package com.bennyhuo.github.common

import com.bennyhuo.github.common.ext.no
import com.bennyhuo.github.common.ext.otherwise
import com.bennyhuo.github.common.ext.yes
import org.junit.Assert
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    @Test
    fun testBoolean() {
       val resultOtherwise = false.yes{
            1
        }.otherwise{
            2
        }
        Assert.assertEquals(resultOtherwise, 2)
        val result = true.no{
            1
        }.otherwise{
            2
        }
        Assert.assertEquals(result, 2)
    }

    fun getABoolean() = false
}