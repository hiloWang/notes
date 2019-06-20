package cn.kotliner.coroutine.ui

import cn.kotliner.coroutine.async.DownloadContext
import cn.kotliner.coroutine.async.我要开始加载图片啦
import cn.kotliner.coroutine.async.我要开始协程啦
import cn.kotliner.coroutine.async.我要开始耗时操作了
import cn.kotliner.coroutine.common.log
import javax.swing.JFrame.EXIT_ON_CLOSE

/**
 * Created by benny on 5/20/17.
 */
const val LOGO_URL = "http://www.imooc.com/static/img/index/logo.png?t=1.1"

fun main(args: Array<String>) {
    val frame = MainWindow()
    frame.title = "Coroutine@Bennyhuo"
    frame.setSize(200, 150)
    frame.isResizable = true
    frame.defaultCloseOperation = EXIT_ON_CLOSE
    frame.init()
    frame.isVisible = true

    frame.onButtonClick {
        log("协程之前")
        我要开始协程啦(DownloadContext(LOGO_URL)) {
            log("协程开始")
            try {
                val imageData = 我要开始耗时操作了 {
                    我要开始加载图片啦(this[DownloadContext]!!.url)
                }
                log("拿到图片")
                frame.setLogo(imageData)
            } catch(e: Exception) {
                e.printStackTrace()
            }
        }
        log("协程之后")
    }

//    frame.onButtonClick {
//        HttpService.service.getLogo(LOGO_URL)
//                .enqueue(object : Callback<ResponseBody> {
//                    override fun onResponse(
//                            call: Call<ResponseBody>,
//                            response: Response<ResponseBody>) {
//                        if (response.isSuccessful) {
//                            val imageData = response.body()?.byteStream()?.readBytes()
//                            if (imageData == null) {
//                                throw HttpException(HttpError.HTTP_ERROR_NO_DATA)
//                            } else {
//                                SwingUtilities.invokeLater {
//                                    frame.setLogo(imageData)
//                                }
//                            }
//                        } else {
//                            throw HttpException(response.code())
//                        }
//                    }
//
//                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                        throw HttpException(HttpError.HTTP_ERROR_UNKNOWN)
//                    }
//
//                })
//    }
}


