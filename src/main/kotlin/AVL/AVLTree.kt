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
            findParent(insertedNode)?.let {
                //updateHeight(it) // надо еще подумать, насколько это должно быть тут
                balanceAfterInsert(it)
            }
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
    override fun balanceAfterInsert(curNode: AVLNode<K, V>) {
        when (getBalanceFactor(curNode)) {
            -2 -> {
                curNode.leftChild?.let { if (getBalanceFactor(it) == 1) rotateLeft(it) }
                rotateRight(curNode)
            }
            2 -> {
                curNode.rightChild?.let { if (getBalanceFactor(it) == -1) rotateRight(it) }
                rotateLeft(curNode)
            }
            else -> updateHeight(curNode)
        }
        findParent(curNode)?.let { balanceAfterInsert(it) }
    }
    override fun balanceAfterDelete(deletedNode: AVLNode<K, V>) {
        TODO("Not yet implemented")
    }
    private fun findParent(node: AVLNode<K, V>): AVLNode<K, V>? { // вродь работает
        var curNode = root
        var parentNode = root
        while (curNode != null && curNode.key != node.key) {
            parentNode = curNode
            curNode = when {
                node.key > curNode.key -> curNode.rightChild
                node.key < curNode.key -> curNode.rightChild
                else -> break
            }
        }
        //println(parentNode?.key)
        return null
    }

}
fun main() {
    var tree = AVLTree<Int, Any>()
    tree.insert(4, "hi")
    tree.insert(2, "bye")
    tree.insert(6, "xo")
    tree.insert(5, "del me")
    tree.insert(7, "suck")
    tree.insert(8, "suck")
    //tree.findParent(7)
    //tree.printNode(6)
    //tree.findNodeByKey(4)
}