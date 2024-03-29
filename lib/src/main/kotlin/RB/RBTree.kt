package RB

import AVL.AVLNode
import abstractions.*

enum class Color {
    RED, BLACK
}

class RBNode<K : Comparable<K>, V>(key: K, value: V): abstractNode<K, V, RBNode<K, V>>(key, value) {
    var color: Color = Color.RED
}

class RBTree<K : Comparable<K>, V>: balancedTree<K, V, RBNode<K, V>>() {
    override fun createNewNode(key: K, value: V): RBNode<K, V> = RBNode(key, value)

    override fun insert(key: K, value: V) {
        val insertedNode = insertNode(key, value) ?: throw IllegalArgumentException("Nothing to insert")
        balance(insertedNode, true)
    }

    override fun delete(key: K) {
        val nodeToDelete = findNodeByKey(key)
        nodeToDelete?.let { balance(nodeToDelete, false) }
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

    override fun balance(curNode: RBNode<K, V>, isAfterInsert: Boolean) {
        if (isAfterInsert) { balanceAfterInsert(curNode)}
        else { balanceAfterDelete(curNode) }
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

        /* case 4: uncle is black; grandparent, parent and node form a "triangle"
        *    G                        G
        *   /                          \
        *  P    - left triangle         P   - right triangle
        *   \                          /
        *    N                        N
        *
        */
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

        /* case 5: grandparent, parent and node form a "line"
        *
        *      G                   G
        *     /                     \
        *    P    - left line        P   - right line
        *   /                         \
        *  N                           N
        *
        */
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

//    private fun isLeaf(node: RBNode<K, V>): Boolean {
//        return if (node.rightChild == null && node.leftChild == null) true
//        else false
//    }

    private fun deleteLeaf(node: RBNode<K, V>) {
        println("deleting leaf")
        if (node.color != Color.RED) deleteCase1(node)
    }

    private fun balanceAfterDelete(curNode: RBNode<K, V>) {
        val nodeToDelete = curNode
        val child: RBNode<K, V> ?
        when {
            nodeToDelete.rightChild != null && nodeToDelete.leftChild != null -> {
                child = findMinNodeInRight(nodeToDelete.rightChild)
                    ?: throw IllegalArgumentException("Node must have right child")
                val newNode = child
                println("delete node with 2 children")
                delete(child.key)
                newNode.color = nodeToDelete.color
                newNode.leftChild = nodeToDelete.leftChild
                newNode.rightChild = nodeToDelete.rightChild
                moveParentNode(nodeToDelete, findParent(nodeToDelete), newNode)
                return
//                if(nodeToDelete.color == Color.RED) {
//                    child.color = Color.RED
//                    deleteNode(nodeToDelete.key)
//                    return
//                }
            }
            nodeToDelete.rightChild != null -> {
                println("delete node with right child")
                child = nodeToDelete.rightChild
            }
            nodeToDelete.leftChild != null -> {
                println("delete node with left child")
                child = nodeToDelete.leftChild
            }
            else -> {
                child = null
                deleteLeaf(nodeToDelete)
            }
        }
//        val child = when {
//            nodeToDelete.rightChild != null -> nodeToDelete.rightChild
//            nodeToDelete.leftChild != null -> nodeToDelete.leftChild
//            else -> null
//        }


        if(nodeToDelete.color == Color.RED) {
            println("delete red node")
            deleteNode(nodeToDelete.key)
            return
        }

        if(nodeToDelete.color == Color.BLACK && child?.color == Color.RED) {
            println("delete black node with red child")
            deleteNode(nodeToDelete.key)
            child.color = Color.BLACK
            return
        }
        else {
//        if(nodeToDelete.color == Color.BLACK && child?.color == Color.BLACK) {
            println("delete black node with black child")
            deleteNode(nodeToDelete.key)
            if (child != null) deleteCase1(child)
        }
    }

    /* child of nodeToDelete is new root */
    private fun deleteCase1(node: RBNode<K, V>) {
        println("case 1")
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
                println("case 2.1")
                if (parent != null) {
                    val grandparent = getGrandparent(node)
                    rotateLeft(parent, grandparent)
                    //parent = node

                    //here is the problem
                    //node = node.leftChild ?: return //?: throw IllegalArgumentException("Node should have left child after left rotation")
                }

            }
            else {
                println("case 2.2")
                if (parent != null) {
                    val grandparent = getGrandparent(node)
                    rotateRight(parent, grandparent)
                    //parent = node


                    //here is the problem
                    //node = node.rightChild ?: return //?: throw IllegalArgumentException("Node should have right child after right rotation")
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
        println("hhh")
        val sibling = getSibling(node)
        println("${sibling?.key}")
//        if (sibling != null) findRelatives(sibling.key)
        val parent = findParent(node)
        println("${parent?.key}")
//        if (parent != null) findRelatives(parent.key)
        if(parent?.color == Color.BLACK && (sibling?.color == Color.BLACK) &&
            (sibling.rightChild?.color == Color.BLACK || sibling.rightChild == null)
            && (sibling.leftChild?.color == Color.BLACK || sibling.leftChild == null)) {
            println("case 3")

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
            (sibling.rightChild?.color == Color.BLACK || sibling.rightChild == null)
            && (sibling.leftChild?.color == Color.BLACK || sibling.leftChild == null)) {
            println("case 4")

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
        println("${sibling?.key}")
//        if (sibling != null) findRelatives(sibling.key)
        val parent = findParent(node)
        println("${parent?.key}")
        if (node == parent?.leftChild && sibling?.color == Color.BLACK &&
            (sibling.rightChild?.color == Color.BLACK || sibling.rightChild == null) && sibling.leftChild?.color == Color.RED) {
            println("case 5.1")
            sibling.color = Color.RED
            sibling.leftChild?.color = Color.BLACK

            //val leftNephew = sibling.leftChild
            rotateRight(sibling, parent)
        }
        else if (node == parent?.rightChild && sibling?.color == Color.BLACK &&
            sibling.rightChild?.color == Color.RED && (sibling.leftChild?.color == Color.BLACK || sibling.leftChild == null)) {
            println("case 5.2")

            sibling.color = Color.RED
            sibling.rightChild?.color = Color.BLACK
            println("${sibling.key}, ${sibling.color}")
            println("${sibling.rightChild?.key}, ${sibling.rightChild?.color}")

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
    private fun deleteCase6(node: RBNode<K, V>) {
        //val node = curNode
        val sibling = getSibling(node)
        val parent = findParent(node)
        val grandparent = getGrandparent(node)
        val color = parent?.color
        if (color != null) sibling?.color = color
        parent?.color = Color.BLACK

        if (node == parent?.leftChild) {
            println("case 6.1")
            sibling?.rightChild?.color = Color.BLACK
            if (parent != null) {
                rotateLeft(parent, grandparent)
                //parent = node
                //node = node.leftChild?: throw IllegalArgumentException("Node should have left child after left rotation")
            }
        }
        else {
            println("case 6.2")
            println("${sibling?.key}")
//            findRelatives(node.key)
//            if (sibling != null) findRelatives(sibling.key)
            sibling?.leftChild?.color = Color.BLACK
            println("${sibling?.leftChild?.key}, ${sibling?.leftChild?.color}")
            if (parent != null) {
                rotateRight(parent, grandparent)
                println("${sibling?.leftChild?.key}, ${sibling?.leftChild?.color}")

                //parent = node
                //node = node.rightChild?: throw IllegalArgumentException("Node should have right child after right rotation")
            }
        }
    }

//    fun findRelatives(key: K) {
//        val node = findNodeByKey(key)
//        if (node == null) {
//            println("$key node is null")
//            return
//        }
//        println("$key left is ${node.leftChild?.key}")
//        println("$key right is ${node.rightChild?.key}")
//        println("$key parent is ${findParent(node)?.key}")
//        println("$key color is ${node.color}")
//    }

    fun preorderTraverse(): List<Pair<K, Color>> {
        val listOfNodes = mutableListOf<RBNode<K, V>>()
        traverse(root, listOfNodes)
        val listOfKeysAndColors = mutableListOf<Pair<K, Color>>()
        listOfNodes.forEach { listOfKeysAndColors.add(Pair(it.key, it.color)) }
        return listOfKeysAndColors
    }
}

fun main(){
    val tree = RBTree<Int, String>()
    tree.insert(111, "")
    tree.insert(222, "")
    tree.insert(88, "")
    tree.insert(233, "")
    tree.delete(233)
    tree.delete(88)

    val myList = tree.preorderTraverse()
    for (item in myList) {
        print("$item ")
    }

//    tree.findRelatives(20)
//    tree.findRelatives(10)
//    tree.findRelatives(40)
//    tree.findRelatives(30)
//    tree.findRelatives(33)
//    tree.findRelatives(45)

}