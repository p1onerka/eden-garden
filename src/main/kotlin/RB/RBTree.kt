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
        val insertedNode = insertNode(key, value) ?: throw IllegalArgumentException("Nothing to insert")
        balance(insertedNode, true)
    }

    private fun balanceAfterInsert(curNode: RBNode<K, V>) {
        var insertedNode = curNode
        insertedNode.color = Color.RED

        //case 0: insertedNode is root
        if (root == insertedNode) {
            insertedNode.color = Color.BLACK
            return
        }

        var parent = findParent(insertedNode) ?: throw IllegalArgumentException("Non-root should have parent")
        insertedNode.parent = parent

        //case 1: parent of insertedNode is black
        if (parent.color == Color.BLACK) {
            return
        }

        val uncle = getUncle(insertedNode)
        val grandparent = getGrandparent(insertedNode) ?: throw IllegalArgumentException("Every node by that point should have grandparent")

        //case 2: uncle is non-null and red (so both uncle and parent are red)
        if ((uncle != null) && (uncle.color == Color.RED)) {
            parent.color = Color.BLACK
            uncle.color = Color.BLACK
            grandparent.color = Color.RED
            balanceAfterInsert(grandparent)
            return
        }

        //case 3: uncle is black; grandparent, parent and node form a "triangle"; should be continued w case 4
        if ((insertedNode == parent.rightChild) && (parent == grandparent.leftChild)) {
            rotateLeft(parent, grandparent)
            parent = insertedNode
            insertedNode = insertedNode.leftChild ?: throw IllegalArgumentException("Node should have child after left rotation")

        }
        else if ((insertedNode == parent.leftChild) && (parent == grandparent.rightChild)) {
            rotateRight(parent, grandparent)
            parent = insertedNode
            insertedNode = insertedNode.rightChild ?: throw IllegalArgumentException("Node should have right child after right rotation")
        }

        //case 4: grandparent, parent and node form a "line"
        parent.color = Color.BLACK
        grandparent.color = Color.RED
        if ((insertedNode == parent.leftChild)&&(parent == grandparent.leftChild)) {
            rotateRight(grandparent, grandparent.parent)
        }
        else {
            rotateLeft(grandparent, grandparent.parent)
        }
        return
    }

    override fun balance(curNode: RBNode<K, V>, isAfterInsert: Boolean) {
        if (isAfterInsert) { balanceAfterInsert(curNode)}
        else { balanceAfterDelete(curNode) }
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

    private fun getUncle(node: RBNode<K, V>): RBNode<K, V>? {
        val parent = node.parent
        val grandparent = parent?.parent
        if (parent == grandparent?.leftChild) {
            return grandparent?.rightChild
        } else {
            return grandparent?.leftChild
        }
    }

    private fun balanceAfterDelete(curNode: RBNode<K, V>) {
        var nodeToDelete = curNode
        val child = when {
            nodeToDelete.rightChild != null -> nodeToDelete.rightChild
            nodeToDelete.leftChild != null -> nodeToDelete.leftChild
            else -> null
        }

        child?.parent = nodeToDelete

        if(nodeToDelete.color == Color.RED) {
            deleteNode(nodeToDelete.key)
            child?.leftChild?.parent = child
            return
        }

        if(nodeToDelete.color == Color.BLACK && child?.color == Color.RED) {
            deleteNode(nodeToDelete.key)
            child.leftChild?.parent = child
            child.color = Color.BLACK
            return
        }

        if(nodeToDelete.color == Color.BLACK && child?.color == Color.BLACK) {
            deleteNode(nodeToDelete.key)
            child.leftChild?.parent = child
            deleteCase1(child)
        }
    }

    override fun delete(key: K) {
        val nodeToDelete = findNodeByKey(key)
        nodeToDelete?.let { balance(nodeToDelete, false) }
    }

    private fun deleteCase1(node: RBNode<K, V>?) {
        if(node?.parent != null) deleteCase2(node)
    }

    private fun deleteCase2(node: RBNode<K, V>) {
        val sibling = getSibling(node)
        if(sibling?.color == Color.RED) {
            node.parent?.color = Color.RED
            sibling.color = Color.BLACK
            if(node == node.parent?.leftChild) {
                val parent = node.parent
                if (parent != null) rotateLeft(parent, parent.parent)
            }
            else {
                val parent = node.parent
                if (parent != null) rotateRight(parent, parent.parent)
            }
        }
        deleteCase3(node)
    }

    private fun deleteCase3(node: RBNode<K, V>) {
        val sibling = getSibling(node)
        if(node.parent?.color == Color.BLACK && sibling?.color == Color.BLACK &&
            sibling.rightChild?.color == Color.BLACK && sibling.leftChild?.color == Color.BLACK) {

            sibling.color = Color.RED
            deleteCase1(node.parent)
        }
        else deleteCase4(node)
    }

    private fun deleteCase4(node: RBNode<K, V>) {
        val sibling = getSibling(node)
        if(node.parent?.color == Color.RED && sibling?.color == Color.BLACK &&
            sibling.rightChild?.color == Color.BLACK && sibling.leftChild?.color == Color.BLACK) {

            sibling.color = Color.RED
            node.parent?.color = Color.BLACK
        }
        else deleteCase5(node)
    }

    private fun deleteCase5(node: RBNode<K, V>) {
        val sibling = getSibling(node)
        if (node == node.parent?.leftChild && sibling?.color == Color.BLACK &&
            sibling.rightChild?.color == Color.BLACK && sibling.leftChild?.color == Color.RED) {

            sibling.color = Color.RED
            sibling.leftChild?.color = Color.BLACK
            rotateRight(sibling, node.parent)
        }
        else if (node == node.parent?.rightChild && sibling?.color == Color.BLACK &&
            sibling.rightChild?.color == Color.RED && sibling.leftChild?.color == Color.BLACK) {

            sibling.color = Color.RED
            sibling.rightChild?.color = Color.BLACK
            rotateLeft(sibling, node.parent)
        }
        deleteCase6(node)
    }

    private fun deleteCase6(node: RBNode<K, V>) {

        val sibling = getSibling(node)
        val color = node.parent?.color
        if (color != null) sibling?.color = color
        node.parent?.color = Color.BLACK

        if (node == node.parent?.leftChild) {
            sibling?.rightChild?.color = Color.BLACK
            val parent = node.parent
            if (parent != null) rotateLeft(parent, parent.parent)
        }
        else {
            sibling?.leftChild?.color = Color.BLACK
            val parent = node.parent
            if (parent != null) rotateRight(parent, parent.parent)
        }
    }
}

fun main(){
    val tree = RBTree<Int, Any>()
    tree.insert(30, "hi")
    tree.insert(35, "hi")
    tree.insert(20, "hi")
    tree.insert(25, "hi")
    tree.insert(22, "hi")
    tree.insert(26, "hi")
    tree.insert(27, "hi")

    //tree.delete(22)
    //tree.findRelatives(27)

    tree.inorderTraversal()
}