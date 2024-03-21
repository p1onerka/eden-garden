package RB

import abstractions.*

enum class Color {
    RED, BLACK
}

class RBNode<K : Comparable<K>, V>(key: K, value: V): abstractNode<K, V, RBNode<K, V>>(key, value) {
    var color: Color = Color.RED
    var parent: RBNode<K, V>? = null
}

class RBTree<K : Comparable<K>, V>: balancedTree<K, V, RBNode<K, V>>() {
    override fun createNewNode(key: K, value: V): RBNode<K, V> {
        return RBNode(key, value)
    }

    override fun insert(key: K, value: V) {
        val newNode = createNewNode(key, value)
        if (root == null) {
            root = newNode
            newNode.color = Color.BLACK
            return
        }

        val parent = findParent(newNode)
        if (parent == null) {
            println("Node with the same key already exists in the tree.")
            return
        }

        newNode.parent = parent
        if (newNode.key < parent.key) {
            parent.leftChild = newNode
        } else {
            parent.rightChild = newNode
        }
    }
    private fun fixRedRedViolation(curNode: RBNode<K, V>) {
        var node = curNode
        if (node == root) {
            node.color = Color.BLACK
            return
        }
        if (node.parent?.color == Color.RED) {
            val grandparent = node.parent?.parent
            val uncle = getUncle(node)

            if (uncle?.color == Color.RED) {
                node.parent?.color = Color.BLACK
                uncle.color = Color.BLACK
                if(grandparent != null) {
                    grandparent.color = Color.RED
                    fixRedRedViolation(grandparent)
                } else {

                }
            } else {
                if (node == node.parent?.rightChild && node.parent == grandparent?.leftChild) {
                    rotateLeft(node.parent)
                    node = node.leftChild ?: throw IllegalStateException("Left child is null")
                } else if (node == node.parent?.leftChild && node.parent == grandparent?.rightChild) {
                    rotateRight(node.parent)
                    node = node.rightChild ?: throw IllegalStateException("Right child is null")
                }

                if (node == node.parent?.leftChild) {
                    rotateRight(grandparent)
                } else {
                    rotateLeft(grandparent)
                }

                node.parent?.color = Color.BLACK
                grandparent?.color = Color.RED
            }
        }
    }

    private fun rotateLeft(node: RBNode<K, V>?) {
        val newRoot = node?.rightChild ?: return
        node.rightChild = newRoot.leftChild
        if (newRoot.leftChild != null) {
            newRoot.leftChild?.parent = node
        }
        newRoot.parent = node.parent
        if (node.parent == null) {
            root = newRoot
        } else if (node == node.parent?.leftChild) {
            node.parent?.leftChild = newRoot
        } else {
            node.parent?.rightChild = newRoot
        }
        newRoot.leftChild = node
        node.parent = newRoot
    }

    private fun rotateRight(node: RBNode<K, V>?) {
        val newRoot = node?.leftChild ?: return
        node.rightChild = newRoot.leftChild
        if (newRoot.rightChild != null) {
            newRoot.rightChild?.parent = node
        }
        newRoot.parent = node.parent
        if (node.parent == null) {
            root = newRoot
        } else if (node == node.parent?.rightChild) {
            node.parent?.rightChild = newRoot
        } else {
            node.parent?.leftChild = newRoot
        }
        newRoot.rightChild = node
        node.parent = newRoot
    }
    private fun getUncle(node: RBNode<K, V>): RBNode<K, V>? {
        val parent = node.parent
        val grandparent = parent?.parent
        if (parent == grandparent?.leftChild) {
            return grandparent?.rightChild
        } else {
            return grandparent?.leftChild
        }
    }

    override fun balanceAfterInsert(curNode: RBNode<K, V>) {
        TODO("Not yet implemented")
    }

    override fun balanceAfterDelete(curNode: RBNode<K, V>) {
        TODO("Not yet implemented")
    }
}
