import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

class AVLTreeTest {
	
	
	/**
	 * Creates a random AVLTree of size ~100 and confirms the height of each subtree
	 * to the left and right of the root is roughly equal.
	 */
	@Test
	void heightTest() {
		Random rand = new Random();
		HashSet<Integer> numbers = new HashSet<>();
		AVLTree<Integer> currTree = new AVLTree<>();
		for (int i = 0; i < 100; i++) {
			int randomNum = rand.nextInt(-1000, 1000);
			if (numbers.add(randomNum)) {
				currTree.insert(randomNum);
			}
		}
		
		int heightDiff = currTree.getRightHeight() - currTree.getLeftHeight();
		currTree.printTree();
		assertTrue(heightDiff > -2);
		assertTrue(heightDiff < 2);
		assertTrue(currTree.getSize() == numbers.size());
	}
	
	/**
	 * Generates a random AVLTree of size ~50 and attempts to search for each value within the tree
	 * to confirm that it can find each value after being built.
	 */
	@Test
	void searchTest() {
		Random rand = new Random();
		HashSet<Integer> numbers = new HashSet<>();
		AVLTree<Integer> currTree = new AVLTree<>();
		for (int i = 0; i < 50; i++) {
			int toAdd = rand.nextInt(0, 100);
			if (numbers.add(toAdd)) {
				currTree.insert(toAdd);
			}
		}
		
		assertTrue(currTree.getSize() == numbers.size());
		for (int n : numbers) {
			assertTrue(currTree.search(n) == n);
		}
	}
	
	/**
	 * Tests to see if the the delete method in the AVLTree is able to delete each value after being built
	 * without compromising the total organization and balance of the tree.
	 */
	@Test
	void deleteTest() {
		Random rand = new Random();
		HashSet<Integer> numbers = new HashSet<>();
		AVLTree<Integer> currTree = new AVLTree<>();
		for (int i = 0; i < 50; i++) {
			int toAdd = rand.nextInt(-100, 100);
			if (numbers.add(toAdd)) {
				currTree.insert(toAdd);
			}
		}
		
		int size = currTree.getSize();
		for (int n : numbers) {
			currTree.printTree();
			System.out.println("\nCurrent size is " + currTree.getSize() + "\nActual should be " + size);
			currTree.deleteValue(n);
			size--;
			//numbers.remove(n);
			assertTrue(currTree.search(n) == null);
			if (!currTree.isEmpty()) {
				int heightDiff = currTree.getLeftHeight() - currTree.getRightHeight();
				assertTrue(heightDiff > -2);
				assertTrue(heightDiff < 2);
			}
			assertTrue(currTree.getSize() == size);
		}
	}
	
	/**
	 * Creates an AVLTree of size 100, containing the values 0-99.
	 * Checks to see if the values of the min and the max are stored correctly.
	 */
	@Test
	void checkMinAndMax() {
		HashSet<Integer> numbers = new HashSet<>();
		AVLTree<Integer> currTree = new AVLTree<>();
		for (int i = 0; i < 100; i++) {
			if (numbers.add(i)) {
				currTree.insert(i);
			}
		}
		
		assertTrue(currTree.getMin() == 0);
		assertTrue(currTree.getMax() == 99);
		assertTrue(currTree.getSize() == numbers.size());
	}
	
	/**
	 * Creates and AVLTree with and ordered list and assures that the balance of the tree is kept
	 * and that the size is correct.
	 */
	@Test
	void checkOrderedList() {
		AVLTree<Integer> currTree = new AVLTree<>();
		for (int i = 0; i < 100; i++) {
			currTree.insert(i);
		}
		
		int heightDiff = currTree.getLeftHeight() - currTree.getRightHeight();
		assertTrue(heightDiff > -2);
		assertTrue(heightDiff < 2);
		assertTrue(currTree.getSize() == 100);
	}
	
	/**
	 * Creates an empty tree and assures that the methods getMax, getMin, and getSize return the
	 * corresponding values for an empty tree.
	 */
	@Test
	void checkEmptyTree() {
		AVLTree<Integer> currTree = new AVLTree<>();
		
		assertTrue(currTree.getMax() == null);
		assertTrue(currTree.getMin() == null);
		assertTrue(currTree.getSize() == 0);
	}

}
