package AVL

import abstractions.*
import kotlin.math.max

class AVLNode<K : Comparable<K>, V>(key: K, value: V): abstractNode<K, V, AVLNode<K, V>>(key, value) {
    var height = 1
}
class AVLTree<K : Comparable<K>, V>: balancedTree<K, V, AVLNode<K, V>>() {
    override fun createNewNode(key: K, value: V) = AVLNode(key, value)

    /* inserts a node & calls balancing after insertion */
    override fun insert(key: K, value: V) {
        val insertedNode = insertNode(key, value) ?: throw IllegalArgumentException("Nothing to insert")
        findParent(insertedNode)?.let { balance(it) }
//        val insertedNode = insertNode(key, value)
//        if (insertedNode != null) {
//            findParent(insertedNode)?.let { balance(it) }
//        }
    }

    /* deletes a node & calls balancing after deletion */
    override fun delete(key: K) {
        val nodeToDelete = findNodeByKey(key)
//        if (nodeToDelete != null) {
//            val deletedNodeParent = findParent(nodeToDelete)
//            println("parent node: ${deletedNodeParent?.key}")
//            val newNode = deleteNode(key)
//            println("newNode: ${newNode?.key}")
//            println("deleted node: ${nodeToDelete.key}")
//            /* deleting a node with 2 children */
//            if (nodeToDelete.leftChild != null && nodeToDelete.rightChild != null) {
//                val newNode = deleteNode(key)
//                val newNodeParent = findMinNodeInRight(newNode.rightChild)
//                println("newNodeParent: ${newNodeParent?.key}")
//                if (newNodeParent != null) balance(newNodeParent)
//                else balance(newNode)
//            }
//            /* deleting a node with 1 child */
//            else if (nodeToDelete.leftChild != null || nodeToDelete.rightChild != null) {
//                println("newNodeParent: ${findParent(newNode)?.key}")
//                balance(newNode)
//            }
//            /* deleting a leaf */
//            else {
//                deletedNodeParent?.let { balance(it) }
//            }
//        }
        //println("deleted node: ${nodeToDelete?.key}")
        if (nodeToDelete != null) {
            val deletedNodeParent = findParent(nodeToDelete)
//            println("parent node: ${deletedNodeParent?.key}")
            val childrenNum = getNumberOfChildren(nodeToDelete)
            val newNode = deleteNode(key)
//            println("newNode: ${newNode?.key}")
            /* deleting a leaf */
//            println("deleted node: ${nodeToDelete.key}")
            if (newNode == null) {
                deletedNodeParent?.let { balance(it) }
            }
            /* deleting a node with 1 child */
            else if (childrenNum == 1) {
//                println("newNodeParent1: ${findParent(newNode)?.key}")
                balance(newNode)
            }
            /* deleting a node with 2 children */
            else {
                val newNodeParent = findMinNodeInRight(newNode.rightChild)
//                println("newNodeParent2: ${newNodeParent?.key}")
                if (newNodeParent != null) balance(newNodeParent)
                else balance(newNode)
            }
        }
    }

    private fun getNumberOfChildren(node: AVLNode<K, V>): Int {
        return when {
            node.leftChild != null && node.rightChild != null -> 2
            node.leftChild != null || node.rightChild != null -> 1
            else -> 0
        }
    }

    private fun getHeight(node: AVLNode<K, V>?): Int {
        return node?.height ?: 0
    }

    private fun updateHeight(node: AVLNode<K, V>) {
        node.height = max(getHeight(node.rightChild), getHeight(node.leftChild)) + 1
    }

    private fun getBalanceFactor(node: AVLNode<K, V>): Int {
        return getHeight(node.rightChild) - getHeight(node.leftChild)
    }

    /* balances a tree by performing left & right rotations
    if absolute value of balance factor is more than 1 */
    override fun balance (curNode: AVLNode<K, V>, isAfterInsert: Boolean) {
        when (getBalanceFactor(curNode)) {
            -2 -> {
//                println("we're here")
                curNode.leftChild?.let { if (getBalanceFactor(it) == 1) rotateLeft(it, findParent(it)) }
                rotateRight(curNode, findParent(curNode))
            }
            2 -> {
                curNode.rightChild?.let { if (getBalanceFactor(it) == -1) rotateRight(it, findParent(it)) }
                rotateLeft(curNode, findParent(curNode))
            }
            else -> {
                updateHeight(curNode)
//                println("${curNode.key}, ${curNode.height}")
            }
        }
        findParent(curNode)?.let { balance(it) }
    }

    override fun rotateRight (node: AVLNode<K, V>, parentNode:  AVLNode<K, V>?) {
        super.rotateRight(node, parentNode)
        updateHeight(node)
        //updateHeight(tempNode)
    }

    override fun rotateLeft (node: AVLNode<K, V>, parentNode:  AVLNode<K, V>?) {
        super.rotateLeft(node, parentNode)
        updateHeight(node)
        //updateHeight(tempNode)
    }

    fun preorderTraverse(): List<Pair<K, Int>> {
        val listOfNodes = mutableListOf<AVLNode<K, V>>()
//        println("${root?.key}")
        traverse(root, listOfNodes)
        val listOfKeysAndHeights = mutableListOf<Pair<K, Int>>()
        listOfNodes.forEach { listOfKeysAndHeights.add(Pair(it.key, it.height)) }
        return listOfKeysAndHeights
    }
}
fun main() {
//    val tree = AVLTree<Int, String>()
//    tree.insert()
//    tree.insert(155, "")
//    tree.insert(55, "")
//    tree.insert(105, "")
//    tree.insert(15, "")
//    tree.delete(115)
//    val myList = tree.preorderTraverse()
//    for (item in myList) {
//        print("$item ")
//    }
}