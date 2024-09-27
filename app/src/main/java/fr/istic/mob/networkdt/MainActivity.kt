package fr.istic.mob.networkdt

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var graphController: GraphController
    private lateinit var drawableGraph: DrawableGraph
    private lateinit var graph: Graph
    private lateinit var plan: FrameLayout
    private var firstSelectedNode: ObjectNode? = null

    // Variable pour suivre l'objet sélectionné
    private var selectedNode: ObjectNode? = null
    private var isAddingObject: Boolean = false;
    private var isConnectingObject: Boolean = false;
    private var isEditObject: Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        graph = Graph()
        graphController = GraphController(graph)
        drawableGraph = DrawableGraph(graph)

        plan = findViewById(R.id.plan)
        plan.background = drawableGraph

        // Redessiner le graphe pour voir les objets ajoutés
        drawableGraph.invalidateSelf()

        plan.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                val x = motionEvent.x
                val y = motionEvent.y
                selectedNode = graph.findNodeAt(x, y);
                addAndConnectObject(selectedNode, view, x, y, isAddingObject, isConnectingObject)
            } else if (motionEvent.action == MotionEvent.ACTION_MOVE) {
                if(isEditObject){
                    // Si un objet est sélectionné, on le déplace en suivant le mouvement du doigt
                    selectedNode?.let {
                        it.x = motionEvent.x
                        it.y = motionEvent.y
                        drawableGraph.invalidateSelf()  // Redessiner le graphe avec l'objet déplacé
                    }
                }
            } else if (motionEvent.action == MotionEvent.ACTION_UP){
                selectedNode = null
            }
            false
        }
    }

    private fun addAndConnectObject(selectedNode: ObjectNode?,
                                    view: View,
                                    x: Float,
                                    y: Float,
                                    isAddMode: Boolean,
                                    isConnectMode: Boolean){
        if (selectedNode == null && isAddMode) {
            // If no node is found at the touch position, add a new node
            view.setOnLongClickListener {
                showInputDialog(view.context, x, y)
               //  selectedNode = null
                firstSelectedNode = null
                true
            }
        } else {
            // A node was selected
            if (firstSelectedNode == null) {
                // First node selected
                firstSelectedNode = selectedNode
            } else if (firstSelectedNode != selectedNode && isConnectMode) {
                // Second node selected, create connection
                graphController.addConnection(firstSelectedNode!!, selectedNode!!)
                drawableGraph.invalidateSelf()  // Redraw the graph with the new connection

                // Reset the first selected node
                firstSelectedNode = null
            }
            // Désactiver le long clic si on déplace un objet existant
            view.setOnLongClickListener(null)
        }
    }



    private fun showInputDialog(context: Context, x: Float, y: Float) {
        val editText = EditText(context)
        editText.hint = "Entrez le nom de l'objet"

        val dialog = AlertDialog.Builder(context)
            .setTitle("Nouveau Objet")
            .setMessage("Entrez le nom de l'objet")
            .setView(editText)
            .setPositiveButton("OK") { dialog, which ->
                val label = if (editText.text.isNotEmpty()) {
                    editText.text.toString()
                } else {
                    "Objet ${graph.nodes.size + 1}"  // Si l'utilisateur n'a pas entré de nom, utiliser une étiquette par défaut
                }
                graphController.addObject(label, x, y)
                drawableGraph.invalidateSelf()
            }
            .setNegativeButton("Annuler") { dialog, which ->
                dialog.dismiss()
            }
            .create()
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.reset_network -> {
                graph.reset()
                plan.invalidate()
                Toast.makeText(this, getString(R.string.network_reset), Toast.LENGTH_SHORT).show()
                true
            }
            R.id.add_object -> {
                isAddingObject = true
                isEditObject = false
                isEditObject = false
                true
            }
            R.id.add_connection -> {
                isAddingObject = false
                isEditObject = false
                isConnectingObject= true
                true
            }
            R.id.edit_mode-> {
                isAddingObject = false
                isEditObject = true
                isConnectingObject= false
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
