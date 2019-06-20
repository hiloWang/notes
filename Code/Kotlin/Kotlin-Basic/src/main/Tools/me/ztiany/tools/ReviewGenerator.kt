package me.ztiany.tools

import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.util.*

/**
 *
 * @author Ztiany
 *          Email ztiany3@gmail.com
 *          Date 2019/5/14 23:33
 */

const val HEADER = "| 今日 |  1 天前 |  4 天前 |  7 天前 | 15 天前 |"
const val TABLE = "| --- | --- | --- | --- | --- |"

const val FILE_NAME = "review.md"

fun main() {
    val start = Calendar.getInstance()
    val end = Calendar.getInstance()
    start.set(Calendar.MONTH, 4)
    start.set(Calendar.DAY_OF_MONTH, 1)

    end.time = start.time
    end.add(Calendar.MONTH, 1)

    createReviewPlan(start, end)
}

fun createReviewPlan(start: Calendar, end: Calendar) {
    val bw = BufferedWriter(OutputStreamWriter(FileOutputStream(File(FILE_NAME))))
    bw.use {
        it.write(HEADER)
        it.newLine()
        it.write(TABLE)
        it.newLine()

        val temp = Calendar.getInstance()

        while (start.before(end)) {
            temp.time = start.time

            it.write("| ")
            it.write("${start.get(Calendar.YEAR)}-${start.get(Calendar.MONTH) + 1}-${start.get(Calendar.DAY_OF_MONTH)}")
            it.write(" | ")

            temp.add(Calendar.DAY_OF_MONTH, -1)
            it.write("${temp.get(Calendar.YEAR)}-${temp.get(Calendar.MONTH) + 1}-${temp.get(Calendar.DAY_OF_MONTH)}")
            it.write(" | ")

            temp.add(Calendar.DAY_OF_MONTH, -2)
            it.write("${temp.get(Calendar.YEAR)}-${temp.get(Calendar.MONTH) + 1}-${temp.get(Calendar.DAY_OF_MONTH)}")
            it.write(" | ")

            temp.add(Calendar.DAY_OF_MONTH, -3)
            it.write("${temp.get(Calendar.YEAR)}-${temp.get(Calendar.MONTH) + 1}-${temp.get(Calendar.DAY_OF_MONTH)}")
            it.write(" | ")

            temp.add(Calendar.DAY_OF_MONTH, -8)
            it.write("${temp.get(Calendar.YEAR)}-${temp.get(Calendar.MONTH) + 1}-${temp.get(Calendar.DAY_OF_MONTH)}")
            it.write(" | ")

            it.newLine()
            start.add(Calendar.DAY_OF_MONTH, 1)
        }


    }
}

