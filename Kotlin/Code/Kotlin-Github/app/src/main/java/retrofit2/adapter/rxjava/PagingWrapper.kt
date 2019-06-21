package retrofit2.adapter.rxjava

abstract class PagingWrapper<T>{

    abstract fun getElements(): List<T>

    /*这里把 getElements 添加到 GitHubPaging中，是为了给上层使用*/
    val paging by lazy {
        GitHubPaging<T>().also { it.addAll(getElements()) }
    }
}