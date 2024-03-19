package AVL

import abstractions.*
import kotlin.math.max

class AVLNode<K : Comparable<K>, V>(key: K, value: V): abstractNode<K, V, AVLNode<K, V>>(key, value) {
    var height = 1
}
class AVLTree<K : Comparable<K>, V>: balancedTree<K, V, AVLNode<K, V>>() {
    override fun createNewNode(key: K, value: V): AVLNode<K, V> {
        return AVLNode(key, value)
    }
    override fun insert(key: K, value: V) {
        val insertedNode = insertNode(key, value)
        if (insertedNode != null) {
            println("${findParent(insertedNode)?.key}, ${insertedNode.key}")
            findParent(insertedNode)?.let {
                //updateHeight(it) // надо еще подумать, насколько это должно быть тут
                balanceAfterInsert(it)
            }
            println("after balance: ${findParent(insertedNode)?.key}, ${insertedNode.key}")
            println("height after balance: ${findParent(insertedNode)?.height}, ${insertedNode.height}")
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
    override fun balanceAfterInsert(insertedNode: AVLNode<K, V>) {
        val curNode = insertedNode
        when (getBalanceFactor(curNode)) {
            -2 -> {
                curNode.leftChild?.let { if (getBalanceFactor(it) == 1) leftRotate(it, findParent(it)) }
                println("rr ${curNode.key}")
                rightRotate(curNode, findParent(curNode))
                println("rr done ${curNode.height}")
            }
            2 -> {
                curNode.rightChild?.let { if (getBalanceFactor(it) == -1) rightRotate(it, findParent(it)) }
                leftRotate(curNode, findParent(curNode))
            }
            else -> updateHeight(curNode)
        }
        findParent(curNode)?.let { balanceAfterInsert(it) }
    }
    override fun balanceAfterDelete(curNode: AVLNode<K, V>) {
        TODO("Not yet implemented")
    }
    private fun swapNodes(node: AVLNode<K, V>, parentNode: AVLNode<K, V>?, nodeToSwap: AVLNode<K, V>?) {
        when (parentNode) {
            null -> {
                if (root == node) root = nodeToSwap
                //else throw IllegalArgumentException("Node's parent cannot be null if it isn't a root")
            }
            else -> {
                if (parentNode.rightChild == node) parentNode.rightChild = nodeToSwap
                else if (parentNode.leftChild == node) parentNode.leftChild = nodeToSwap
            }
        }
    }
    private fun rightRotate (node: AVLNode<K, V>, parentNode:  AVLNode<K, V>?) {
        val tempNode = node.leftChild ?: throw IllegalArgumentException("Node must have left child for right rotation")
        node.leftChild = tempNode.rightChild
        tempNode.rightChild = node
        swapNodes(node, parentNode, tempNode)
        println("n: ${node.key}")
        println("tn: ${tempNode.key}")
        updateHeight(node)
        updateHeight(tempNode)
    }
    private fun leftRotate (node: AVLNode<K, V>, parentNode:  AVLNode<K, V>?) {
        val tempNode = node.rightChild ?: throw IllegalArgumentException("Node must have right child for left rotation")
        node.rightChild = tempNode.leftChild
        tempNode.leftChild = node
        swapNodes(node, parentNode,tempNode)
        updateHeight(node)
        updateHeight(tempNode)
    }
    private fun findParent(node: AVLNode<K, V>): AVLNode<K, V>? { // вродь работает
        var curNode = root
        while (curNode != null) {
            if (curNode.key == node.key) return null
            if (curNode.leftChild?.key == node.key || curNode.rightChild?.key == node.key) return curNode
            curNode = when {
                curNode.key < node.key -> curNode.rightChild
                else -> curNode.leftChild
            }
        }
        return null
    }
}
fun main() {
}