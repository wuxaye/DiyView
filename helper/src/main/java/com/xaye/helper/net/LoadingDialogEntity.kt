package com.xaye.helper.net

import kotlinx.coroutines.CoroutineScope


/**
 * 作者　: hegaojian
 * 时间　: 2020/11/3
 * 描述　:
 */
data class LoadingDialogEntity(
    @LoadingType var loadingType: Int = LoadingType.LOADING_NULL,
    var loadingMessage: String = "请求网络中...",
    var isShow: Boolean = false,
    var requestCode: String = "mmp",
    var coroutineScope: CoroutineScope? = null //请求绑定的作用域
)