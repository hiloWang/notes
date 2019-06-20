package me.ztiany.releasable_var

import com.bennyhuo.kotlin.rnnv.releasableNotNull
import com.bennyhuo.kotlin.rnnv.release

/* ReleasableVar，可以为空的 Kotlin 非空类型 var：https://www.bennyhuo.com/2018/11/26/ReleasableVar/ */
class Bitmap {
    fun recycle() {}
}

class ReleaseableVar {

    var image by releasableNotNull<Bitmap>()

    fun onDestroy() {
        image.recycle()
        // You simply make the backing value null, thus making the gc of this Bitmap instance possible.
        ::image.release()
    }

}
