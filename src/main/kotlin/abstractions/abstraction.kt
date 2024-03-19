package abstractions

import AVL.AVLNode

abstract class abstractNode<K: Comparable<K>, V, someNode: abstractNode<K, V, someNode>>(var key:K, var value: V) {
    var leftChild: someNode? = null
    var rightChild: someNode? = null
}

class BSNode<K : Comparable<K>, V>(key: K, value: V) : abstractNode<K, V, BSNode<K, V>>(key, value)

abstract class abstractTree<K: Comparable<K>, V, someNode: abstractNode<K, V, someNode>> {
    var root: someNode? = null

    protected abstract fun createNewNode(key: K, value: V): someNode

    open fun insert(key: K, value: V) {
        insertNode(key, value)
    }
    protected fun insertNode(key: K, value: V): someNode? {
        val nodeToInsert = createNewNode(key, value)
        var curNode = root
        if (curNode == null) {
            root = nodeToInsert
            return root
        }
        else {
            while ((curNode != null) && (curNode != nodeToInsert)) {
                when {
                    (curNode.key == nodeToInsert.key) -> {
                        curNode.value = nodeToInsert.value
                        println("existing key entered. data has been changed")
                        break
                    }
                    ((curNode.leftChild == null) && (nodeToInsert.key < curNode.key)) -> {
                        curNode.leftChild = nodeToInsert
                        curNode = curNode.leftChild
                    }
                    ((curNode.rightChild == null) && (nodeToInsert.key > curNode.key)) -> {
                        curNode.rightChild = nodeToInsert
                        curNode = curNode.rightChild
                    }
                    nodeToInsert.key < curNode.key -> {
                        curNode = curNode.leftChild
                    }
                    nodeToInsert.key > curNode.key -> {
                        curNode = curNode.rightChild
                    }
                }
            }
            return curNode
        }
    }

    open fun delete(key: K) {
        val nodeToDelete = findNodeByKey(key)
        if ((nodeToDelete == null) || (root == null)) {
            println("nothing to delete")
            return
        }
        root = deleteRecursively(root, nodeToDelete)

    }

    protected fun deleteRecursively(curNode: someNode?, nodeToDelete: someNode): someNode? {
        when {
            curNode == null -> return null
            curNode.key < nodeToDelete.key -> curNode.rightChild = deleteRecursively(curNode.rightChild, nodeToDelete)
            curNode.key > nodeToDelete.key -> curNode.leftChild = deleteRecursively(curNode.leftChild, nodeToDelete)
            else -> {
                if ((curNode.leftChild == null)||(curNode.rightChild == null)) {
                    return if (curNode.leftChild != null) curNode.leftChild else curNode.rightChild
                }
                val replacementNode = findMinNodeInRight(curNode.rightChild)
                if (replacementNode != null) {
                    curNode.key = replacementNode.key
                    curNode.value = replacementNode.value
                    curNode.rightChild = deleteRecursively(curNode.rightChild, replacementNode)
                }
            }
        }
        return curNode
    }

    private fun findMinNodeInRight(subtree: someNode?): someNode? {
        var minNode = subtree
        while (true) {
            minNode = minNode?.leftChild ?: break
        }
        if (minNode != null) {
            return minNode
        }
        return subtree
    }

    fun findNodeByKey(key: K): someNode? {
        var curNode: someNode? = root ?: return null
        while (curNode != null) {
            curNode = when {
                (curNode.key == key) -> return curNode
                (curNode.key > key) -> curNode.leftChild
                else -> curNode.rightChild
            }
        }
        return null
    }

    fun printNode(key: K) {
        val valueToPrint = findNodeByKey(key)
        if (valueToPrint != null) {
            println(valueToPrint.value) }
        else {
            println("no such node")
        }
    }
}

abstract class balancedTree<K: Comparable<K>, V, someNode: abstractNode<K, V, someNode>>: abstractTree<K, V, someNode>() {
    protected abstract fun balanceAfterInsert(curNode: someNode)
    protected abstract fun balanceAfterDelete(curNode: someNode)
    protected open fun rotateRight (node: someNode, parentNode:  someNode?) {
        val tempNode = node.leftChild ?: throw IllegalArgumentException("Node must have left child for right rotation")
        node.leftChild = tempNode.rightChild
        tempNode.rightChild = node
        swapNodes(node, parentNode, tempNode)
    }
    protected open fun rotateLeft (node: someNode, parentNode:  someNode?) {
        val tempNode = node.rightChild ?: throw IllegalArgumentException("Node must have right child for left rotation")
        node.rightChild = tempNode.leftChild
        tempNode.leftChild = node
        swapNodes(node, parentNode,tempNode)
    }
    private fun swapNodes(node: someNode, parentNode: someNode?, nodeToSwap: someNode?) {
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
}

class BSTree<K : Comparable<K>, V> : abstractTree<K, V, BSNode<K, V>>() {
    override fun createNewNode(key: K, value: V): BSNode<K, V> = BSNode(key, value)
}

fun main() {

//    var tree = BSTree<Int, Any>()
//    tree.insert(4, "hi")
//    tree.insert(2, "bye")
//    tree.insert(6, "xo")
//    tree.insert(5, "del me")
//    tree.insert(7, "suck")
//    tree.delete(6)
//    tree.printNode(6)
//    tree.findNodeByKey(4)
}