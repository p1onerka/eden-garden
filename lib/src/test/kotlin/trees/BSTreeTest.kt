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
        tree.insert(100, "There's the root!")

        val expectedKeys: List<Int> = listOf(100)
        val actualKeys = tree.preorderTraverse()
        assertEquals(expectedKeys, actualKeys)
    }
    @Test
    fun `insert some nodes`() {
        val tree = BSTree<Int, String>()
        tree.insert(44, "Sofa")
        tree.insert(23, "Xenia")
        tree.insert(58, "Vladimir")
        tree.insert(37, "Iakov")
        tree.insert(19, "Egor")
        tree.insert(60,"James")

        val expectedKeys: List<Int> = listOf(44, 23, 19, 37, 58, 60)
        val actualKeys = tree.preorderTraverse()
        assertEquals(expectedKeys, actualKeys)
    }

    @Test
    fun `insert node with existing key`() {
        val tree = BSTree<Int, String>()
        tree.insert(2, "Qwerty")
        tree.insert(4, "Asdfgh")
        tree.insert(6, "12345")
        tree.insert(9, "Ycucken")
        tree.insert(11, "Zxc")
        tree.insert(2, "098765")

        val expectedValue = "098765"
        val actualValue = tree.find(2)
        assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `delete node with non-existing key`() {
        val tree = BSTree<Int, String>()
        tree.insert(12, "Apple")
        tree.insert(6, "Samsung")
        tree.insert(15, "Lenovo")
        tree.insert(9, "Nokia")
        tree.insert(3, "Honor")
        tree.insert(18,"ZTE")
        tree.delete(38)

        val expectedKeys: List<Int> = listOf(12, 6, 3, 9, 15, 18)
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
        tree.insert(9, "Volga")
        tree.insert(7, "Mercedes")
        tree.insert(10, "Lamborgini")
        tree.insert(8, "Toyota")
        tree.insert(6, "BMW")
        tree.insert(11,"Reno")
        tree.delete(8)

        val expectedKeys: List<Int> = listOf(9, 7, 6, 10, 11)
        val actualKeys = tree.preorderTraverse()
        assertEquals(expectedKeys, actualKeys)
    }

    @Test
    fun `delete node with one right child`() {
        val tree = BSTree<Int, String>()
        tree.insert(14, "1")
        tree.insert(12, "22")
        tree.insert(15, "333")
        tree.insert(13, "4444")
        tree.insert(11, "55555")
        tree.insert(16,"666666")
        tree.delete(15)

        val expectedKeys: List<Int> = listOf(14, 12, 11, 13, 16)
        val actualKeys = tree.preorderTraverse()
        assertEquals(expectedKeys, actualKeys)
    }

    @Test
    fun `delete node with one left child`() {
        val tree = BSTree<Int, String>()
        tree.insert(9, "Matan")
        tree.insert(6, "DiskretMath")
        tree.insert(15, "Algebra")
        tree.insert(12, "ORG")
        tree.insert(3, "Prorgramming")
        tree.insert(18,"Geomtry")
        tree.delete(6)

        val expectedKeys: List<Int> = listOf(9, 3, 15, 12, 18)
        val actualKeys = tree.preorderTraverse()
        assertEquals(expectedKeys, actualKeys)
    }

    @Test
    fun `delete node with two children`() {
        val tree = BSTree<Int, String>()
        tree.insert(4, "Sunny")
        tree.insert(2, "Foggy")
        tree.insert(5, "Rainy")
        tree.insert(3, "Snowly")
        tree.insert(1, "Hot")
        tree.insert(6,"Cold")
        tree.delete(2)

        val expectedKeys: List<Int> = listOf(4, 3, 1, 5, 6)
        val actualKeys = tree.preorderTraverse()
        assertEquals(expectedKeys, actualKeys)
    }
}
