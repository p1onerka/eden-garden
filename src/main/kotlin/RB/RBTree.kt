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
//    fun getColor(node: RBNode<K, V>): RBNode<K, V>? {
//        return if (node.color == C) {
//            node.parent?.rightChild
//        } else {
//            node.parent?.leftChild
//        }
//    }

//    fun setColor(newColor: Color) {
//
//    }
    override fun insert(key: K, value: V) {
        val newNode = insertNode(key, value)

        if (newNode == null) {
            //println("no insert have been found")
            return
        }
        if (root == newNode) {
            newNode.color = Color.BLACK
            return
        }

        val parent = findParent(newNode)
            ?: throw IllegalArgumentException("non-root should have parent")

        newNode.parent = parent

        println("parent of ${newNode.key} is ${newNode.parent?.key}")

        balanceAfterInsert(newNode)

        fixRedRedViolation(newNode)
        fixBlackBlackViolation(newNode)
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
                    rotateLeft(node, node.parent)
                    node = node.leftChild ?: throw IllegalStateException("Left child is null")
                } else if (node == node.parent?.leftChild && node.parent == grandparent?.rightChild) {
                    rotateRight(node, node.parent)
                    node = node.rightChild ?: throw IllegalStateException("Right child is null")
                }

                if (node == node.parent?.leftChild) {
                    rotateRight(node, grandparent)
                } else {
                    rotateLeft(node, grandparent)
                }

                node.parent?.color = Color.BLACK
                grandparent?.color = Color.RED
            }
        }
    }
    private fun fixBlackBlackViolation(currentNode: RBNode<K, V>?) {
        var curNode = currentNode
        while (curNode != root && (curNode?.color == Color.BLACK)) {
            var sibling = getSibling(curNode)
            if (sibling?.color == Color.RED) {
                sibling.color = Color.BLACK
                curNode.parent?.color = Color.RED
                if (curNode == curNode.parent?.leftChild) {
                    rotateLeft(sibling, curNode.parent)
                } else {
                    rotateRight(sibling, curNode.parent)
                }
            }

            val leftNephew = sibling?.leftChild
            val rightNephew = sibling?.rightChild
            if ((sibling?.leftChild == null || leftNephew?.color == Color.BLACK) &&
                (sibling?.rightChild == null || rightNephew?.color == Color.BLACK)) {
                sibling?.color = Color.RED
                curNode = curNode.parent
            } else {
                if (curNode == curNode.parent?.leftChild && rightNephew?.color == Color.BLACK) {
                    leftNephew?.color = Color.BLACK
                    sibling.color = Color.RED
                    if (leftNephew != null) {
                        rotateRight(leftNephew, sibling)
                    }
                    sibling = curNode.parent?.rightChild
                } else if (curNode == curNode.parent?.rightChild && leftNephew?.color == Color.BLACK) {
                    rightNephew?.color = Color.BLACK
                    sibling.color = Color.RED
                    if (rightNephew != null) {
                        rotateLeft(rightNephew, sibling)
                    }
                    sibling = curNode.parent?.leftChild
                }

                sibling?.color = curNode.parent?.color ?: Color.BLACK
                curNode.parent?.color = Color.BLACK

                if (curNode == curNode.parent?.leftChild) {
                    rightNephew?.color = Color.BLACK
                    if (sibling != null) {
                        rotateLeft(sibling, curNode.parent)
                    }
                } else {
                    leftNephew?.color = Color.BLACK
                    if (sibling != null) {
                        rotateRight(sibling, curNode.parent)
                    }
                }
                curNode = root
            }
        }
    }


    override fun rotateRight(node: RBNode<K, V>, parentNode: RBNode<K, V>?) {
        val tempNode = node.leftChild ?: throw IllegalArgumentException("Node must have left child for right rotation")
        node.leftChild = tempNode.rightChild
        tempNode.rightChild = node
        if (tempNode == root) {
            tempNode.leftChild?.color = Color.BLACK
        }
    }
    override fun rotateLeft(node: RBNode<K, V>, parentNode: RBNode<K, V>?) {
        val tempNode = node.rightChild ?: throw IllegalArgumentException("Node must have right child for left rotation")
        node.rightChild = tempNode.leftChild
        tempNode.leftChild = node
        if (tempNode == root) {
            tempNode.rightChild?.color = Color.BLACK
        }
    }

//    private fun rotateLeft(node: RBNode<K, V>?) {
//        val newRoot = node?.rightChild ?: return
//        node.rightChild = newRoot.leftChild
//        if (newRoot.leftChild != null) {
//            newRoot.leftChild?.parent = node
//        }
//        newRoot.parent = node.parent
//        if (node.parent == null) {
//            root = newRoot
//        } else if (node == node.parent?.leftChild) {
//            node.parent?.leftChild = newRoot
//        } else {
//            node.parent?.rightChild = newRoot
//        }
//        newRoot.leftChild = node
//        node.parent = newRoot
//    }

//    override fun rotateRight(node: RBNode<K, V>, parentNode:  RBNode<K, V>?) {
//        val newRoot = node?.leftChild ?: return
//        node.rightChild = newRoot.leftChild
//        if (newRoot.rightChild != null) {
//            newRoot.rightChild?.parent = node
//        }
//        newRoot.parent = node.parent
//        if (node.parent == null) {
//            root = newRoot
//        } else if (node == node.parent?.rightChild) {
//            node.parent?.rightChild = newRoot
//        } else {
//            node.parent?.leftChild = newRoot
//        }
//        newRoot.rightChild = node
//        node.parent = newRoot
//    }
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
        var currentNode = curNode
        while (currentNode.parent?.color == Color.RED) {
            var parent = currentNode.parent

            val grandparent = parent?.parent

            if (parent == grandparent?.leftChild) {
                val uncle = grandparent?.rightChild

                if (uncle?.color == Color.RED) {
                    parent?.color = Color.BLACK
                    uncle.color = Color.BLACK
                    grandparent.color = Color.RED
                    currentNode = grandparent
                } else {
                    if (currentNode == parent?.rightChild) {
                        rotateLeft(currentNode, parent)
                        currentNode = parent
                        parent = currentNode.parent
                    }

                    parent?.color = Color.BLACK
                    grandparent?.color = Color.RED
                    rotateRight(currentNode, grandparent)
                }
            } else {
                val uncle = grandparent?.leftChild

                if (uncle?.color == Color.RED) {
                    parent?.color = Color.BLACK
                    uncle.color = Color.BLACK
                    grandparent.color = Color.RED
                    currentNode = grandparent
                } else {
                    if (currentNode == parent?.leftChild) {
                        rotateRight(currentNode, parent)
                        currentNode = parent
                        parent = currentNode.parent
                    }

                    parent?.color = Color.BLACK
                    grandparent?.color = Color.RED
                    rotateLeft(currentNode, grandparent)
                }
            }
        }
    }


    override fun balanceAfterDelete(curNode: RBNode<K, V>) {
        var parent = curNode.parent
        // Пока текущий узел не явл корнем и он черный
        while (curNode != parent && curNode.color == Color.BLACK && parent != null) {
            var sibling = getSibling(curNode)

            if (curNode == parent.leftChild) {
                if (sibling != null && parent.color == Color.BLACK)// Текущий узел находится слева от родителя
                    if (sibling.color == Color.RED) {
                        sibling.color = Color.BLACK
                        parent.color = Color.RED
                        rotateLeft(curNode, curNode.parent)
                        sibling = getSibling(curNode)
                }

                if (sibling != null && sibling.leftChild?.color == Color.BLACK && sibling.rightChild?.color == Color.BLACK) { // Случай 2: Брат и соседняя вершина брата черные
                    sibling.color = Color.RED
                    curNode.parent = parent
                } else {
                    if (sibling?.rightChild?.color == Color.BLACK) { // Случай 3: Соседняя вершина брата черная
                        sibling.leftChild?.color = Color.BLACK
                        sibling.color = Color.RED
                        rotateRight(curNode, sibling)
                        sibling = getSibling(curNode)
                    } else { // Случай 4: Соседняя вершина брата красная
                        sibling?.leftChild?.color = Color.RED
                        sibling?.color = Color.BLACK
                        if (curNode != root) {
                            rotateRight(curNode, sibling)
                            sibling = getSibling(curNode)
                        }
                    }
                }
            } else { //текущий узел справа от родителя
                if (sibling?.color == Color.RED) { 
                    sibling.color = parent.color
                    parent.color = Color.BLACK
                    sibling.leftChild?.color = Color.BLACK
                    rotateRight(curNode, getSibling(curNode))
                    sibling = getSibling(curNode)
                }

                if (sibling?.rightChild?.color == Color.BLACK && sibling.leftChild?.color == Color.BLACK) { // Случай 2: Брат и соседняя вершина брата черные
                    sibling.color = Color.RED
                    curNode.parent = parent
                    parent = curNode.parent
                } else {
                    if (sibling?.leftChild?.color == Color.BLACK) { // Случай 3: Соседняя вершина брата черная
                        sibling.rightChild?.color = Color.BLACK
                        sibling.color = Color.RED
                        rotateLeft(curNode, sibling)// meow
                        parent = curNode.parent
                    } else {
                        sibling?.color = parent.color
                        parent.color = Color.BLACK
                        sibling?.leftChild?.color = Color.BLACK
                         if (curNode != root) {
                             curNode.parent?.let { rotateRight(it, sibling) }
                             parent = curNode.parent
                         }
                    }
                }
            }
            curNode.color = Color.BLACK
        }
    }
    override fun delete(key: K) {
        val parent = deleteNode(key)
        parent?.let {
            balanceAfterDelete(parent)
            //fixRedRedViolation(parent)
            //fixBlackBlackViolation(parent)
        }
    }
}

fun main() {
    var rb = RBTree<Int, Any>()
    rb.insert(4, 1)
    rb.insert(5, 1)
    rb.insert(3, 1)
    rb.insert(2, 1)
    rb.delete(5)
    //rb.insert(7, 10)

    val myList = rb.preorderTraversal()
    for (item in myList) {
        print("$item ")
    }
}