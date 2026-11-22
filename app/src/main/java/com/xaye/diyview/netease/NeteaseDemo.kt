package com.xaye.diyview.netease

import android.app.Activity
import com.xaye.diyview.netease.canvas.splash.CanvasSplashActivity
import com.xaye.diyview.netease.canvas.bezier.CanvasBezierMultiActivity
import com.xaye.diyview.netease.canvas.bezier.CanvasBezierPathActivity
import com.xaye.diyview.netease.canvas.bezier.CanvasDragBubbleActivity
import com.xaye.diyview.netease.canvas.split.CanvasSplitActivity
import com.xaye.diyview.netease.canvas.transform.CanvasSaveRestoreActivity
import com.xaye.diyview.netease.canvas.transform.CanvasTransformActivity
import com.xaye.diyview.netease.paint.colorfilter.ColorFilterActivity
import com.xaye.diyview.netease.paint.colorfilter.ColorFilterViewActivity
import com.xaye.diyview.netease.paint.xfermode.XfermodeEraserActivity
import com.xaye.diyview.netease.paint.xfermode.XfermodeViewActivity
import com.xaye.diyview.netease.paint.xfermode.XfermodesActivity

/**
 * 示例合集中某个具体 UI 示例（叶子）的登记信息。
 *
 * @param title  菜单上展示的示例标题
 * @param target 点击后跳转到的示例 Activity
 */
data class NeteaseDemo(
    val title: String,
    val target: Class<out Activity>
)

/**
 * 示例合集中的一个功能分组（二级功能容器），如"颜色滤镜"下包含多个示例。
 *
 * @param title 分组标题（一级菜单展示）
 * @param demos 分组下的示例（二级菜单展示）
 */
data class NeteaseGroup(
    val title: String,
    val demos: List<NeteaseDemo>
)

/**
 * UI 示例集中注册表（两级）。
 *
 * 扩展方式：
 * - 新增一个功能分组：往 [groups] 追加一个 [NeteaseGroup]；
 * - 往已有分组加示例：在其 [NeteaseGroup.demos] 里加一条 [NeteaseDemo]。
 * 菜单与跳转逻辑都无需改动，只需把目标 Activity 在 AndroidManifest 中注册。
 */
object NeteaseRegistry {

    val groups: List<NeteaseGroup> = listOf(
        NeteaseGroup(
            title = "颜色滤镜",
            demos = listOf(
                NeteaseDemo("滤镜网格（RecyclerView）", ColorFilterActivity::class.java),
                NeteaseDemo("滤镜绘制（自定义View）", ColorFilterViewActivity::class.java)
            )
        ),
        NeteaseGroup(
            title = "图像混合（Xfermode）",
            demos = listOf(
                NeteaseDemo("基础混合（DST_IN）", XfermodeViewActivity::class.java),
                NeteaseDemo("橡皮擦刮奖（SRC_OUT）", XfermodeEraserActivity::class.java),
                NeteaseDemo("18种混合模式", XfermodesActivity::class.java)
            )
        ),
        NeteaseGroup(
            title = "Canvas 变换与状态保存",
            demos = listOf(
                NeteaseDemo("变换操作（平移/缩放/旋转/倾斜/切割）", CanvasTransformActivity::class.java),
                NeteaseDemo("状态保存与恢复（saveLayer）", CanvasSaveRestoreActivity::class.java)
            )
        ),
        NeteaseGroup(
            title = "粒子效果",
            demos = listOf(
                NeteaseDemo("图片粒子碎裂（轻触触发）", CanvasSplitActivity::class.java)
            )
        ),
        NeteaseGroup(
            title = "Canvas 操作案例",
            demos = listOf(
                NeteaseDemo("水波纹加载动画（SplashView）", CanvasSplashActivity::class.java)
            )
        ),
        NeteaseGroup(
            title = "贝塞尔曲线",
            demos = listOf(
                NeteaseDemo("二阶与三阶贝塞尔曲线", CanvasBezierPathActivity::class.java),
                NeteaseDemo("多阶贝塞尔曲线（de Casteljau）", CanvasBezierMultiActivity::class.java),
                NeteaseDemo("拖拽气泡（贝塞尔曲线应用）", CanvasDragBubbleActivity::class.java)
            )
        )
    )

    fun findGroup(title: String): NeteaseGroup? = groups.firstOrNull { it.title == title }
}
