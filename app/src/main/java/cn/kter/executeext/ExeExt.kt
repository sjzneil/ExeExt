package cn.kter.executeext


/**
 * author : ning
 * company：inkr
 * desc   :
 */
class NetStatus(var error: Exception?, var willDone: Boolean = false)
typealias Executable<T> = suspend () -> T
typealias ReturnBlock<T> = suspend (T) -> Unit

/**
 * 同步处理网络请求流程和ui显示流程
 */
suspend infix fun <T> Executable<T>.execute( backBlock: ReturnBlock<T>): NetStatus {
    val data: T
    try {
        data = this.invoke()
    } catch (e: Exception) {
        e.printStackTrace()
        return NetStatus(e)
    }
    backBlock(data)
    return NetStatus(null)
}


/**
 * 筛选异常类型
 */
infix fun NetStatus.filter(block: () -> Array<Class<*>>): NetStatus {
    val clazzes = block()
    clazzes.forEach {
        this.error?.javaClass?.apply {
            if (it.isAssignableFrom(error?.javaClass).orFalse()) {
                return NetStatus(error, true)
            }
        }
    }
    return this
}

/**
 * 处理
 */
infix fun NetStatus.then(block: (Exception) -> Unit): NetStatus {
    if (willDone) {
        error?.apply {
            block(error!!)
        }
    }
    return NetStatus(error, false)
}
/**
 * 如果为 null 则为 false
 */
fun Boolean?.orFalse(): Boolean {
    return this ?: false
}