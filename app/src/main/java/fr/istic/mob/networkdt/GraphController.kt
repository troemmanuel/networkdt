package fr.istic.mob.networkdt

class GraphController(private val graph: Graph) {

    fun addObject(label: String, x: Float, y: Float) {
        val node = ObjectNode(label, x, y)
        graph.addObject(node)
    }

    fun addConnection(start: ObjectNode, end: ObjectNode) {
        graph.addConnection(start, end)
    }

    fun resetGraph() {
        graph.reset();
    }
}