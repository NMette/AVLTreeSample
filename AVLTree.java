import java.util.LinkedList;
import java.util.Queue;

/**
 * A self-balancing binary search tree that holds objects that extend the Comparable interface.
 * 
 * @author Nathan Mette, Jeziel Banos-Gonzalez, Tony Zhang
 * @version 1.0, 04/05/24, Java 17
 * 
 * @see Comparable
 */

public class AVLTree<T extends Comparable<T>> {
	private Node<T> root;
	private int size;

	/**
	 * Default Constructor
	 * 
	 * @author Nathan Mette
	 */
	public AVLTree() {
		this.root = null;
		size = 0;
	}

	/**
	 * Constructor if given the root
	 * 
	 * @param root the root of the tree to copy.
	 * @author Nathan Mette
	 */
	public AVLTree(Node<T> root) {
		this.root = root;
		size = 1;
	}

	/**
	 * Insert a new value into the AVLTree.
	 * <p>
	 * If a node with the value already exists, increment the count of that node by 1.
	 * 
	 * @param value the value to be inserted
	 * @author Nathan Mette
	 */
	public void insert(T value) {
		root = insertHelper(root, value);
	}

	/**
	 * Insert a new value into the AVLTree recursively.
	 * 
	 * @param curr  the node to be evaluated for insertion
	 * @param value the value to be inserted
	 * @return The current node.
	 * @author Jeziel Banos-Gonzalez w/ support from Nathan Mette
	 */
	private Node<T> insertHelper(Node<T> curr, T value) {
		if (curr == null) {
			curr = new Node<T>(value);
			size++;
			return curr;
		}
		int compareValue = compare(value, curr.value);

		if (compareValue < 0) { // Go left
			curr.left = insertHelper(curr.left, value);
		} else if (compareValue > 0) { // Go right
			curr.right = insertHelper(curr.right, value);
		} else { // Increase count of node
			curr.quantity++;
		}

		// Check if rotate is required as recursion returns
		curr.weight = recalcWeightOfNode(curr);

		if (curr.weight >= 2) {
			curr = rotateLeft(curr);
		} else if (curr.weight <= -2) {
			curr = rotateRight(curr);
		}

		curr.height = recalcHeightOfNode(curr);
		return curr;
	}
	
	/**
	 * Rotates the subtree with the passed root to the left.
	 * <p>
	 * Assumes Node passed is the root of the tree being
	 * rotated.
	 * 
	 * @param root the root of the subtree to be rotated left
	 * @return The new root of the subtree.
	 * @author Nathan Mette
	 */
	private Node<T> rotateLeft(Node<T> root) {
		if (root.right == null) {
			return null; // I shouldn't reach this
		}

		root.right.height = recalcHeightOfNode(root.right);
		root.right.weight = recalcWeightOfNode(root.right);
		if (root.right.weight == -1) {
			root.right = rotateRight(root.right); // Only in the event that the right needs to be rotated to keep
													// balance too
		}

		Node<T> origin = root;
		Node<T> newOrigin = root.right;

		origin.right = newOrigin.left;
		newOrigin.left = origin;

		origin.height = recalcHeightOfNode(origin);
		newOrigin.height = recalcHeightOfNode(newOrigin);
		return newOrigin;
	}

	/**
	 * Rotates the subtree with the passed root to the right.
	 * <p>
	 * Assumes Node passed is the root of the tree being
	 * rotated.
	 * 
	 * @param root the root of the subtree to be rotated right
	 * @return The new root of the subtree.
	 * @author Nathan Mette
	 */
	private Node<T> rotateRight(Node<T> root) {
		if (root.left == null) {
			return null; // I shouldn't reach this
		}

		root.left.height = recalcHeightOfNode(root.left);
		root.left.weight = recalcWeightOfNode(root.left);
		if (root.left.weight == 1) {
			root.left = rotateLeft(root.left); // Only in the event that the left needs to be rotated to keep balance
												// too
		}

		Node<T> origin = root;
		Node<T> newOrigin = root.left;

		origin.left = newOrigin.right;
		newOrigin.right = origin;

		origin.height = recalcHeightOfNode(origin);
		newOrigin.height = recalcHeightOfNode(newOrigin);
		return newOrigin;
	}

	/**
	 * Searches for a specific value within the tree
	 * 
	 * @param value the value to be searched for
	 * @return the value that the user want
	 * @author Jeziel Banos Gonzalez
	 */
	public T search(T value) {
		return searchHelper(root, value);
	}

	/**
	 * This is the main search for the tree
	 * 
	 * @param root  the root
	 * @param value the value to search for
	 * @return the value if found, null if not
	 * @author Jeziel Banos Gonzalez
	 */
	private T searchHelper(Node<T> root, T value) {
		Node<T> tempRoot = root;

		if (this.root == null) {
			return null;
		}

		while (tempRoot != null) {
			if (compare(value, tempRoot.value) == 0) {
				return tempRoot.value;
			} else if (compare(value, tempRoot.value) < 0) {
				tempRoot = tempRoot.left;
			} else {
				tempRoot = tempRoot.right;
			}
		}

		return null;
	}

	/**
	 * Finds the minimum of the tree
	 * 
	 * @return the minimum value
	 * @author Tony Zhang
	 */
	public T getMin() {
		Node<T> tempNode = root;
		if (tempNode == null) {
			return null;
		}

		while (tempNode.left != null) {
			tempNode = tempNode.left;
		}
		return tempNode.value;
	}

	/**
	 * Returns the max of the tree
	 * 
	 * @return the maximum value
	 * @author Tony Zhang
	 */
	public T getMax() {
		Node<T> tempNode = root;
		if (tempNode == null) {
			return null;
		}

		while (tempNode.right != null) {
			tempNode = tempNode.right;
		}
		return tempNode.value;
	}

	/**
	 * The public function users have access to, to delete from the tree.
	 * 
	 * @param value of type T
	 * @author Jeziel Banos Gonzalez
	 */
	public void deleteValue(T value) {
		/*
		 * The public method that gets called when user wants to delete a value. A value
		 * is passed and then gets passed to the delete helper. no returns, just deletes
		 */
		this.root = deleteHelper(root, value);
	}

	/**
	 * Recursive function that deletes from the tree and calls rotate as it recurses
	 * up
	 * 
	 * @param root  Node
	 * @param value of type T that matches what type the Node holds
	 * @return a Node, which is an updated "root"
	 * @author Jeziel Banos Gonzalez
	 */
	private Node<T> deleteHelper(Node<T> root, T value) {
		/*
		 * Function takes in a node and a value, will then look for the value if the
		 * value exists then the node holding the value will be removed or will have its
		 * quantity field updated. Recursive
		 */
		// value not found, recurse up
		if (root == null) {
			return root;
		}

		// searching for the value
		if (compare(value, root.value) < 0) {
			root.left = deleteHelper(root.left, value);
		} else if (compare(value, root.value) > 0) {
			root.right = deleteHelper(root.right, value);
		}

		// Case 1: the quantity is greater than 1 (dupes)
		else if (root.quantity > 1) {
			root.quantity--;

			// the line below is just for testing the delete on dup vals
			// System.out.print("Quantity of a node has been reduced\n");
		}

		// Case 2: The node is a leaf
		else if (root.left == null && root.right == null) {
			root = null;
			size--;

		}

		// Case 3: The node only has 1 child
		else if (root.right == null && root.left != null) {
			root = root.left;
			size--;
		} else if (root.left == null && root.right != null) {
			root = root.right;
			size--;
		}

		// Case 4: The node has both children
		else {

			root.value = findNodeToReplace(root.right).value;
			root.right = deleteHelper(root.right, root.value);
		}

		// checking for root being null, else null can get passed to recalc functions
		// causing a crash
		if (root == null) {
			return root;
		}

		// need to recalc these field for every node on our way up
		root.height = recalcHeightOfNode(root);
		root.weight = recalcWeightOfNode(root);

		// checking if rotate needs to happen
		if (root.weight >= 2) {
			root = rotateLeft(root);
		} else if (root.weight <= -2) {
			root = rotateRight(root);
		}

		// finally return the new "root"
		return root;
	}

	/**
	 * Finds the node to be replaced. Helper for delete
	 * 
	 * @param root the node to be searched at
	 * @return value of the node
	 * @author Jeziel Banos Gonzalez
	 */
	private Node<T> findNodeToReplace(Node<T> root) {
		Node<T> minValueNode = root;
		while (root.left != null) {
			minValueNode = root.left;
			root = root.left;
		}
		return minValueNode;
	}

	/**
	 * Compares the values returns -1 if less, 0 if equals and 1 if greater
	 * 
	 * @param insertValue the value to check
	 * @param nodeValue   the value at location
	 * @return -1, 0, or 1 if less, equals, or greater
	 * @author Tony Zhang
	 */
	private int compare(T insertValue, T nodeValue) {
		return insertValue.compareTo(nodeValue);
	}

	/**
	 * Gets the size of the tree (# of nodes)
	 * 
	 * @return the size of the tree
	 * @author Tony Zhang
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Gets the left value of the root
	 * 
	 * @return the left value of the root
	 * @author Tony Zhang
	 */
	public T getLeft() {
		return root.left.value;
	}

	/**
	 * Gets the right value of the root
	 * 
	 * @return the right value of the root
	 * @author Tony Zhang
	 */
	public T getRight() {
		return root.right.value;
	}

	/**
	 * Recalculates height of the node
	 * 
	 * @param curr the current node that is is given
	 * @return the current node's heights
	 * @author Nathan Mette
	 */
	private int recalcHeightOfNode(Node<T> curr) {
		int rightHeight = (curr.right != null) ? curr.right.height : -1;
		int leftHeight = (curr.left != null) ? curr.left.height : -1;
		return (rightHeight > leftHeight) ? rightHeight + 1 : leftHeight + 1;
	}

	/**
	 * Calculates the weights of the node given
	 * 
	 * @param curr the node that is given to be calculated
	 * @return Weight of the node.
	 * @author Nathan Mette
	 */
	private int recalcWeightOfNode(Node<T> curr) {
		curr.weight = 0;
		if (curr.right != null) {
			curr.weight += (curr.right.height + 1);
		}
		if (curr.left != null) {
			curr.weight -= (curr.left.height + 1);
		}
		return curr.weight;
	}

	/**
	 * Prints the tree in breath first search order
	 * 
	 * @author Tony Zhang
	 */
	public void printTree() {
		// linked list that keeps track of the order of visited nodes
		Queue<String> queue = new LinkedList<>();
		// linked list that keeps track of what nodes need to be visited
		Queue<Node<T>> treeTraversalQueue = new LinkedList<>();
		// need to check if the tree is empty
		if (root != null) {
			treeTraversalQueue.add(root);
		}

		// traversal of the tree
		while (!treeTraversalQueue.isEmpty()) {
			// updating the queues
			Node<T> curNode = treeTraversalQueue.remove();
			queue.add(curNode.value.toString() + " ");
			// left and right get added if they are not null
			if (curNode.left != null) {
				treeTraversalQueue.add(curNode.left);
			}
			if (curNode.right != null) {
				treeTraversalQueue.add(curNode.right);
			}
		}
		// actual print
		while (!queue.isEmpty()) {
			System.out.print(queue.remove());
		}
	}

	// FOR TESTING PURPOSES ONLY
	public int getLeftHeight() {
		if (root.left == null) {
			return 0;
		}
		return root.left.height;
	}

	// FOR TESTING PURPOSES ONLY
	public int getRightHeight() {
		if (root.right == null) {
			return 0;
		}
		return root.right.height;
	}

	/**
	 * Checks to see if tree is empty
	 * 
	 * @return a boolean to see if tree is empty of not
	 */
	public boolean isEmpty() {
		return size == 0;
	}

}

class Node<T> {
	public T value;
	public Node<T> right;
	public Node<T> left;
	public Node<T> parent;
	public int weight;
	public int height;
	public int quantity;
	
	public Node(T item){
		this.value = item;
		this.right = null;
		this.left = null;
		this.parent = null;
		this.height = 0;
		this.quantity = 1;
	}

	
}