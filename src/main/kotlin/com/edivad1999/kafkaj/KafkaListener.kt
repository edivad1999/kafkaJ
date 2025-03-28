package com.edivad1999.kafkaj

import com.intellij.application.subscribe
import com.intellij.ide.AppLifecycleListener
import com.intellij.ide.plugins.DynamicPluginListener
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.extensions.PluginId
import com.intellij.ui.JBColor
import com.intellij.ui.scale.JBUIScale.scale
import com.intellij.util.ui.GraphicsUtil
import com.intellij.util.ui.JBInsets
import kafkaj.KafkaJInstaller
import java.awt.* // ktlint-disable no-wildcard-imports
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.geom.RoundRectangle2D
import javax.swing.JComponent
import javax.swing.UIManager
import javax.swing.plaf.basic.BasicProgressBarUI
import kotlin.math.roundToInt

class KafkaAppLifecycleListener : AppLifecycleListener {

    override fun appFrameCreated(commandLineArgs: MutableList<String>) {
        service<KafkaListener>()
    }
}

class KafkaDynamicPluginListener : DynamicPluginListener {

    override fun pluginLoaded(pluginDescriptor: IdeaPluginDescriptor) {
        if (pluginDescriptor.pluginId == PluginId.getId("com.edivad1999.kafkaJ")) {
            service<KafkaListener>()
        }
    }
}

@Service
class KafkaListener : LafManagerListener, Disposable {
    init {
        LafManagerListener.TOPIC.subscribe(this, this)
        updateProgressBarUi()
    }

    fun updateProgressBarUi() {
        UIManager.put("ProgressBarUI", KafkaJInstaller::class.java.name)
        UIManager.getDefaults()[KafkaJInstaller::class.java.name] = KafkaJInstaller::class.java
        UIManager.put(
            "MemoryIndicator.usedBackground",
            JBColor(
                KafkaProgressBarUi.FLAG_PURPLE,
                KafkaProgressBarUi.FLAG_PURPLE,
            ),
        )
        overrideBluePalette()
        val keys = listOf(
            "ActionButton.focusedBorderColor", "Button.default.endBackground",
            "Button.default.endBorderColor",
            "Button.default.focusColor",
            "Button.default.startBackground",
            "Button.default.startBorderColor",
            "Component.focusColor",
            "DragAndDrop.borderColor",
            "HelpBrowser.titleHighlightForeground",
            "RecentProject.Color5.Avatar.Start",
            "TabbedPane.underlineColor",
            "Tooltip.Learning.background",
            "Tooltip.Learning.borderColor",
            "ToolWindow.Button.selectedBackground",
            "RunWidget.background",
        )

        UIManager.put(
            "RunWidget.leftHoverBackground",
            JBColor(
                KafkaProgressBarUi.FLAG_PURPLE,
                KafkaProgressBarUi.FLAG_PURPLE,
            ).darker(),
        )

        keys.forEach {
            UIManager.put(
                it,
                JBColor(
                    KafkaProgressBarUi.FLAG_PURPLE,
                    KafkaProgressBarUi.FLAG_PURPLE,
                ),
            )
        }
    }

    fun overrideBluePalette() {
        val prefix = "ColorPalette.Blue"
        val baseColor = JBColor(
            KafkaProgressBarUi.FLAG_PURPLE,
            KafkaProgressBarUi.FLAG_PURPLE,
        )
        for (i in 1..11) {
            val diff = 6 - i
            val newColor = when {
                diff == 0 -> baseColor
                diff < 0 -> {
                    var newColor = baseColor
                    repeat(diff) {
                        newColor = JBColor(newColor.darker(), newColor.brighter())
                    }
                    newColor
                }

                diff > 0 -> {
                    var newColor = baseColor
                    repeat(diff) {
                        newColor = JBColor(newColor.brighter(), newColor.darker())
                    }
                    newColor
                }

                else -> baseColor
            }
            UIManager.put(
                prefix + i,
                newColor,
            )
        }
    }

    override fun lookAndFeelChanged(source: LafManager) {
        updateProgressBarUi()
    }

    override fun dispose() {
        //
    }
}

class KafkaProgressBarUi : BasicProgressBarUI() {

    private val kafkaPaint = JBColor(FLAG_PURPLE, FLAG_PURPLE)
    private val kafkaGradient = JBColor(FLAG_PURPLE_WHITE, FLAG_PURPLE_WHITE)

    private val borderColor = JBColor(0x50733558, 0x50733558)

    private var iconOffsetX = 0

    private var icon = KafkaIcons.KafkaSpinning

    private val iconSize = icon.iconHeight
    private var isIndeterminate: Boolean = false
        set(value) {
            if (field != value) resetState()
            field = value
        }

    private fun resetState() {
        iconOffsetX = 0
        isGoingRight = true
    }

    private val resizeListener = object : ComponentAdapter() {
        override fun componentResized(e: ComponentEvent?) {
            progressBar.invalidate()
        }
    }

    private val borderInsets = JBInsets.create(1, 1)

    override fun installListeners() {
        super.installListeners()
        progressBar.addComponentListener(resizeListener)
    }

    override fun uninstallListeners() {
        progressBar.removeComponentListener(resizeListener)
        super.uninstallListeners()
    }

    override fun getPreferredSize(c: JComponent): Dimension {
        val verticalInsets = c.insets.vertical
        val borderThickness = borderInsets.top
        return Dimension(
            super.getPreferredSize(c).width,
            getPreferredStripeHeight() + verticalInsets + borderThickness,
        )
    }

    private fun getPreferredStripeHeight(): Int {
        val ho = progressBar.getClientProperty("ProgressBar.stripeWidth")
        return ho?.toString()?.toIntOrNull()?.let { scale(it.coerceAtLeast(12)) } ?: defaultIconSize
    }

    override fun paintIndeterminate(g2d: Graphics, c: JComponent) {
        isIndeterminate = true
        if (g2d !is Graphics2D) {
            return
        }

        GraphicsUtil.setupAAPainting(g2d)

        val insets = progressBar.insets
        val availableWidth = (progressBar.width - insets.horizontal).toFloat()
        val availableHeight = (progressBar.height - insets.vertical).toFloat()

        val borderThickness = borderInsets.top.toFloat()
        val barWidth = availableWidth - 2 * borderThickness
        val barHeight = availableHeight - 2 * borderThickness

        if (availableWidth <= 0 || availableHeight <= 0) {
            return
        }

        val x = insets.top.toFloat()
        val y = insets.left.toFloat()
        doAnimationTick(barWidth.toInt())

        g2d.drawLine(
            x = x + borderThickness,
            y = y + borderThickness,
            barWidth = barWidth,
            barHeight = barHeight,
        )

        g2d.drawKafka(
            barY = y + borderThickness,
            barHeight = barHeight,
            xOffset = x + iconOffsetX.toFloat(),
        )
    }

    private var isGoingRight: Boolean = false
        set(value) {
            if (field != value) {
                field = value
            }
        }

    private fun doAnimationTick(barWidth: Int) {
        val tick = scale(1)
        if (isGoingRight) iconOffsetX += tick else iconOffsetX -= tick

        when {
            !isIndeterminate -> isGoingRight = true
            iconOffsetX > barWidth + iconSize / 2 -> isGoingRight = false
            iconOffsetX < -iconSize -> isGoingRight = true
        }
    }

    override fun paintDeterminate(g2d: Graphics, c: JComponent) {
        isIndeterminate = false
        if (g2d !is Graphics2D) {
            return
        }

        GraphicsUtil.setupAAPainting(g2d)

        val insets = progressBar.insets
        val availableWidth = (progressBar.width - insets.horizontal).toFloat()
        val availableHeight = (progressBar.height - insets.vertical).toFloat()

        val progress = progressBar.value.toFloat() / progressBar.maximum
        val barWidth = ((availableWidth - 2) * progress + iconSize * progress)
        val barHeight = availableHeight - 2

        if (availableWidth <= 0 || availableHeight <= 0) {
            return
        }

        val x = insets.top.toFloat()
        val y = insets.left.toFloat()
        doAnimationTick(barWidth.toInt())

        g2d.drawLine(
            x = x,
            y = y,
            barWidth = (barWidth - iconSize / 2f).coerceIn(0f..(availableWidth - 2)),
            barHeight = barHeight,
        )

        g2d.drawKafka(
            barY = y,
            barHeight = barHeight,
            xOffset = x + barWidth - iconSize / 2f,

            )
    }

    private fun Graphics2D.drawLine(
        x: Float,
        y: Float,
        barWidth: Float,
        barHeight: Float,
    ) {
        if (barWidth < 1f) return
        val barArc = scale(ARC_SIZE)
        val barReducedHeight = barHeight / 3f
        val path = RoundRectangle2D.Float(x, y + barReducedHeight, barWidth, barReducedHeight, barArc, barArc)
        paint = JBColor.WHITE
        fill(path)
        val offset = (progressBar.width / 4) + if (isIndeterminate) iconOffsetX * 10 else (barWidth / 4).toInt()
        paint = GradientPaint(
            /* x1 = */ 0f,
            /* y1 = */ 0f,
            /* color1 = */ kafkaPaint,
            /* x2 = */ offset.toFloat(),
            /* y2 = */ 0f,
            /* color2 = */ kafkaGradient,
            /* cyclic = */ true,
        )
        fill(path)
        paint = borderColor
        draw(path)
    }

    private fun Graphics2D.drawKafka(barY: Float, barHeight: Float, xOffset: Float) {
        val yOffset = barY + barHeight / 2f - icon.iconHeight / 2f
        icon.paintIcon(
            progressBar,
            this,
            (xOffset - (iconSize / 2f)).roundToInt(),
            (yOffset).roundToInt(),
        )
    }

    override fun getBoxLength(availableLength: Int, otherDimension: Int): Int = availableLength

    companion object {

        const val FLAG_PURPLE = 0x733558

        private val FLAG_PURPLE_WHITE = JBColor(Color(115, 53, 88, 180), Color(115, 53, 88, 180))

        private val defaultIconSize
            get() = scale(20)

        private const val ARC_SIZE = 8f
    }
}

fun Int.even() = (this % 2) == 0

internal val Insets.horizontal
    get() = left + right

internal val Insets.vertical
    get() = top + bottom
