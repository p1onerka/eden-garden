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
        if (insertedNode != null) balanceAfterInsert(insertedNode)
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

        TODO("Not yet implemented") //посравнивать бф и понять, гнать цикл или как
    }
    override fun balanceAfterDelete(deletedNode: AVLNode<K, V>) {
        TODO("Not yet implemented")
    }
}