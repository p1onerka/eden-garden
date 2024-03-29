package abstractions

abstract class abstractNode<K: Comparable<K>, V, someNode: abstractNode<K, V, someNode>>(var key:K, var value: V) {
    var leftChild: someNode? = null
    var rightChild: someNode? = null
}

class BSNode<K : Comparable<K>, V>(key: K, value: V) : abstractNode<K, V, BSNode<K, V>>(key, value)

abstract class abstractTree<K: Comparable<K>, V, someNode: abstractNode<K, V, someNode>> {
    protected var root: someNode? = null

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
        deleteNode(key)
    }

    protected fun deleteNode(key: K): someNode? {
        val nodeToDelete = findNodeByKey(key)
        if ((nodeToDelete == null) || (root == null)) return null
        val parentNode = findParent(nodeToDelete)
        /* no children case */
        if (nodeToDelete.leftChild == null && nodeToDelete.rightChild == null) {
            moveParentNode(nodeToDelete, parentNode, null)
            /* new */
            return null
        }
        /* 1 child case */
        else if (nodeToDelete.leftChild == null || nodeToDelete.rightChild == null) {
            if (nodeToDelete.leftChild == null) {
                moveParentNode(nodeToDelete, parentNode, nodeToDelete.rightChild)
                /* new */
                return nodeToDelete.rightChild
            }
            else {
                moveParentNode(nodeToDelete, parentNode, nodeToDelete.leftChild)
                /* new */
                return nodeToDelete.leftChild
            }
        }
        /* 2 children case */
        else {
            val replacementNode = findMinNodeInRight(nodeToDelete.rightChild)
                ?: throw IllegalArgumentException ("Node with 2 children must have a right child")
            moveParentNode(replacementNode, findParent(replacementNode), replacementNode.rightChild)
            replacementNode.leftChild = nodeToDelete.leftChild
            replacementNode.rightChild = nodeToDelete.rightChild
            moveParentNode(nodeToDelete, parentNode, replacementNode)
            /* new */
            return replacementNode
        }
    }

    protected fun findParent(node: someNode): someNode? { // вродь работает
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

    /* moves parent of a node to point to a different node instead */
    protected fun moveParentNode(node: someNode, parentNode: someNode?, replacementNode: someNode?) {
        when (parentNode) {
            null -> if (root == node) root = replacementNode
            //else throw IllegalArgumentException("Node's parent cannot be null if it isn't a root")
            else -> {
                if (parentNode.rightChild == node) parentNode.rightChild = replacementNode
                else if (parentNode.leftChild == node) parentNode.leftChild = replacementNode
            }
        }
    }

    /* finds minimal node in a right subtree */
    protected fun findMinNodeInRight(subtree: someNode?): someNode? {
        var minNode = subtree
        while (true) {
            minNode = minNode?.leftChild ?: break
        }
        when {
            (minNode != null) -> return minNode
            else -> return null
        }
    }

    fun find(key: K): V? {
        return findNodeByKey(key)?.value
    }

    protected fun findNodeByKey(key: K): someNode? {
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

//    fun preorderTraverse(): List<K> {
//        val listOfNodes = mutableListOf<someNode>()
//        traverse(root, listOfNodes)
//        val listOfKeys = mutableListOf<K>()
//        listOfNodes.forEach { listOfKeys.add(it.key) }
//        return listOfKeys
//    }

    protected fun traverse(curNode: someNode?, listOfNodes: MutableList<someNode>) {
        if(curNode != null) {
            listOfNodes.add(curNode)
            traverse(curNode.leftChild, listOfNodes)
            traverse(curNode.rightChild, listOfNodes)
        }
    }

    fun inorderTraversal() {
        val count = inorderTraversalRecursive(root, "", 0)
    }

    protected fun inorderTraversalRecursive(node: someNode?, prefix: String, count: Int): Int {
        var counter = count
        if (node != null) {
            if (node.leftChild != null) {
                counter += inorderTraversalRecursive(node.leftChild, "$prefix  ", counter)
            }
            println("$prefix${node.key}")
            if (node.rightChild != null) {
                counter += inorderTraversalRecursive(node.rightChild, "$prefix  ", counter)
            }
        }
        return 1
    }
    private fun recursivePrint(prefix: String, node: someNode?, isLeft: Boolean) {
        if (node == null) {
            return
        }
        println("$prefix└──${node.key}")
        var newPrefix = "$prefix    "
        if (isLeft) {
            newPrefix = "$prefix│   "
        }
        recursivePrint(newPrefix, node.leftChild, true)
        recursivePrint(newPrefix, node.rightChild, false)
    }
    fun print() {
        recursivePrint("", root, false)
    }

}

abstract class balancedTree<K: Comparable<K>, V, someNode: abstractNode<K, V, someNode>>: abstractTree<K, V, someNode>() {
    protected abstract fun balance(curNode: someNode, isAfterInsert: Boolean = true)

    protected open fun rotateRight(node: someNode, parentNode:  someNode?) {
        val tempNode = node.leftChild ?: throw IllegalArgumentException("Node must have left child for right rotation")
        node.leftChild = tempNode.rightChild
        tempNode.rightChild = node
        moveParentNode(node, parentNode, tempNode)
    }

    protected open fun rotateLeft(node: someNode, parentNode:  someNode?) {
        val tempNode = node.rightChild ?: throw IllegalArgumentException("Node must have right child for left rotation")
        node.rightChild = tempNode.leftChild
        tempNode.leftChild = node
        moveParentNode(node, parentNode, tempNode)
    }
}

class BSTree<K : Comparable<K>, V> : abstractTree<K, V, BSNode<K, V>>() {
    override fun createNewNode(key: K, value: V): BSNode<K, V> = BSNode(key, value)
    fun preorderTraverse(): List<K> {
        val listOfNodes = mutableListOf<BSNode<K, V>>()
        traverse(root, listOfNodes)
        val listOfKeys = mutableListOf<K>()
        listOfNodes.forEach { listOfKeys.add(it.key) }
        return listOfKeys
    }
}

fun main() {
    val tree = BSTree<Int, Any>()
    tree.insert(70, "hi")
    tree.insert(11000, "hi")
    tree.insert(50, "hi")
    tree.insert(1000, "hi")
    tree.insert(6, "hi")
    tree.insert(9000, "hi")
    tree.insert(1200000000, "hi")
    tree.insert(8000, "hi")
    tree.insert(1, "hi")
//    tree.print()
//    val myList = tree.preorderTraverse()
//    for (item in myList) {
//        print("$item ")
//    }

}