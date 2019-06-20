package ztiany.chapter19

//--------------------
//分类与DSL
//--------------------

/*
使用分类可以以可控的方式拦截方法的调用，创建DSL也可以使用分类，比如实现如下流畅的调用：
            4.days.ago.at(4:43)
 */

class DateUtils{
    static int getDays(Integer self) {
        self
    }

    static Calendar getAgo(Integer self) {
        def date = Calendar.instance
        date.add(Calendar.DAY_OF_MONTH, -self)
        date
    }

    static Date at(Calendar self, Map time){
        def hour =0
        def minute = 0
        time.each {
            key,value->
                hour = key.toInteger()
                minute = value.toInteger()
        }
        self.set(Calendar.HOUR_OF_DAY,hour)
        self.set(Calendar.MINUTE,minute)
        self.set(Calendar.SECOND,0)
        self.time
    }
}

use(DateUtils){
    println 4.days.ago.at(5:30)
}

