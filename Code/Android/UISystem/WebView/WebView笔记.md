# WebView使用记录

## 1 使用WebView打开内部的Activity

1. 使用JsBridge
2. 使用隐式的Intent，在对应的Activity中添加IntentFilter，默认情况下WebView可以自动处理，但是当我们设置了setWebViewClient后，
则无法自动处理Activity跳转，这时需要重写shouldOverrideUrlLoading方法，然后判断url参数，根据传过来的url做对应的调整处理。