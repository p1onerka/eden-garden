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
        val insertedNode = insertNode(key, value) ?: throw IllegalArgumentException("empty insert")
        balanceAfterInsert(insertedNode)
    }

    override fun balanceAfterInsert(insertedNode: RBNode<K, V>) {
        var curNode = insertedNode
        curNode.color = Color.RED

        //case 0: insertedNode is root
        if (root == curNode) {
            println("node ${curNode.key} activated 0 case")
            curNode.color = Color.BLACK
            return
        }

        var parent = findParent(curNode) ?: throw IllegalArgumentException("non root should have parent")
        curNode.parent = parent

        //case 1: parent of insertedNode is black
        if (parent.color == Color.BLACK) {
            println("node ${curNode.key} activated 1 case")
            return
        }

        val uncle = getUncle(curNode)
        val grandparent = getGrandparent(curNode) ?: throw IllegalArgumentException("every node by that point should have grandy")

        //case 2: uncle non-null and red (so both uncle and parent are red)
        if ((uncle != null) && (uncle.color == Color.RED)) {
            println("node ${curNode.key} activated 2 case")
            parent.color = Color.BLACK
            uncle.color = Color.BLACK
            grandparent.color = Color.RED
            balanceAfterInsert(grandparent)
            return
        }

        //case 3: uncle black. grandparent, parent and node form a "triangle". should be continued w case 4
        if ((curNode == parent.rightChild) && (parent == grandparent.leftChild)) {
            println("node ${curNode.key} activated 3 case left")
            rotateLeft(parent, grandparent)
            parent = curNode
            curNode = curNode.leftChild ?: throw IllegalArgumentException("smth wrong w left rotation")

        }
        else if ((curNode == parent.leftChild) && (parent == grandparent.rightChild)) {
            println("node ${curNode.key} activated 3 case right")
            rotateRight(parent, grandparent)
            parent = curNode
            curNode = curNode.rightChild ?: throw IllegalArgumentException("smth wrong w right rotation")
        }
        //parent & cur оба красные, так что тут никого не надо перекрашивать (КАЖЕТСЯ!!)

        //case 4: grandparent, parent and node form a "line"
//        println("node ${curNode.key} activated 4 case")
//        println("cur is ${curNode.key}")
//        println("parent is ${parent.key}")
//        println("grand is ${grandparent.key}")
//        println("grands left is ${grandparent.leftChild?.key}")
        parent.color = Color.BLACK
        grandparent.color = Color.RED
        if ((curNode == parent.leftChild)&&(parent == grandparent.leftChild)) {
            rotateRight(grandparent, grandparent.parent)
//            val temp = curNode
//            curNode = curNode.rightChild ?: throw IllegalArgumentException("smth wrong w right rotation")
//            curNode.parent = temp
        }
        else {
            rotateLeft(grandparent, grandparent.parent)
//            val temp = curNode
//            curNode = curNode.leftChild ?: throw IllegalArgumentException("smth wrong w left rotation")
//            curNode.parent = temp
        }
        return
    }

    private fun getGrandparent(node: RBNode<K, V>): RBNode<K, V>? {
        val grandparent = node.parent?.parent
        return grandparent
    }
    private fun getSibling(node: RBNode<K, V>): RBNode<K, V>? {
        if (node.parent == null) {
            return null
        }
        return if (node == node.parent?.leftChild) {
            node.parent?.rightChild
        } else {
            node.parent?.leftChild
        }
    }

    override fun rotateRight(node: RBNode<K, V>, parentNode: RBNode<K, V>?) {
        val tempNode = node.leftChild ?: throw IllegalArgumentException("Node must have left child for right rotation")
        node.leftChild = tempNode.rightChild
        tempNode.rightChild = node
        moveParentNode(node, parentNode, tempNode)
        tempNode.parent = parentNode
        node.parent = tempNode
    }
    override fun rotateLeft(node: RBNode<K, V>, parentNode: RBNode<K, V>?) {
        val tempNode = node.rightChild ?: throw IllegalArgumentException("Node must have right child for left rotation")
        node.rightChild = tempNode.leftChild
        tempNode.leftChild = node
        moveParentNode(node, parentNode,tempNode)
        tempNode.parent = parentNode
        node.parent = tempNode
    }
    private fun changeColor(node: RBNode<K, V>): RBNode<K, V> {
        when (node.color) {
            Color.BLACK -> node.color = Color.RED
            else -> node.color = Color.BLACK
        }
        return node
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

    override fun balanceAfterDelete(curNode: RBNode<K, V>) {
        return
    }
}

fun main(){
    val tree = RBTree<Int, Any>()
    tree.insert(30, "hi")
    tree.insert(35, "hi")
    tree.insert(20, "hi")
    tree.insert(25, "hi")
    tree.insert(22, "hi")

    val myList = tree.preorderTraversal()
    for (item in myList) {
        print("$item ")
    }
}