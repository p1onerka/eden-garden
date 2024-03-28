package trees

import RB.Color
import RB.RBTree
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class RBTreeTest {
    @Test
    fun findNodeByKeyTest() {
        val tree = RBTree<Int, String>()
        tree.insert(2,"Sofa")
        tree.insert(4,"Sonya")
        tree.insert(1,"Xenia")
        
        val expectedValue = "Sofa"
        val actuallyValue = tree.find(2)
        assertEquals(expectedValue, actuallyValue)
    }

    @Test
    fun traverseTreeTest() {
        val tree = RBTree<Int, String>()
        tree.insert(3,"i")
        tree.insert(2,"love")
        tree.insert(1,"making")
        tree.insert(4,"trees")

        val expectedKeysAndColors = listOf(Pair(2, Color.BLACK), Pair(1, Color.BLACK), Pair(3, Color.BLACK), Pair(4, Color.RED))
        val actualKeysAndColors: List<Pair<Int, Color>> = tree.preorderTraverse()
        assertEquals(expectedKeysAndColors, actualKeysAndColors)
    }

    @Test
    fun `insert node with the same key`() {
        val tree = RBTree<Int, String>()
        tree.insert(1, "Kittens")
        tree.insert(2, "Sadness")
        tree.insert(3, "Puppies")
        tree.insert(4, "Bunnies")
        tree.insert(5, "Birds")
        tree.insert(2, "Happiness")

        val actualValue = tree.find(2)
        val expectedValue = "Happiness"
        assertEquals(expectedValue, actualValue)
    }

    //case 1: insertedNode is root
    @Test
    fun `insert a root`() {
        val tree = RBTree<Int, String>()
        tree.insert(8, "Infinity")

        val expectedKeysAndColors = listOf(Pair(8, Color.BLACK))
        val actualKeysAndColors: List<Pair<Int, Color>> = tree.preorderTraverse()
        assertEquals(expectedKeysAndColors, actualKeysAndColors)
    }

    //case 2: parent of insertedNode is black
    @Test
    fun `insert a node with BLACK parent`() {
        val tree = RBTree<Int, String>()
        tree.insert(10, "Sunny")
        tree.insert(8, "Cloudy")
        tree.insert(14, "Stormy")
        tree.insert(15, "Hazy")
        tree.insert(9, "Rainy")

        val expectedKeysAndColors = listOf(Pair(10, Color.BLACK), Pair(8, Color.BLACK),
            Pair(9, Color.RED), Pair(14, Color.BLACK), Pair(15, Color.RED))
        val actualKeysAndColors: List<Pair<Int, Color>> = tree.preorderTraverse()
        assertEquals(expectedKeysAndColors, actualKeysAndColors)
    }

    //case 3: uncle is non-null and red (so both uncle and parent are red)
    @Test
    fun `insert a node with RED parent and RED uncle`() {
        val tree = RBTree<Int, String>()
        tree.insert(100, "Red")
        tree.insert(55, "Orange")
        tree.insert(111, "Yellow")
        tree.insert(33, "Green")

        val expectedKeysAndColors = listOf(Pair(100, Color.BLACK), Pair(55, Color.BLACK), Pair(33, Color.RED), Pair(111, Color.BLACK))
        val actualKeysAndColors: List<Pair<Int, Color>> = tree.preorderTraverse()
        assertEquals(expectedKeysAndColors, actualKeysAndColors)
    }

    /* case 4: uncle is black, parent is red; grandparent, parent and node form a "triangle"
    *    G                        G
    *   /                          \
    *  P    - left triangle         P   - right triangle
    *   \                          /
    *    N                        N
    *
    */
    @Test
    fun `insert a node into a left triangle`() {
        val tree = RBTree<Int, String>()
        tree.insert(50, "Apple")
        tree.insert(30, "Banana")
        tree.insert(40, "Grape")

        val expectedKeysAndColors = listOf(Pair(40, Color.BLACK), Pair(30, Color.RED), Pair(50, Color.RED))
        val actualKeysAndColors: List<Pair<Int, Color>> = tree.preorderTraverse()
        assertEquals(expectedKeysAndColors, actualKeysAndColors)
    }

    @Test
    fun `insert a node into a right triangle`() {
        val tree = RBTree<Int, String>()
        tree.insert(49, "Chocolate")
        tree.insert(34, "Cookies")
        tree.insert(78, "Croissants")
        tree.insert(100, "Candy")
        tree.insert(95, "Creme brulee")

        val expectedKeysAndColors = listOf(Pair(49, Color.BLACK), Pair(34, Color.BLACK),
            Pair(95, Color.BLACK), Pair(78, Color.RED), Pair(100, Color.RED))
        val actualKeysAndColors: List<Pair<Int, Color>> = tree.preorderTraverse()
        assertEquals(expectedKeysAndColors, actualKeysAndColors)
    }

    /* case 5: uncle is black, parent is red; grandparent, parent and node form a "line"
    *      G                   G
    *     /                     \
    *    P    - left line        P   - right line
    *   /                         \
    *  N                           N
    *
    */
    @Test
    fun `insert a node into a left line`() {
        val tree = RBTree<Int, String>()
        tree.insert(900, "folklore")
        tree.insert(90, "evermore")
        tree.insert(9, "midnights")

        val expectedKeysAndColors = listOf(Pair(90, Color.BLACK), Pair(9, Color.RED), Pair(900, Color.RED))
        val actualKeysAndColors: List<Pair<Int, Color>> = tree.preorderTraverse()
        assertEquals(expectedKeysAndColors, actualKeysAndColors)
    }

    @Test
    fun `insert a node into a right line`() {
        val tree = RBTree<Int, String>()
        tree.insert(333, "Didn't")
        tree.insert(33, "the trees")
        tree.insert(3333, "tell us")
        tree.insert(33333, "their stories")
        tree.insert(333333, "?")

        val expectedKeysAndColors = listOf(Pair(333, Color.BLACK), Pair(33, Color.BLACK),
            Pair(33333, Color.BLACK), Pair(3333, Color.RED), Pair(333333, Color.RED))
        val actualKeysAndColors: List<Pair<Int, Color>> = tree.preorderTraverse()
        assertEquals(expectedKeysAndColors, actualKeysAndColors)
    }
}

