package trees

import AVL.AVLTree
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test


class AVLTreeTest {

    @Test
    fun searchNodeByKeyTest() {
        val tree = AVLTree<Int, String>()
        tree.insert(2,"Sofa")
        tree.insert(4,"Sonya")
        tree.insert(1,"Xenia")

        val currentlyValue = tree.find(2)
        val expectedValue = "Sofa"

        assertEquals(expectedValue, currentlyValue)
    }

    @Test
    fun traverseTreeTest() {
        val tree = AVLTree<Int, String>()
        tree.insert(3,"i")
        tree.insert(2,"love")
        tree.insert(1,"making")
        tree.insert(4,"trees")

        val expectedKeysAndHeights = listOf(Pair(2, 3), Pair(1, 1), Pair(3, 2), Pair(4, 1))
        val currentKeysAndHeights: List<Pair<Int, Int>> = tree.preorderTraverse()
        assertEquals(expectedKeysAndHeights, currentKeysAndHeights)
    }

    @Test
    fun `insert node with the same key`() {
        val tree = AVLTree<Int, String>()
        tree.insert(1, "Kittens")
        tree.insert(2, "Sadness")
        tree.insert(3, "Puppies")
        tree.insert(4, "Bunnies")
        tree.insert(5, "Birds")
        tree.insert(2, "Happiness")

        val currentValue = tree.find(2)
        val expectedValue = "Happiness"

        assertEquals(expectedValue, currentValue)
    }
    @Test
    fun `insertion left-left`() {
        val tree = AVLTree<Int, String>()
        tree.insert(4,"hi")
        tree.insert(2,"hi")
        tree.insert(5,"hi")
        tree.insert(0,"hii")
        tree.insert(3,"hiiii")

        tree.insert(1,"hey")

        val expectedKeysAndHeights = listOf(Pair(2, 3), Pair(0,2), Pair(1, 1), Pair(4, 2), Pair(3, 1), Pair(5, 1))
        val currentKeysAndHeights = tree.preorderTraverse()
        assertEquals(expectedKeysAndHeights, currentKeysAndHeights)
    }

    @Test
    fun `insertion left-right`() {
        val tree = AVLTree<Int, String>()
        tree.insert(4,"y")
        tree.insert(1,"n")
        tree.insert(5,"d")
        tree.insert(0,"e")
        tree.insert(2,"r")

        tree.insert(3,"p")
        val expectedKeysAndHeights = listOf(Pair(2, 3), Pair(1,2), Pair(0, 1), Pair(4, 2), Pair(3, 1), Pair(5, 1))
        val currentKeysAndHeights = tree.preorderTraverse()
        assertEquals(expectedKeysAndHeights, currentKeysAndHeights)
    }

    @Test
    fun `insertion right-left`() {
        val tree = AVLTree<Int, String>()
        tree.insert(1,"3")
        tree.insert(0,"33")
        tree.insert(4,"333")
        tree.insert(3,"3333")
        tree.insert(5,"33333")

        tree.insert(2,"333333")

        val expectedKeysAndHeights = listOf(Pair(3, 3), Pair(1, 2), Pair(0, 1), Pair(2, 1), Pair(4, 2), Pair(5, 1))
        val currentKeysAndHeights = tree.preorderTraverse()
        assertEquals(expectedKeysAndHeights, currentKeysAndHeights)
    }

    @Test
    fun `insertion right-right`() {
        val tree = AVLTree<Int, String>()
        tree.insert(1,"Shine")
        tree.insert(0,"Bright")
        tree.insert(3,"Like")
        tree.insert(2,"A")
        tree.insert(5,"Diamond")

        tree.insert(4,"Eeeeee")

        val expectedKeysAndHeights = listOf(Pair(3, 3), Pair(1, 2), Pair(0, 1), Pair(2, 1), Pair(5, 2), Pair(4, 1))
        val currentKeysAndHeights = tree.preorderTraverse()
        assertEquals(expectedKeysAndHeights, currentKeysAndHeights)
    }

    @Test
    fun `delete_childless_right when left-with-left-child_case`() {
        val tree = AVLTree<Int, String>()
        tree.insert(4,"Xenia")
        tree.insert(3,"Sofa")
        tree.insert(5,"Sonya")
        tree.insert(2,"Iakov")
        tree.delete(5)

        val expectedKeysAndHeights = listOf(Pair(3, 2), Pair(2, 1), Pair(4, 1))
        val currentKeysAndHeights = tree.preorderTraverse()
        assertEquals(expectedKeysAndHeights, currentKeysAndHeights)
    }

    @Test
    fun `delete childless left when right with left child`() {
        val tree = AVLTree<Int, String>()
        tree.insert(2,"Xenia")
        tree.insert(1,"Sofa")
        tree.insert(4,"Sonya")
        tree.insert(3,"Iakov")

        tree.delete(1)

        val expectedKeysAndHeights = listOf(Pair(3, 2), Pair(2, 1), Pair(4, 1))
        val currentKeysAndHeights = tree.preorderTraverse()
        assertEquals(expectedKeysAndHeights, currentKeysAndHeights)
    }

    @Test
    fun `delete childless right when left with right child`() {
        val tree = AVLTree<Int, String>()
        tree.insert(3,"Xenia")
        tree.insert(1,"Sofa")
        tree.insert(4,"Sonya")
        tree.insert(2,"Iakov")

        tree.delete(4)

        val expectedKeysAndHeights = listOf(Pair(2, 2), Pair(1, 1), Pair(3, 1))
        val currentKeysAndHeights = tree.preorderTraverse()
        assertEquals(expectedKeysAndHeights, currentKeysAndHeights)
    }

    @Test
    fun `delite childless left when right with right child`() {
        val tree = AVLTree<Int, String>()
        tree.insert(4,"Xenia")
        tree.insert(3,"Sofa")
        tree.insert(5,"Sonya")
        tree.insert(6,"Iakov")

        tree.delete(3)

        val expectedKeysAndHeights = listOf(Pair(5, 2), Pair(4, 1), Pair(6, 1))
        val currentKeysAndHeights = tree.preorderTraverse()
        assertEquals(expectedKeysAndHeights, currentKeysAndHeights)
    }

    @Test
    fun `left-right rotation after delete node with one child`() {
        val tree = AVLTree<Int, String>()
        tree.insert(4,"Xenia")
        tree.insert(6,"Sofa")
        tree.insert(1,"Sonya")
        tree.insert(3,"Iakov")
        tree.insert(0,"Sofa")
        tree.insert(5,"Sonya")
        tree.insert(2,"Iakov")

        tree.delete(6)

        val expectedKeysAndHeights = listOf(Pair(3, 3), Pair(1, 2), Pair(0, 1), Pair(2, 1), Pair(4, 2), Pair(5, 1))
        val currentKeysAndHeights = tree.preorderTraverse()
        assertEquals(expectedKeysAndHeights, currentKeysAndHeights)
    }

    @Test
    fun `right-left rotation after delete node with one child`() {
        val tree = AVLTree<Int, String>()
        tree.insert(2,"Xenia")
        tree.insert(0,"Sofa")
        tree.insert(1,"Sonya")
        tree.insert(5,"Iakov")
        tree.insert(6,"Sofa")
        tree.insert(3,"Sonya")
        tree.insert(4,"Iakov")

        tree.delete(0)

        val expectedKeysAndHeights = listOf(Pair(3, 3), Pair(2, 2), Pair(1, 1), Pair(5, 2), Pair(4, 1), Pair(6, 1))
        val currentKeysAndHeights = tree.preorderTraverse()
        assertEquals(expectedKeysAndHeights, currentKeysAndHeights)
    }

    @Test
    fun `right rotation after delete node with one child`() {
        val tree = AVLTree<Int, String>()
        tree.insert(5,"Xenia")
        tree.insert(3,"Sofa")
        tree.insert(7,"Sonya")
        tree.insert(1,"Iakov")
        tree.insert(4,"Sofa")
        tree.insert(6,"Sonya")
        tree.insert(2,"Iakov")

        tree.delete(7)

        val expectedKeysAndHeights = listOf(Pair(3, 3), Pair(1, 2), Pair(2, 1), Pair(5, 2), Pair(4, 1), Pair(6, 1))
        val currentKeysAndHeights = tree.preorderTraverse()
        assertEquals(expectedKeysAndHeights, currentKeysAndHeights)
    }

    @Test
    fun `left rotation after delete node with one child`() {
        val tree = AVLTree<Int, String>()
        tree.insert(3, "Xenia")
        tree.insert(1, "Sofa")
        tree.insert(5, "Sonya")
        tree.insert(2, "Iakov")
        tree.insert(4, "Sofa")
        tree.insert(6, "Sonya")
        tree.insert(7, "Iakov")

        tree.delete(1)

        val expectedKeysAndHeights = listOf(Pair(5, 3), Pair(3, 2), Pair(2, 1), Pair(4, 1), Pair(6, 2), Pair(7, 1))
        val currentKeysAndHeights = tree.preorderTraverse()
        assertEquals(expectedKeysAndHeights, currentKeysAndHeights)
    }

    @Test
    fun `left rotation after delete node with two children`() {
        val tree = AVLTree<Int, String>()
        tree.insert(5, "Xenia")
        tree.insert(3, "Sofa")
        tree.insert(6, "Sonya")
        tree.insert(1, "Kotlin")
        tree.insert(4, "Python")
        tree.insert(7, "C")
        tree.insert(2, "Rust")

        tree.delete(5)

        val expectedKeysAndHeights = listOf(Pair(3, 3), Pair(1, 2), Pair(2, 1), Pair(6, 2), Pair(4, 1), Pair(7, 1))
        val currentKeysAndHeights = tree.preorderTraverse()
        assertEquals(expectedKeysAndHeights, currentKeysAndHeights)
    }

    @Test
    fun `delete node with two children and replacing him by his child`() {
        val tree = AVLTree<Int, String>()
        tree.insert(3, "Xenia")
        tree.insert(2, "Sofa")
        tree.insert(5, "Sonya")
        tree.insert(1, "Kotlin")
        tree.insert(4, "Python")
        tree.insert(7, "C")
        tree.insert(6, "Rust")

        tree.delete(3)

        val expectedKeysAndHeights = listOf(Pair(4, 3), Pair(2, 2), Pair(1, 1), Pair(6, 2), Pair(5, 1), Pair(7, 1))
        val currentKeysAndHeights = tree.preorderTraverse()
        assertEquals(expectedKeysAndHeights, currentKeysAndHeights)
    }

    @Test
    fun `delete node with two children and perform left rotation`() {
        val tree = AVLTree<Int, String>()
        tree.insert(4, "Xenia")
        tree.insert(2, "Sofa")
        tree.insert(5, "Sonya")
        tree.insert(1, "Kotlin")
        tree.insert(3, "Python")

        tree.delete(4)

        val expectedKeysAndHeights = listOf(Pair(2, 3), Pair(1, 1), Pair(5, 2), Pair(3, 1))
        val currentKeysAndHeights = tree.preorderTraverse()
        assertEquals(expectedKeysAndHeights, currentKeysAndHeights)
    }

    @Test
    fun `delete node with two children and perform left-right rotation`() {
        val tree = AVLTree<Int, String>()
        tree.insert(3, "Xenia")
        tree.insert(4, "Sofa")
        tree.insert(1, "Sonya")
        tree.insert(2, "Kotlin")

        tree.delete(4)
        val expectedKeysAndHeights = listOf(Pair(2, 2), Pair(1, 1), Pair(3, 1))
        val currentKeysAndHeights = tree.preorderTraverse()
        assertEquals(expectedKeysAndHeights, currentKeysAndHeights)
    }
}