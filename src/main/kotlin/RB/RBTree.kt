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
        val newNode = insertNode(key, value)
        if (root == newNode) {
            root = newNode
            newNode?.color = Color.BLACK
            return
        }

        val parent = newNode?.let { findParent(it) }
        if (parent == null) {
            return
        }

        newNode.parent = parent
        fixRedRedViolation(newNode)
        fixBlackBlackViolation(newNode)
        balanceAfterInsert(newNode)
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

    private fun changeColor(node: RBNode<K, V>): RBNode<K, V> {
        if (node.color == Color.BLACK ){
            node.color = Color.RED
        } else {
            node.color = Color.BLACK
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

    override fun balanceAfterInsert(curNode: RBNode<K, V>) {
        var currentNode = curNode
        while (currentNode.parent?.color == Color.RED) {
            var parent = currentNode.parent
                ?: throw IllegalStateException("parent can not be null")
            var grandparent = parent.parent
                ?: throw IllegalStateException("grandparent can not be null")
            if (parent == grandparent.leftChild) {
                var uncle = grandparent.rightChild

                if (uncle?.color == Color.RED) {
                    parent = changeColor(parent)
                    grandparent = changeColor(grandparent)
                    uncle = changeColor(uncle)
                } else {
                    if (parent == grandparent.leftChild) {
                        if (currentNode == parent.rightChild) rotateLeft(parent, grandparent)
                        rotateRight(grandparent, grandparent.parent)
                        currentNode = grandparent.leftChild!! //SORRYYYYYY
                        currentNode.rightChild?.let { changeColor(it) }
                    } else {
                        if (currentNode == parent.leftChild) rotateLeft(parent, grandparent)
                        rotateLeft(grandparent, grandparent.parent)
                        currentNode = grandparent.rightChild!! //SORRYYYYYY
                        currentNode.leftChild?.let { changeColor(it) }
                    }
                    currentNode = changeColor(currentNode)
                    break
                }
            }
            if (currentNode.parent == null && currentNode.color == Color.RED){
                currentNode = changeColor(currentNode)
            }
            while (currentNode.parent != null) {
                currentNode = currentNode.parent
                    ?: throw IllegalStateException("currentNode must have parent")
            }
        }
    }


    override fun balanceAfterDelete(curNode: RBNode<K, V>) {
        var currentNode = curNode
        // Пока текущий узел не явл корнем и он черный
        while (currentNode.parent != null && currentNode.color == Color.BLACK) {
            var parent = currentNode.parent
                ?: throw IllegalStateException("parent can not be null")
            if (currentNode == parent.leftChild) {
                var right = parent.rightChild
                    ?: throw IllegalStateException("parent must have right child")
                if (parent.color == Color.RED){
                    if (right.leftChild?.color == Color.RED || right.rightChild?.color == Color.RED){
                        parent = changeColor(parent)
                        if (right.leftChild?.color == Color.RED) {
                            rotateRight(right, right.parent)
                        } else {
                            right = changeColor(right)
                            right.rightChild = right.rightChild?.let { changeColor(it) }
                        }
                        rotateLeft(parent, parent.parent)
                        currentNode = parent.rightChild!!
                        break
                    } else { //rightChild has not any red children
                        parent = changeColor(parent)
                        right = changeColor(right)
                    }
                    break
                } else{

                }

//                if (sibling != null && sibling.leftChild?.color == Color.BLACK && sibling.rightChild?.color == Color.BLACK) { // Случай 2: Брат и соседняя вершина брата черные
//                    sibling.color = Color.RED
//                    curNode.parent = parent
//                } else {
//                    if (sibling?.rightChild?.color == Color.BLACK) { // Случай 3: Соседняя вершина брата черная
//                        sibling.leftChild?.color = Color.BLACK
//                        sibling.color = Color.RED
//                        rotateRight(curNode, sibling)
//                        sibling = getSibling(curNode)
//                    } else { // Случай 4: Соседняя вершина брата красная
//                        sibling?.leftChild?.color = Color.RED
//                        sibling?.color = Color.BLACK
//                        if (curNode != root) {
//                            rotateRight(curNode, sibling)
//                            sibling = getSibling(curNode)
//                        }
//                    }
//                }
//            } else { //текущий узел справа от родителя
//                if (sibling?.color == Color.RED) {
//                    sibling.color = parent.color
//                    parent.color = Color.BLACK
//                    sibling.leftChild?.color = Color.BLACK
//                    rotateRight(curNode, getSibling(curNode))
//                    sibling = getSibling(curNode)
//                }
//
//                if (sibling?.rightChild?.color == Color.BLACK && sibling.leftChild?.color == Color.BLACK) { // Случай 2: Брат и соседняя вершина брата черные
//                    sibling.color = Color.RED
//                    curNode.parent = parent
//                    parent = curNode.parent
//                } else {
//                    if (sibling?.leftChild?.color == Color.BLACK) { // Случай 3: Соседняя вершина брата черная
//                        sibling.rightChild?.color = Color.BLACK
//                        sibling.color = Color.RED
//                        rotateLeft(curNode, sibling)// meow
//                        parent = curNode.parent
//                    } else {
//                        sibling?.color = parent.color
//                        parent.color = Color.BLACK
//                        sibling?.leftChild?.color = Color.BLACK
//                         if (curNode != root) {
//                             curNode.parent?.let { rotateRight(it, sibling) }
//                             parent = curNode.parent
//                         }
//                    }
            }
        }
        curNode.color = Color.BLACK
    }
}


fun main(){
    val tree = RBTree<Int, Any>()
    tree.insert(7, "hi")
    tree.insert(11, "hi")
    tree.insert(5, "hi")
    tree.insert(12, "hi")
    val myList = tree.preorderTraversal()
    for (item in myList) {
        print("$item ")
    }
}