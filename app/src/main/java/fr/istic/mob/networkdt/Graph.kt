package fr.istic.mob.networkdt

import kotlin.math.sqrt


data class ObjectNode(
    var label: String,
    var x: Float,
    var y: Float
)

data class Connection(
    var start: ObjectNode,
    var end: ObjectNode
)

class Graph {
    val nodes: MutableList<ObjectNode> = mutableListOf()
    val connections: MutableList<Connection> = mutableListOf()

    fun findNodeAt(x: Float, y: Float): ObjectNode? {
        val tolerance = 50f  // Correspond au rayon des cercles dessinés
        return nodes.find { node ->
            val dx = node.x - x
            val dy = node.y - y
            sqrt((dx * dx + dy * dy).toDouble()) <= tolerance
        }
    }


    // Ajouter un objet au réseau
    fun addObject(node: ObjectNode) {
        nodes.add(node)
    }

    // Ajouter une connexion entre deux objets
    fun addConnection(start: ObjectNode, end: ObjectNode) {
        connections.add(Connection(start, end))
    }

    // Réinitialiser le réseau
    fun reset() {
        nodes.clear()
        connections.clear()
    }

    // Vérifier s'il existe déjà une connexion entre deux objets
    private fun isConnected(from: ObjectNode, to: ObjectNode): Boolean {
        return connections.any { it.start == from && it.end == to }
    }
}
