package com.bennyhuo.github.network.entities

sealed class SubscriptionBody(val ignored: Boolean, val subscribed: Boolean)

object IGNORED: SubscriptionBody(true, false)

object  WATCH: SubscriptionBody(false, true)

//这里看上去用 enum 似乎更好，但实际上 enum 在 Gson 序列化 Json 的时候会直接 toString，而我们需要的是 { "ignored": true, "subscribed": true}
//所以直接的，不能用枚举；如果非要用枚举，可以自定义序列化方法
//enum class SubscriptionBody(val ignored: Boolean, val subscribed: Boolean){
//    IGNORED(true, false), WATCH(false, true)
//}