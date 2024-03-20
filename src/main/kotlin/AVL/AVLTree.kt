package AVL

import abstractions.*
import kotlin.math.max

class AVLNode<K : Comparable<K>, V>(key: K, value: V): abstractNode<K, V, AVLNode<K, V>>(key, value) {
    var height = 1
}
class AVLTree<K : Comparable<K>, V>: balancedTree<K, V, AVLNode<K, V>>() {
    override fun createNewNode(key: K, value: V) = AVLNode(key, value)

    override fun insert(key: K, value: V) {
        val insertedNode = insertNode(key, value)
        if (insertedNode != null) {
            //println("${findParent(insertedNode)?.key}, ${insertedNode.key}")
            findParent(insertedNode)?.let {
                //updateHeight(it) // надо еще подумать, насколько это должно быть тут
                balanceAfterInsert(it)
            }
            //println("after balance: ${findParent(insertedNode)?.key}, ${insertedNode.key}")
            //println("height after balance: ${findParent(insertedNode)?.height}, ${insertedNode.height}")
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
                curNode.leftChild?.let { if (getBalanceFactor(it) == 1) rotateLeft(it, findParent(it)) }
                rotateRight(curNode, findParent(curNode))
            }
            2 -> {
                curNode.rightChild?.let { if (getBalanceFactor(it) == -1) rotateRight(it, findParent(it)) }
                rotateLeft(curNode, findParent(curNode))
            }
            else -> updateHeight(curNode)
        }
        findParent(curNode)?.let { balanceAfterInsert(it) }
    }

    /* I have no idea if this works */
    override fun delete(key: K) {
        val deletedNodeParent = deleteNode(key)
        /* if nothing was added or tree is empty, there's no need to balance it */
        deletedNodeParent?.let { balanceAfterInsert(it) }
    }

    override fun balanceAfterDelete(curNode: AVLNode<K, V>) {
        TODO("Not yet implemented")
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
}
fun main() {
}