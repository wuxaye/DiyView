# DIY ANDROID VIEW

练习安卓自定义 View，此项目会持续更新，加入更多自定义 View。

> 每个示例仅保留 **简介** 与 **代码路径**（不再逐一贴图，避免示例变多后难以维护）。点击「代码」跳转到对应源码目录。

## 文本 / 字幕

| 示例 | 简介 | 代码 |
|---|---|---|
| [文字变色](https://juejin.cn/post/7436217936564273178) | 实现文字渐变变色效果 | [代码](https://github.com/wuxaye/DiyView/tree/master/app/src/main/java/com/xaye/diyview/view/tv) |
| [文字变色应用](https://juejin.cn/post/7436370478519042063) | 将文字渐变效果应用于 ViewPager 实际场景 | [代码](https://github.com/wuxaye/DiyView/tree/master/app/src/main/java/com/xaye/diyview/view/trackTvViewpage) |
| [文字跑马灯](https://juejin.cn/post/7478665093385289754) | 多段文本跑马灯展示 | [代码](https://github.com/wuxaye/DiyView/tree/master/app/src/main/java/com/xaye/diyview/view/marquee) |

## 进度 / 加载

| 示例 | 简介 | 代码 |
|---|---|---|
| [环形进度条](https://juejin.cn/post/7436667191539466250) | 自定义环形进度条，可动态显示进度 | [代码](https://github.com/wuxaye/DiyView/tree/master/app/src/main/java/com/xaye/diyview/view/progress) |
| [带指针的渐变环形进度条](https://juejin.cn/post/7455179302729777186) | 环形进度条升级，增加指针动态显示 | [代码](https://github.com/wuxaye/DiyView/tree/master/app/src/main/java/com/xaye/diyview/view/progressEx) |
| [仿 58 同城加载动画](https://juejin.cn/post/7439169215134384191) | 自定义加载动画，模仿 58 同城的效果 | [代码](https://github.com/wuxaye/DiyView/tree/master/app/src/main/java/com/xaye/diyview/view/loading) |

## 交互控件

| 示例 | 简介 | 代码 |
|---|---|---|
| [环形 SeekBar](https://juejin.cn/post/7491633146199375899) | 圆形轨迹的 SeekBar，类似传统 SeekBar 但采用环形 | [代码](https://github.com/wuxaye/DiyView/tree/master/app/src/main/java/com/xaye/diyview/view/circleseekbar) |
| [评分控件](https://juejin.cn/post/7441190456501043211) | 自定义评分控件，支持动态交互 | [代码](https://github.com/wuxaye/DiyView/tree/master/app/src/main/java/com/xaye/diyview/view/rating) |
| [侧边字母选择器](https://juejin.cn/post/7442284498741198858) | 实现字母快速选择的功能 | [代码](https://github.com/wuxaye/DiyView/tree/master/app/src/main/java/com/xaye/diyview/view/alphabet) |
| 刮刮乐 | 自定义刮刮乐控件，模拟刮奖效果 | [代码](https://github.com/wuxaye/DiyView/tree/master/app/src/main/java/com/xaye/diyview/view/erase) |

## 数据展示 / 动效

| 示例 | 简介 | 代码 |
|---|---|---|
| [仿 QQ 步数](https://juejin.cn/post/7436667191539466250) | 自定义控件显示步数统计，模仿 QQ 效果 | [代码](https://github.com/wuxaye/DiyView/tree/master/app/src/main/java/com/xaye/diyview/view/qqstep) |
| 跑道 | 动态显示赛跑效果 | [代码](https://github.com/wuxaye/DiyView/tree/master/app/src/main/java/com/xaye/diyview/view/track) |
| 时间线 | 物流信息时间线显示 | [代码](https://github.com/wuxaye/DiyView/tree/master/app/src/main/java/com/xaye/diyview/view/stepview) |

## Canvas / Paint 绘制示例

| 示例 | 简介 | 代码 |
|---|---|---|
| 颜色滤镜 | ColorMatrix / 着色器滤镜绘制 | [代码](https://github.com/wuxaye/DiyView/tree/master/app/src/main/java/com/xaye/diyview/netease/paint/colorfilter) |
| 图像混合（Xfermode） | DST_IN / SRC_OUT 及 18 种混合模式 | [代码](https://github.com/wuxaye/DiyView/tree/master/app/src/main/java/com/xaye/diyview/netease/paint/xfermode) |
| Canvas 变换与状态保存 | 平移/缩放/旋转/倾斜/切割及 saveLayer | [代码](https://github.com/wuxaye/DiyView/tree/master/app/src/main/java/com/xaye/diyview/netease/canvas/transform) |
| 图片粒子碎裂 | 图片按像素点碎裂散落（轻触触发） | [代码](https://github.com/wuxaye/DiyView/tree/master/app/src/main/java/com/xaye/diyview/netease/canvas/split) |
| 水波纹加载动画 | SplashView 扩散圆加载效果 | [代码](https://github.com/wuxaye/DiyView/tree/master/app/src/main/java/com/xaye/diyview/netease/canvas/splash) |
| 贝塞尔曲线 | 二/三阶曲线、多阶 de Casteljau、拖拽气泡 | [代码](https://github.com/wuxaye/DiyView/tree/master/app/src/main/java/com/xaye/diyview/netease/canvas/bezier) |
| Path 测量（PathMeasure） | 路径长度/截取/nextContour，取点取切线驱动箭头沿圆周运动 | [代码](https://github.com/wuxaye/DiyView/tree/master/app/src/main/java/com/xaye/diyview/netease/canvas/pathmeasure) |
