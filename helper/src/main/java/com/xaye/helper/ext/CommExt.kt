package com.xaye.helper.ext

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hjq.toast.ToastUtils
import com.xaye.helper.base.appContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * 作者　: hegaojian
 * 时间　: 2020/11/18
 * 描述　:一些常用的方法
 */


val gson: Gson by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { Gson() }

inline fun <reified T> String.toEntity(): T? {
    val type = object : TypeToken<T>() {}
    return try {
        gson.fromJson(this, type.type)
    } catch (e: Exception) {
        null
    }
}

inline fun <reified T> String.toArrayEntity(): ArrayList<T>? {
    val type = object : TypeToken<ArrayList<T>>() {}
    return try {
        gson.fromJson(this, type.type)
    } catch (e: Exception) {
        null
    }

}

fun Any?.toJsonStr(): String {
    return gson.toJson(this)
}

fun Any?.toast() {
    ToastUtils.show(this)
}

/**
 * 关闭键盘
 */
fun EditText.hideKeyboard() {
    val imm = appContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(
        this.windowToken,
        InputMethodManager.HIDE_IMPLICIT_ONLY
    )
}

/**
 * 打开键盘
 */
fun EditText.openKeyboard() {
    this.apply {
        isFocusable = true
        isFocusableInTouchMode = true
        requestFocus()
    }
    val inputManager =
        appContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.showSoftInput(this, 0)
}

/**
 * 关闭键盘焦点
 */
fun Activity.hideOffKeyboard() {
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (imm.isActive && this.currentFocus != null) {
        if (this.currentFocus?.windowToken != null) {
            imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}

fun toStartActivity(@NonNull clz: Class<*>) {
    val intent = Intent(appContext, clz)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    appContext.startActivity(intent)
}

fun toStartActivity(@NonNull clz: Class<*>, @NonNull bundle: Bundle) {
    val intent = Intent(appContext, clz)
    intent.apply {
        putExtras(bundle)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    appContext.startActivity(intent)
}

fun toStartActivity(activity: Activity, @NonNull clz: Class<*>, code: Int, @NonNull bundle: Bundle) {
    activity.startActivityForResult(Intent(appContext, clz).putExtras(bundle), code)
}

fun toStartActivity(fragment: Fragment, @NonNull clz: Class<*>, code: Int, @NonNull bundle: Bundle) {
    fragment.startActivityForResult(Intent(appContext, clz).putExtras(bundle), code)
}

fun toStartActivity(activity: Activity, @NonNull intent: Intent, code: Int) {
    activity.startActivityForResult(intent, code)
}

fun toStartActivity(@NonNull type: Any, @NonNull clz: Class<*>, code: Int, @NonNull bundle: Bundle) {
    if (type is Activity) {
        toStartActivity(type, clz, code, bundle)
    } else if (type is Fragment) {
        toStartActivity(type, clz, code, bundle)
    }
}

/**
 * 隐藏状态栏
 */
fun hideStatusBar(activity: Activity) {
    val attrs = activity.window.attributes
    attrs.flags = attrs.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
    activity.window.attributes = attrs
}

/**
 * 显示状态栏
 */
fun showStatusBar(activity: Activity) {
    val attrs = activity.window.attributes
    attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
    activity.window.attributes = attrs
}

/**
 * 横竖屏
 */
fun isLandscape(context: Context) =
    context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

/**
 * 应用商店
 */
fun gotoStore() {
    val uri =
        Uri.parse("market://details?id=" + appContext.packageName)
    val goToMarket = Intent(Intent.ACTION_VIEW, uri)
    try {
        goToMarket.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        appContext.startActivity(goToMarket)
    } catch (ignored: ActivityNotFoundException) {
    }
}

/**
 * 字符串相等
 */
fun isEqualStr(value: String?, defaultValue: String?) =
    if (value.isNullOrEmpty() || defaultValue.isNullOrEmpty()) false else TextUtils.equals(
        value,
        defaultValue
    )

/**
 * Int类型相等
 *
 */
fun isEqualIntExt(value: Int, defaultValue: Int) = value == defaultValue

fun getDrawableExt(id: Int): Drawable? = ContextCompat.getDrawable(appContext, id)

fun getColorExt(id: Int): Int = ContextCompat.getColor(appContext, id)

fun getStringExt(id: Int) = appContext.resources.getString(id)

fun getStringArrayExt(id: Int): Array<String> = appContext.resources.getStringArray(id)

fun getIntArrayExt(id: Int) = appContext.resources.getIntArray(id)

fun getDimensionExt(id: Int) = appContext.resources.getDimension(id)


/**
 * 倒计时扩展函数
 * @param total Int 倒计时的秒数 比如 10秒
 * @param onTick Function1<Int, Unit> 倒计时一次回调函数
 * @param onFinish Function0<Unit> 倒计时结束回调函数
 * @param scope CoroutineScope 作用域
 * @return Job
 */
fun countDownCoroutines(total: Int, onTick: (Int) -> Unit, onFinish: () -> Unit, scope: CoroutineScope = GlobalScope): Job {
    return flow {
        for (i in total downTo 1) {
            emit(i)
            delay(1000)
        }
    }.flowOn(Dispatchers.Default)
        .onCompletion { onFinish.invoke() }
        .onEach { onTick.invoke(it) }
        .flowOn(Dispatchers.Main)
        .launchIn(scope)
}