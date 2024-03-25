## About the project

This library allows you to use three types of binary search trees: simple, AVL and Red-Black tree. [AVL](https://en.wikipedia.org/wiki/AVL_tree) and [Red-Black](https://en.wikipedia.org/wiki/Red–black_tree) tree implement their natural balancing.


## How to build
Gradle
## How to use
To start implementing library install package.

There are 4 public methods you can use for each tree:

Note that key should be of Comparable type:
* `insert(key, value)`  inserts a node with such key and value. If a node with the same key already exists in the tree, old value is replaced with the new one.
* `delete(key)`  deletes a node with such key. If there is no node with that key, nothing is done.
* `find(key)`  finds a node with such key and returns its value. If there is no node with that key, null is returned.
* `traverse()`  traverses the tree from `parent` to `left child` to `right child`

Now you can simply create a tree: `BSTree`, `AVLTree` or `RBTree`:
```
  val tree = RBTree<Int, String>()
  ```
Start inserting and deleting nodes:
```
  tree.insert(11, "Welcome to the jungle")
  tree.insert(9, "We got fun and games")
  tree.insert(27, "We take it day by day")
  tree.delete(11)
  val value = find(9)  // value = "We got fun and games"
  val value = find(15) // value = null
  ```
Have fun!
## License
The product is distributed under MIT license. Check LICENSE for more information
## Developers & contacts
* [p1onerka](https://github.com/p1onerka) (tg @p10nerka)  
* [sofyak0zyreva](https://github.com/sofyak0zyreva) (tg @soffque)  
* [shvorobsofia](https://github.com/shvorobsofia) (tg @fshv23)  
