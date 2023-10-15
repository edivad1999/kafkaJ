package com.edivad1999.kafkaj

import java.awt.Image
import java.io.File
import java.net.URL
import javax.swing.ImageIcon

object KafkaIcons {
    private var icon: ImageIcon? = null
    val KafkaSpinning: ImageIcon
        get() {
            val oldk = icon
            return if (oldk != null) {
                oldk
            } else {
                val image = ImageIcon(cleanURL("/kafka-spin.gif"))

                image.image = image.image.getScaledInstance(22, 22, Image.SCALE_DEFAULT)
                icon = image
                image
            }
        }

    private fun cleanURL(filePath: String): URL {
        return javaClass.getResource(filePath) ?: createUrlFromPath(filePath)
    }

    private fun createUrlFromPath(filePath: String): URL {
        return File(filePath).toURI().toURL()
    }
}
