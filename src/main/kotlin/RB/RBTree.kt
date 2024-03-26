package RB

import abstractions.*

enum class Color {
    RED, BLACK
}

class RBNode<K : Comparable<K>, V>(key: K, value: V): abstractNode<K, V, RBNode<K, V>>(key, value) {
    var color: Color = Color.RED
    //var parent: RBNode<K, V>? = null
}

class RBTree<K : Comparable<K>, V>: balancedTree<K, V, RBNode<K, V>>() {
    override fun createNewNode(key: K, value: V): RBNode<K, V> = RBNode(key, value)

    override fun insert(key: K, value: V) {
        val insertedNode = insertNode(key, value) ?: throw IllegalArgumentException("Nothing to insert")
        balance(insertedNode, true)
    }

    private fun balanceAfterInsert(curNode: RBNode<K, V>) {
        var insertedNode = curNode
        insertedNode.color = Color.RED

        /* case 1: insertedNode is root */
        if (root == insertedNode) {
            insertedNode.color = Color.BLACK
            return
        }

        var parent = findParent(insertedNode) ?: throw IllegalArgumentException("Non-root should have parent")

        /* case 2: parent of insertedNode is black */
        if (parent.color == Color.BLACK) {
            return
        }

        val uncle = getUncle(insertedNode)
        val grandparent = getGrandparent(insertedNode) ?: throw IllegalArgumentException("Every node by that point should have grandparent")

        /* case 3: uncle is non-null and red (so both uncle and parent are red) */
        if ((uncle != null) && (uncle.color == Color.RED)) {
            parent.color = Color.BLACK
            uncle.color = Color.BLACK
            grandparent.color = Color.RED
            balanceAfterInsert(grandparent)
            return
        }

        /* case 4: uncle is black; grandparent, parent and node form a "triangle"; should be continued w case 4 */
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

        /* case 5: grandparent, parent and node form a "line" */
        parent.color = Color.BLACK
        grandparent.color = Color.RED
        if ((insertedNode == parent.leftChild)&&(parent == grandparent.leftChild)) {
            rotateRight(grandparent, findParent(grandparent))

        }
        else {
            rotateLeft(grandparent, findParent(grandparent))
        }
        return
    }

    override fun balance(curNode: RBNode<K, V>, isAfterInsert: Boolean) {
        if (isAfterInsert) { balanceAfterInsert(curNode)}
        else { balanceAfterDelete(curNode) }
    }

    private fun getGrandparent(node: RBNode<K, V>): RBNode<K, V>? {
        val parent = findParent(node) ?: return null
        val grandparent = findParent(parent)
        return grandparent
    }

    private fun getSibling(node: RBNode<K, V>): RBNode<K, V>? {
        val parent = findParent(node) ?: return null
        return if (node == parent.leftChild) {
            parent.rightChild
        } else {
            parent.leftChild
        }
    }

    private fun getUncle(node: RBNode<K, V>): RBNode<K, V>? {
        val parent = findParent(node) ?: return null
        val grandparent = findParent(parent)
        return if (parent == grandparent?.leftChild) {
            grandparent.rightChild
        } else {
            grandparent?.leftChild
        }
    }

    private fun balanceAfterDelete(curNode: RBNode<K, V>) {
        val nodeToDelete = curNode
        val child = when {
            nodeToDelete.rightChild != null -> nodeToDelete.rightChild
            nodeToDelete.leftChild != null -> nodeToDelete.leftChild
            else -> null
        }

        if(nodeToDelete.color == Color.RED) {
            deleteNode(nodeToDelete.key)
            return
        }

        if(nodeToDelete.color == Color.BLACK && child?.color == Color.RED) {
            deleteNode(nodeToDelete.key)
            child.color = Color.BLACK
            return
        }

        if(nodeToDelete.color == Color.BLACK && child?.color == Color.BLACK) {
            deleteNode(nodeToDelete.key)
            deleteCase1(child)
        }
    }

    override fun delete(key: K) {
        val nodeToDelete = findNodeByKey(key)
        nodeToDelete?.let { balance(nodeToDelete, false) }
    }

    /* child of nodeToDelete is new root */
    private fun deleteCase1(node: RBNode<K, V>) {
        val parent = findParent(node)
        if(parent != null) deleteCase2(node)
    }

    /** sibling of child is red
     *          BLACK
     *        /       \
     *      child     RED
     */
    private fun deleteCase2(curNode: RBNode<K, V>) {
        var node = curNode
        val sibling = getSibling(node)
        if(sibling?.color == Color.RED) {
            val parent = findParent(node)
            parent?.color = Color.RED
            sibling.color = Color.BLACK
            if (node == parent?.leftChild) {
                if (parent != null) {
                    val grandparent = getGrandparent(node)
                    rotateLeft(parent, grandparent)
                    //parent = node
                    node = node.leftChild?: throw IllegalArgumentException("Node should have left child after left rotation")
                }

            }
            else {
                if (parent != null) {
                    val grandparent = getGrandparent(node)
                    rotateRight(parent, grandparent)
                    //parent = node
                    node = node.rightChild?: throw IllegalArgumentException("Node should have right child after right rotation")
                }
            }
        }
        deleteCase3(node)
    }

    /** parent, sibling and its children are black
     *       BLACK
     *     /       \
     *   child    BLACK
     *          /       \
     *       BLACK     BLACK
     */
    private fun deleteCase3(node: RBNode<K, V>) {
        val sibling = getSibling(node)
        val parent = findParent(node)
        if(parent?.color == Color.BLACK && sibling?.color == Color.BLACK &&
            sibling.rightChild?.color == Color.BLACK && sibling.leftChild?.color == Color.BLACK) {

            sibling.color = Color.RED
            deleteCase1(parent)
        }
        else deleteCase4(node)
    }

    /** parent is red, sibling and its children are black
     *       RED
     *     /    \
     *   child  BLACK
     *         /    \
     *     BLACK     BLACK
     */
    private fun deleteCase4(node: RBNode<K, V>) {
        val sibling = getSibling(node)
        val parent = findParent(node)
        if(parent?.color == Color.RED && sibling?.color == Color.BLACK &&
            sibling.rightChild?.color == Color.BLACK && sibling.leftChild?.color == Color.BLACK) {

            sibling.color = Color.RED
            parent.color = Color.BLACK
        }
        else deleteCase5(node)
    }

    /** sibling is black & leftChild, its leftChild is red and its rightChild is black
     *             parent
     *             /    \
     *          BLACK  child
     *         /    \
     *       RED   BLACK
     */
    private fun deleteCase5(node: RBNode<K, V>) {
        val sibling = getSibling(node)
        val parent = findParent(node)
        if (node == parent?.leftChild && sibling?.color == Color.BLACK &&
            sibling.rightChild?.color == Color.BLACK && sibling.leftChild?.color == Color.RED) {

            sibling.color = Color.RED
            sibling.leftChild?.color = Color.BLACK

            //val leftNephew = sibling.leftChild
            rotateRight(sibling, parent)
        }
        else if (node == parent?.rightChild && sibling?.color == Color.BLACK &&
            sibling.rightChild?.color == Color.RED && sibling.leftChild?.color == Color.BLACK) {

            sibling.color = Color.RED
            sibling.rightChild?.color = Color.BLACK

            rotateLeft(sibling, parent)
        }
        deleteCase6(node)
    }

    /** sibling is right(left)Child and is black, its right(left)Child is red
     *              parent       or          parent
     *             /     \                  /     \
     *          child  BLACK            BLACK    child
     *                     \             /
     *                     RED         RED
     */
    private fun deleteCase6(curNode: RBNode<K, V>) {
        val node = curNode
        val sibling = getSibling(node)
        val parent = findParent(node)
        val grandparent = getGrandparent(node)
        val color = parent?.color
        if (color != null) sibling?.color = color
        parent?.color = Color.BLACK

        if (node == parent?.leftChild) {
            sibling?.rightChild?.color = Color.BLACK
            if (parent != null) {
                rotateLeft(parent, grandparent)
                //parent = node
                //node = node.leftChild?: throw IllegalArgumentException("Node should have left child after left rotation")
            }
        }
        else {
            sibling?.leftChild?.color = Color.BLACK
            if (parent != null) {
                rotateRight(parent, grandparent)
                //parent = node
                //node = node.rightChild?: throw IllegalArgumentException("Node should have right child after right rotation")
            }
        }
    }

    fun findRelatives(key: K) {
        val node = findNodeByKey(key)
        if (node == null) {
            println("$key node is null")
            return
        }
        println("$key left is ${node.leftChild?.key}")
        println("$key right is ${node.rightChild?.key}")
        println("$key parent is ${findParent(node)?.key}")
        println("$key color is ${node.color}")
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

    tree.findRelatives(30)
    tree.findRelatives(35)
    tree.findRelatives(20)
    tree.findRelatives(25)
    tree.findRelatives(22)
    tree.findRelatives(26)
    tree.findRelatives(27)


    //tree.inorderTraversal()

    println()
    tree.delete(22)
    tree.delete(30)
    //tree.delete(35)
    tree.insert(28, "hi")
    tree.insert(29, "hi")
    tree.findRelatives(30)
    tree.findRelatives(35)
    tree.findRelatives(20)
    tree.findRelatives(25)
    tree.findRelatives(22)
    tree.findRelatives(26)
    tree.findRelatives(27)
    tree.findRelatives(28)
    tree.findRelatives(29)
    //tree.findRelatives(26)

    //tree.inorderTraversal()
}