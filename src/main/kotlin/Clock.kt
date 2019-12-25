import org.w3c.dom.*
import kotlin.browser.document
import kotlin.browser.window
import kotlin.js.Date
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

typealias CRC2D = CanvasRenderingContext2D

//initialize canvas
val canvas = document.getElementById("canvas") as HTMLCanvasElement
//canvas has context
val ctx = canvas.getContext("2d") as CRC2D

val width = window.innerWidth
val height = window.innerHeight
const val radius = 100.0

fun main() {
    with(ctx) {
        canvas.let {
            it.width = width
            it.height = height
        }
        drawClock()
        window.setInterval({ drawClock() }, 1000)
    }
}

private fun CRC2D.drawClock() {
    clearRect(0.0, 0.0, width.toDouble(), height.toDouble())
    drawBorder()
    drawDots()
    drawHoursText()
    Date().run { drawPointer(getHours(), getMinutes(), getSeconds()) }
    drawCenterDot()
    restore()
}

//绘制时钟中心点
private fun CRC2D.drawCenterDot() {
    beginPath()
    fillStyle = "#ccc"
    arc(0.0, 0.0, 4.0, 0.0, 2 * PI)
    fill()
}

//绘制12小时文本
private fun CRC2D.drawHoursText() {
    fillStyle = "#000"
    font = "20px Arial"
    textAlign = CanvasTextAlign.CENTER
    textBaseline = CanvasTextBaseline.MIDDLE
    (3..12).map { it.toString() }.plus(listOf("1", "2")).forEachIndexed { index, number ->
        val rad = 2 * PI / 12 * index
        fillText(number, cos(rad) * (radius - 25), sin(rad) * (radius - 25))
    }
}

//绘制60个时钟点
private fun CRC2D.drawDots() = (0..59).forEach {
    beginPath()
    fillStyle = if (it % 5 == 0) "#000" else "#ccc"
    val rad = (2 * PI / 60 * it)
    arc(cos(rad) * (radius - 10), sin(rad) * (radius - 10), 2.0, 0.0, 2 * PI)
    fill()
}

//绘制时钟外边框
private fun CRC2D.drawBorder() {
    save()
    beginPath()
    lineWidth = 6.0
    translate((width / 2).toDouble(), (height / 2).toDouble() - 150)  // 设置canvas原点坐标
    arc(0.0, 0.0, radius, 0.0, 2 * PI)
    stroke()
}

//绘制指针
private fun CRC2D.drawPointer(hours: Int, minutes: Int, seconds: Int) {
    drawHoursLine(hours, minutes)
    drawMinutesLine(minutes)
    drawSecondsLine(seconds)
}

//绘制秒针线条
private fun CRC2D.drawSecondsLine(seconds: Int) {
    save()
    beginPath()
    lineWidth = 3.0
    fillStyle = "#f00"
    rotate(2 * PI / 60 * seconds)
    moveTo(-2.0, 20.0)
    lineTo(2.0, 20.0)
    lineTo(1.0, -radius + 18)
    lineTo(-1.0, -radius + 18)
    fill()
    restore()
}

//绘制分针线条
private fun CRC2D.drawMinutesLine(minutes: Int) {
    save()
    beginPath()
    lineWidth = 4.0
    rotate(2 * PI / 60 * minutes)
    moveTo(0.0, 14.0)
    lineTo(0.0, -radius + 38)
    stroke()
    restore()
}

//绘制时针线条
private fun CRC2D.drawHoursLine(hours: Int, minutes: Int) {
    save()
    beginPath()
    lineCap = CanvasLineCap.ROUND
    val rad = 2 * PI / 12 * hours
    val mRad = 2 * PI / 60 / 12 * minutes
    rotate(rad + mRad)
    moveTo(0.0, 10.0)
    lineTo(0.0, -radius / 2)
    stroke()
    restore()
}