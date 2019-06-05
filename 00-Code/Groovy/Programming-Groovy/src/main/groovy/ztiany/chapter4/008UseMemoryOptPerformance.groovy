package ztiany.chapter4

import groovy.transform.ToString
import jdk.nashorn.internal.ir.annotations.Immutable


//--------------------
//使用记忆化改进性能
//--------------------
/*
递归的本质是是使用子问题的解决方案来解决问题本身的方式，将问题分解为可以多次重复解决的若干部分，在执行期间
我们将子问题的结果保存起来，当调用到重复的计算时，直接使用相同的值，这就避免了重复运算，这被称作动态规划

卖杆业务：不同长度的杆子售价不同，我们会批发特定长度的杆子，比如说27英寸，然后将其分割成长度不一的杆销售，
已实现收入最大化：首先使用递归解决此问题，然后使用记忆化
 */

def timeIt(length, closure) {
    long start = System.nanoTime()
    println "max revenue for $length is ${closure(length)}"
    long end = System.nanoTime()
    println "time taken ${(end - start) / 1.0e9} seconds"
}

//定义一个列表，为各组长度的杆(0-30inch)的零售价，之所以包含0是为了弥合基于0的数组索引带来的问题（加入0后就可以以杆的长度来获取其价格了）
def radPrices = [
        0, 1, 3, 4, 5, 8, 9, 11, 12, 14,
        15, 15, 16, 18, 19, 15, 20, 21, 22, 24,
        25, 24, 26, 28, 29, 35, 37, 38, 39, 40
]
println radPrices.size()

def desiredLength = 27//desiredLength是我们采购的杆子的长度，如果直接售出其价格为：38
println radPrices[desiredLength]

//希望有一个程序来计算最优解
@Immutable
@ToString
class RevenueDetails {
    int revenue
    ArrayList splits

    RevenueDetails(int revenue, ArrayList splits) {
        this.revenue = revenue
        this.splits = splits
    }
}

//计算的方案是计算所有的可能性的值，取其中最大的值
def cutRad(prices, length) {
    if (length == 0) {
        new RevenueDetails(0, [])
    } else {
        def maxRevenueDetails = new RevenueDetails(Integer.MIN_VALUE, [])
        for (rodSize in 1..length) {
            def revenueFormSecondHalf = cutRad(prices, length - rodSize)
// 1 length-1 , 2, length -2, 3 - length-3,......

            def potentialRevenue = new RevenueDetails(
                    prices[rodSize] + revenueFormSecondHalf.revenue,
                    revenueFormSecondHalf.splits + rodSize
            )
            if (potentialRevenue.revenue > maxRevenueDetails.revenue) {
                maxRevenueDetails = potentialRevenue
            }
        }
        maxRevenueDetails

    }
}

/*
timeIt desiredLength, {
    length ->
        cutRad( radPrices, length)
}


max revenue for 27 is ztiany.chapter4.RevenueDetails(43, [5, 5, 5, 5, 5, 2])
time taken 59.813592001 senonds
 */


def cutRadOpt

cutRadOpt = {
    prices, length ->

        if (length == 0) {
            new RevenueDetails(0, [])
        } else {
            def maxRevenueDetails = new RevenueDetails(Integer.MIN_VALUE, [])
            for (rodSize in 1..length) {
                def revenueFormSecondHalf = cutRadOpt(prices, length - rodSize)
// 1 length-1 , 2, length -2, 3 - length-3,......

                def potentialRevenue = new RevenueDetails(
                        prices[rodSize] + revenueFormSecondHalf.revenue,
                        revenueFormSecondHalf.splits + rodSize
                )
                if (potentialRevenue.revenue > maxRevenueDetails.revenue) {
                    maxRevenueDetails = potentialRevenue
                }
            }
            maxRevenueDetails

        }
}.memoize()

timeIt desiredLength, {
    length->
        cutRadOpt(radPrices, length)
}
/*

max revenue for 27 is ztiany.chapter4.RevenueDetails(43, [5, 5, 5, 5, 5, 2])
time taken 0.341842142 seconds


将函数保存为闭包，并在其上调用memoize方法后，这样就创建了一个Memoize类，
该实例中有一个执行所提供闭包的引用，还有一个缓存结果，从结果可以看出，我们程序的性能的得到了大大的提升

记忆话技术是以控件换取速度，处于内存的考虑，可以使用memoize的变种：
memoizeAtMost方法代替，其使用lru淘汰算法


变种方法包括：
closure.memoizeAtLeast(100)
closure.memoizeAtMost(1000)
closure.memoizeBetween(100,1000)
 */



