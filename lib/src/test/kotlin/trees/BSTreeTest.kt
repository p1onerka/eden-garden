package trees

import abstractions.BSTree
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class BSTreeTest {
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
    fun `insert two nodes with same key`() {
        val tree = BSTree<Int, Any>()
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
    fun `delete node with no children`() {
        val tree = BSTree<Int, Any>()
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
    fun `delete node with one child`() {
        val tree = BSTree<Int, Any>()
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
    fun `delete node with two children`() {
        val tree = BSTree<Int, Any>()
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