package fr.istic.mob.networkdt

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.Drawable

class DrawableGraph(private val graph: Graph) : Drawable() {

    private val paintNode = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
        textSize = 50f
    }

    private val paintConnection = Paint().apply {
        color = Color.BLACK
        strokeWidth = 5f
        style = Paint.Style.STROKE
    }

    override fun draw(canvas: Canvas) {
        // Dessiner les objets connectés
        for (node in graph.nodes) {
            canvas.drawCircle(node.x, node.y, 50f, paintNode)
            canvas.drawText(node.label, node.x - 100, node.y + 100, paintNode)
        }

        // Dessiner les connexions
        for (connection in graph.connections) {
            println("print connection" + connection.start.x + " " + connection.start.y + " " + connection.end.x + " " + connection.end.y)
            val path = Path()
            path.moveTo(connection.start.x, connection.start.y)
            path.lineTo(connection.end.x, connection.end.y)
            canvas.drawPath(path, paintConnection)
        }
    }

    override fun setAlpha(alpha: Int) {
        // Implementation if needed
    }

    override fun setColorFilter(colorFilter: android.graphics.ColorFilter?) {
        // Implementation if needed
    }

    override fun getOpacity(): Int {
        return android.graphics.PixelFormat.OPAQUE
    }
}
