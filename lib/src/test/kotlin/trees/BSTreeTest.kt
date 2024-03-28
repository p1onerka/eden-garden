package trees

import abstractions.BSTree
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class BSTreeTest {
    @Test
    fun `find node by key`() {
        val tree = BSTree<Int, String>()
        tree.insert(4, "Kittens")
        tree.insert(2, "Happiness")
        tree.insert(5, "Puppies")
        tree.insert(3, "Bunnies")
        tree.insert(1, "Birds")
        tree.insert(6,"333333")

        val expectedValue = "Happiness"
        val actuallyValue = tree.find(2)
        assertEquals(expectedValue, actuallyValue)
    }
    @Test
    fun  `traverse bst tree`() {
        val tree = BSTree<Int, String>()
        tree.insert(55, "A")
        tree.insert(88, "B")
        tree.insert(77, "C")
        tree.insert(33, "D")
        tree.insert(22, "E")
        tree.insert(44, "F")
        tree.insert(99, "G")

        val expectedKeys: List<Int> = listOf(55, 33, 22, 44, 88, 77, 99)
        val actualKeys = tree.preorderTraverse()
        assertEquals(expectedKeys, actualKeys)
    }
    @Test
    fun `insert root`() {
        val tree = BSTree<Int, String>()
    }
    @Test
    fun `insert some nodes`() {
        val tree = BSTree<Int, String>()
        tree.insert(4, "Kittens")
        tree.insert(2, "Happiness")
        tree.insert(5, "Puppies")
        tree.insert(3, "Bunnies")
        tree.insert(1, "Birds")
        tree.insert(6,"333333")

        val expectedKeys: List<Int> = listOf(4, 2, 1, 3, 5, 6)
        val actualKeys = tree.preorderTraverse()
        assertEquals(expectedKeys, actualKeys)
    }

    @Test
    fun `insert node with existing key`() {
        val tree = BSTree<Int, String>()
        tree.insert(1, "Kittens")
        tree.insert(2, "Sadness")
        tree.insert(3, "Puppies")
        tree.insert(4, "Bunnies")
        tree.insert(5, "Birds")
        tree.insert(2, "Happiness")
        
        val expectedValue = "Happiness"
        val actualValue = tree.find(2)
        assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `delete node with non-existing key`() {
        val tree = BSTree<Int, String>()
        tree.insert(4, "Kittens")
        tree.insert(2, "Happiness")
        tree.insert(5, "Puppies")
        tree.insert(3, "Bunnies")
        tree.insert(1, "Birds")
        tree.insert(6,"333333")
        tree.delete(10)

        val expectedKeys: List<Int> = listOf(4, 2, 1, 3, 5, 6)
        val actualKeys = tree.preorderTraverse()
        assertEquals(expectedKeys, actualKeys)
    }

    @Test
    fun `delete node from an empty tree`() {
        val tree = BSTree<Int, String>()
        tree.delete(5)

        val expectedKeys: List<Int> = listOf()
        val actualKeys = tree.preorderTraverse()
        assertEquals(expectedKeys, actualKeys)
    }

    @Test
    fun `delete node with no children`() {
        val tree = BSTree<Int, String>()
        tree.insert(4, "Kittens")
        tree.insert(2, "Happiness")
        tree.insert(5, "Puppies")
        tree.insert(3, "Bunnies")
        tree.insert(1, "Birds")
        tree.insert(6,"333333")
        tree.delete(3)

        val expectedKeys: List<Int> = listOf(4, 2, 1, 5, 6)
        val actualKeys = tree.preorderTraverse()
        assertEquals(expectedKeys, actualKeys)
    }

    @Test
    fun `delete node with one right child`() {
        val tree = BSTree<Int, String>()
        tree.insert(4, "Kittens")
        tree.insert(2, "Happiness")
        tree.insert(5, "Puppies")
        tree.insert(3, "Bunnies")
        tree.insert(1, "Birds")
        tree.insert(6,"333333")
        tree.delete(5)

        val expectedKeys: List<Int> = listOf(4, 2, 1, 3, 6)
        val actualKeys = tree.preorderTraverse()
        assertEquals(expectedKeys, actualKeys)
    }

    @Test
    fun `delete node with one left child`() {
        val tree = BSTree<Int, String>()
        tree.insert(3, "Kittens")
        tree.insert(2, "Happiness")
        tree.insert(5, "Puppies")
        tree.insert(4, "Bunnies")
        tree.insert(1, "Birds")
        tree.insert(6,"333333")
        tree.delete(2)

        val expectedKeys: List<Int> = listOf(3, 1, 5, 4, 6)
        val actualKeys = tree.preorderTraverse()
        assertEquals(expectedKeys, actualKeys)
    }

    @Test
    fun `delete node with two children`() {
        val tree = BSTree<Int, String>()
        tree.insert(4, "Kittens")
        tree.insert(2, "Happiness")
        tree.insert(5, "Puppies")
        tree.insert(3, "Bunnies")
        tree.insert(1, "Birds")
        tree.insert(6,"333333")
        tree.delete(2)

        val expectedKeys: List<Int> = listOf(4, 3, 1, 5, 6)
        val actualKeys = tree.preorderTraverse()
        assertEquals(expectedKeys, actualKeys)
    }
}