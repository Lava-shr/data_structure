import java.util.HashMap;
import java.util.*;
import textbook.LinkedBinaryTree;
import textbook.LinkedQueue;
import textbook.Position;

public class Assignment {

	/**
	 * Convert an arithmetic expression (in prefix notation), to a binary tree
	 * 
	 * Binary operators are +, -, * (i.e. addition, subtraction, multiplication)
	 * Anything else is assumed to be a variable or numeric value
	 * 
	 * Example: "+ 2 15" will be a tree with root "+", left child "2" and right
	 * child "15" i.e. + 2 15
	 * 
	 * Example: "+ 2 - 4 5" will be a tree with root "+", left child "2", right
	 * child a subtree representing "- 4 5" i.e. + 2 - 4 5
	 * 
	 * This method runs in O(n) time
	 * 
	 * @param expression
	 *            - an arithmetic expression in prefix notation
	 * @return BinaryTree representing an expression expressed in prefix
	 *         notation
	 * @throws IllegalArgumentException
	 *             if expression was not a valid expression
	 */
	public static LinkedBinaryTree<String> prefix2tree(String expression) throws IllegalArgumentException {
		if (expression == null) {
			throw new IllegalArgumentException("Expression string was null");
		}
		// break up the expression string using spaces, into a queue
		LinkedQueue<String> tokens = new LinkedQueue<String>();
		for (String token : expression.split(" ")) {
			tokens.enqueue(token);
		}
		// recursively build the tree
		return prefix2tree(tokens);
	}

	/**
	 * Recursive helper method to build an tree representing an arithmetic
	 * expression in prefix notation, where the expression has already been
	 * broken up into a queue of tokens
	 * 
	 * @param tokens
	 * @return
	 * @throws IllegalArgumentException
	 *             if expression was not a valid expression
	 */
	private static LinkedBinaryTree<String> prefix2tree(LinkedQueue<String> tokens) throws IllegalArgumentException {
		LinkedBinaryTree<String> tree = new LinkedBinaryTree<String>();

		// use the next element of the queue to build the root
		if (tokens.isEmpty()) {
			throw new IllegalArgumentException("String was not a valid arithmetic expression in prefix notation");
		}
		String element = tokens.dequeue();
		tree.addRoot(element);

		// if the element is a binary operation, we need to build the left and
		// right subtrees
		if (element.equals("+") || element.equals("-") || element.equals("*")) {
			LinkedBinaryTree<String> left = prefix2tree(tokens);
			LinkedBinaryTree<String> right = prefix2tree(tokens);
			tree.attach(tree.root(), left, right);
		}
		// otherwise, assume it's a variable or a value, so it's a leaf (i.e.
		// nothing more to do)

		return tree;
	}

	/**
	 * Test to see if two trees are identical (every position in the tree stores
	 * the same value)
	 * 
	 * e.g. two trees representing "+ 1 2" are identical to each other, but not
	 * to a tree representing "+ 2 1"
	 * 
	 * @param a
	 * @param b
	 * @return true if the trees have the same structure and values, false
	 *         otherwise
	 */
	public static boolean equals(LinkedBinaryTree<String> a, LinkedBinaryTree<String> b) {
		return equals(a, b, a.root(), b.root());
	}

	/**
	 * Recursive helper method to compare two trees
	 * 
	 * @param aTree
	 *            one of the trees to compare
	 * @param bTree
	 *            the other tree to compare
	 * @param aRoot
	 *            a position in the first tree
	 * @param bRoot
	 *            a position in the second tree (corresponding to a position in
	 *            the first)
	 * @return true if the subtrees rooted at the given positions are identical
	 */
	private static boolean equals(LinkedBinaryTree<String> aTree, LinkedBinaryTree<String> bTree,
			Position<String> aRoot, Position<String> bRoot) {
		// if either of the positions is null, then they are the same only if
		// they are both null
		if (aRoot == null || bRoot == null) {
			return (aRoot == null) && (bRoot == null);
		}
		// first check that the elements stored in the current positions are the
		// same
		String a = aRoot.getElement();
		String b = bRoot.getElement();
		if ((a == null && b == null) || a.equals(b)) {
			// then recursively check if the left subtrees are the same...
			boolean left = equals(aTree, bTree, aTree.left(aRoot), bTree.left(bRoot));
			// ...and if the right subtrees are the same
			boolean right = equals(aTree, bTree, aTree.right(aRoot), bTree.right(bRoot));
			// return true if they both matched
			return left && right;
		} else {
			return false;
		}
	}

	/**
	 * Given a tree, this method should output a string for the corresponding
	 * arithmetic expression in prefix notation, without (parenthesis) (also
	 * known as Polish notation)
	 * 
	 * Example: A tree with root "+", left child "2" and right child "15" would
	 * be "+ 2 15" Example: A tree with root "-", left child a subtree
	 * representing "(2+15)" and right child "4" would be "- + 2 15 4"
	 * 
	 * Ideally, this method should run in O(n) time
	 * 
	 * @param tree
	 *            - a tree representing an arithmetic expression
	 * @return prefix notation expression of the tree
	 * @throws IllegalArgumentException
	 *             if tree was not a valid expression
	 */
	public static String tree2prefix(LinkedBinaryTree<String> tree) throws IllegalArgumentException {

		try {
			if (tree.isEmpty()) {
				throw new IllegalArgumentException("Tree was not a valid arithmetic expression");
			}

			boolean ifValidExpression = isArithmeticExpression(tree);
			if (!ifValidExpression || tree.isEmpty()) {
				throw new IllegalArgumentException("Tree was not a valid arithmetic expression");
			}
			String toPrefix = "";
			LinkedQueue<String> queue = new LinkedQueue<>();
			Position<String> root = tree.root();

			/*
			 * Getting all the values of tree in queue data structure so that we
			 * just need to dequeue it and add to the toPrefix string which will
			 * result in prefix notation for the given tree.
			 */
			queue = tree2prefixHelper(tree, root, queue);

			while (!queue.isEmpty()) {
				if (queue.size() == 1) {
					toPrefix += queue.dequeue();
				} else {
					toPrefix = toPrefix + queue.dequeue() + " ";
				}
			}
			return toPrefix;
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("Tree was not a valid arithmetic expression");
		}

	}
	/*
	 * Recursive method to get all the values of LinkedBinaryTree<String> tree
	 * to LinkedQueue<String> queue Pre-order traversal is done here
	 * 
	 * @param tree - tree on which we doing recursion
	 * 
	 * @param root - root position of the tree
	 * 
	 * @param queue- add the values to queue return the LinkedQueue<String>
	 * queue containing all the values of the tree.
	 * 
	 */

	private static LinkedQueue<String> tree2prefixHelper(LinkedBinaryTree<String> tree, Position<String> root,
			LinkedQueue<String> queue) {

		if (root.getElement() == null) {
			return null;
		}
		queue.enqueue(root.getElement());

		if (tree.left(root) != null) {
			tree2prefixHelper(tree, tree.left(root), queue);
		}
		if (tree.right(root) != null) {
			tree2prefixHelper(tree, tree.right(root), queue);
		}
		return queue;
	}

	/**
	 * Given a tree, this method should output a string for the corresponding
	 * arithmetic expression in infix notation with parenthesis (i.e. the most
	 * commonly used notation).
	 * 
	 * Example: A tree with root "+", left child "2" and right child "15" would
	 * be "(2+15)"
	 * 
	 * Example: A tree with root "-", left child a subtree representing "(2+15)"
	 * and right child "4" would be "((2+15)-4)"
	 * 
	 * Optionally, you may leave out the outermost parenthesis, but it's fine to
	 * leave them on. (i.e. "2+15" and "(2+15)-4" would also be acceptable
	 * output for the examples above)
	 * 
	 * Ideally, this method should run in O(n) time
	 * 
	 * @param tree
	 *            - a tree representing an arithmetic expression
	 * @return infix notation expression of the tree
	 * @throws IllegalArgumentException
	 *             if tree was not a valid expression
	 */
	public static String tree2infix(LinkedBinaryTree<String> tree) throws IllegalArgumentException {
		try {
			if (tree.isEmpty()) {
				throw new IllegalArgumentException("Tree was not a valid arithmetic expression");
			}
			boolean ifValidExpression = isArithmeticExpression(tree);
			if (!ifValidExpression || tree.isEmpty()) {
				throw new IllegalArgumentException("Tree was not a valid arithmetic expression");
			}

			LinkedQueue<String> queue = new LinkedQueue<>();
			// Getting all the values of tree in queue data structure
			queue = tree2prefixHelper(tree, tree.root(), queue);
			Stack<String> infixStack = new Stack<>();
			String infix = "";
			// Adding that queue values to stack
			// Changing Stack values to infix will be easy as we just have to
			// pop and add values.
			while (!queue.isEmpty()) {
				infixStack.push(queue.dequeue());
			}

			infix = tree2infixHelper(infixStack);
			return infix;
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("Tree was not a valid arithmetic expression");
		}
	}

	/*
	 * Given a stack containing the all the values of tree it returns the infix
	 * return Just pop the stack values and add it with some check condition
	 * 
	 * @param infix values in the Stack Data Structure
	 * 
	 * @return infix String
	 */
	private static String tree2infixHelper(Stack<String> infixStack) {
		Stack<String> newStack = new Stack<>();
		String infix = "";
		while (!infixStack.isEmpty()) {
			String temp = infixStack.pop();
			// if infixStack pop the operation values we just pop 3 values from
			// new stack and add it to new stack
			// if infixStack pop operands we just add it to new Stack
			if (temp.equals("*") || temp.equals("+") || temp.equals("-")) {
				String temp1 = newStack.pop();
				String temp2 = newStack.pop();
				String temp3 = "(" + temp1 + temp + temp2 + ")";
				newStack.push(temp3);
			} else {
				newStack.push(temp);
			}
		}
		// NewStack.peek or pop will have final infix value for the given stack
		infix = newStack.peek();
		return infix;
	}

	/**
	 * Given a tree, this method should simplify any subtrees which can be
	 * evaluated to a single integer value.
	 * 
	 * Ideally, this method should run in O(n) time
	 * 
	 * @param tree
	 *            - a tree representing an arithmetic expression
	 * @return resulting binary tree after evaluating as many of the subtrees as
	 *         possible
	 * @throws IllegalArgumentException
	 *             if tree was not a valid expression
	 */
	public static LinkedBinaryTree<String> simplify(LinkedBinaryTree<String> tree) throws IllegalArgumentException {
		try {
			if (tree.isEmpty()) {
				throw new IllegalArgumentException("Tree was not a valid arithmetic expression");
			}
			boolean ifTreeValid = isArithmeticExpression(tree);
			if (!ifTreeValid) {
				throw new IllegalArgumentException("Tree wasn't valid");
			}
			simplifyHelper(tree, tree.root());

			return tree;
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("Tree was not a valid arithmetic expression");
		}
	}

	/*
	 * Given the tree it will simplify the tree Uses the post order traversal
	 * 
	 * @param tree - the tree that we need to simplify
	 * 
	 * @param root - the root position of the given tree
	 * 
	 * @return the simplified tree value
	 * 
	 */
	private static LinkedBinaryTree<String> simplifyHelper(LinkedBinaryTree<String> tree, Position<String> root) {

		if (tree.left(root) != null || tree.right(root) != null) {
			simplifyHelper(tree, tree.left(root));
			simplifyHelper(tree, tree.right(root));

			// Using helper method to match if the String values contains Integers
			// if matched do the necessary operations
			if (intParseCheck(tree.left(root).getElement())
					&& intParseCheck(tree.right(root).getElement())) {
				
				String operation = tree.parent(tree.left(root)).getElement();
				int value = 0;
				if (operation.equals("+")) {
					value = Integer.parseInt(tree.left(root).getElement())
							+ Integer.parseInt(tree.right(root).getElement());
				} else if (operation.equals("-")) {
					value = Integer.parseInt(tree.left(root).getElement())
							- Integer.parseInt(tree.right(root).getElement());
				} else if (operation.equals("*")) {
					value = Integer.parseInt(tree.left(root).getElement())
							* Integer.parseInt(tree.right(root).getElement());
				}
				String final_result = String.valueOf(value);
				// Setting the new simplified value and deleting its left and right child
				tree.set(tree.parent(tree.left(root)), final_result);
				tree.remove(tree.left(root));
				tree.remove(tree.right(root));
			}
		}
		return tree;
	}

	/**
	 * This should do everything the simplify method does AND also apply the
	 * following rules: * 1 x == x i.e. (1*x)==x * x 1 == x (x*1)==x * 0 x == 0
	 * (0*x)==0 * x 0 == 0 (x*0)==0 + 0 x == x (0+x)==x + x 0 == 0 (x+0)==x - x
	 * 0 == x (x-0)==x - x x == 0 (x-x)==0
	 * 
	 * Example: - * 1 x x == 0, in infix notation: ((1*x)-x) = (x-x) = 0
	 * 
	 * Ideally, this method should run in O(n) time (harder to achieve than for
	 * other methods!)
	 * s
	 * @param tree
	 *            - a tree representing an arithmetic expression
	 * @return resulting binary tree after applying the simplifications
	 * @throws IllegalArgumentException
	 *             if tree was not a valid expression
	 */
	public static LinkedBinaryTree<String> simplifyFancy(LinkedBinaryTree<String> tree)
			throws IllegalArgumentException {
		try {
			if (tree.isEmpty()) {
				throw new IllegalArgumentException("Tree was not a valid arithmetic expression");
			}
			boolean ifTreeValid = isArithmeticExpression(tree);
			if (!ifTreeValid) {
				throw new IllegalArgumentException("Tree wasn't valid");
			}
			simplifyFancyHelper(tree, tree.root());
			LinkedBinaryTree<String> modified_tree = new LinkedBinaryTree<>();
			
			// Checking for some more simplification which simplifyFancyHelper doesn't support
			if (tree.root().getElement().equals("*")) {

				// We need to simplify tree to 0 as anything times 0 is 0
				if (tree.left(tree.root()).getElement().equals("0")) {
					modified_tree.addRoot("0");
					return modified_tree;
				}
				// We need to simplify tree to 0 as anything times 0 is 0
				if (tree.left(tree.root()).getElement().equals("0")) {
					modified_tree.addRoot("0");
					return modified_tree;
				}
				// We can get rid of left sub-tree
				// We use the helper method to copy the left sub-tree to new tree
				if (tree.left(tree.root()).getElement().equals("1")) {
					modified_tree.addRoot(tree.right(tree.root()).getElement());
					simpliyFancyCopySubTree(tree, tree.right(tree.root()), modified_tree, modified_tree.root());
					return modified_tree;
				}
				// We can get rid of right subtree
				// We use the helper method to copy the right sub-tree
				if (tree.right(tree.root()).getElement().equals("1")) {
					modified_tree.addRoot(tree.left(tree.root()).getElement());
					simpliyFancyCopySubTree(tree, tree.left(tree.root()), modified_tree, modified_tree.root());
					return modified_tree;
				}
			}
			
			return tree;
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("Tree was not a valid arithmetic expression");
		}
	}

	/*
	 * Given the tree it will simplify fancy the tree Uses the post order
	 * traversal
	 * 
	 * @param tree - the tree that we need to simplify
	 * 
	 * @param root - the root position of the given tree
	 * 
	 * @return the simplified fancy tree
	 * 
	 */
	private static void simplifyFancyHelper(LinkedBinaryTree<String> tree, Position<String> root) {

		if (tree.left(root) == null || tree.right(root) == null)
			return;

		
		simplifyFancyHelper(tree, tree.left(root));
		simplifyFancyHelper(tree, tree.right(root));
		// Using helper method to match if the String values contains Integers
		// if matched do the necessary operations
		// Once simplified we set the new value for parent and delete it child
		if (intParseCheck(tree.left(root).getElement()) && intParseCheck(tree.right(root).getElement())) {

			String operation = tree.parent(tree.left(root)).getElement();
			int value = 0;
			if (operation.equals("+")) {
				value = Integer.parseInt(tree.left(root).getElement())
						+ Integer.parseInt(tree.right(root).getElement());
			} else if (operation.equals("-")) {
				value = Integer.parseInt(tree.left(root).getElement())
						- Integer.parseInt(tree.right(root).getElement());
			} else if (operation.equals("*")) {
				value = Integer.parseInt(tree.left(root).getElement())
						* Integer.parseInt(tree.right(root).getElement());
			}
			String final_result = String.valueOf(value);
			tree.set(tree.parent(tree.left(root)), final_result);
			tree.remove(tree.left(root));
			tree.remove(tree.right(root));
		} else {
			// Doing some fancy simplification
			// Once simplified we set the new value for parent and delete it
			// child
			String final_result = "";
			String operation = tree.parent(tree.left(root)).getElement();

			if ( stringCheck(tree.left(root).getElement())
					&& intParseCheck(tree.right(root).getElement())){
				if (operation.equals("*") && tree.right(root).getElement().equals("0")) {
					final_result = final_result + "0";
					tree.set(tree.parent(tree.left(root)), final_result);
					tree.remove(tree.left(root));
					tree.remove(tree.right(root));
				} else if (operation.equals("*") && tree.right(root).getElement().equals("1")){
					final_result = final_result + tree.left(root).getElement();
					tree.set(tree.parent(tree.left(root)), final_result);
					tree.remove(tree.left(root));
					tree.remove(tree.right(root));
				} else if (operation.equals("*") && tree.right(root).getElement().equals("-1")){
					final_result = final_result +"-" +tree.left(root).getElement();
					tree.set(tree.parent(tree.left(root)), final_result);
					tree.remove(tree.left(root));
					tree.remove(tree.right(root));
				}else if (operation.equals("*")) {
					final_result = final_result + tree.right(root).getElement() + tree.left(root).getElement();
					tree.set(tree.parent(tree.left(root)), final_result);
					tree.remove(tree.left(root));
					tree.remove(tree.right(root));
				} else if (operation.equals("+") && tree.right(root).getElement().equals("0")) {
					final_result = final_result + tree.left(root).getElement();
					tree.set(tree.parent(tree.left(root)), final_result);
					tree.remove(tree.left(root));
					tree.remove(tree.right(root));
				} else if (operation.equals("-") && tree.right(root).getElement().equals("0")) {
					final_result = final_result + tree.left(root).getElement();
					tree.set(tree.parent(tree.left(root)), final_result);
					tree.remove(tree.left(root));
					tree.remove(tree.right(root));
				}
			} else if (stringCheck(tree.right(root).getElement())
					&& intParseCheck(tree.left(root).getElement())){
				if (operation.equals("*") && tree.left(root).getElement().equals("0")) {
					final_result = final_result + "0";
					tree.set(tree.parent(tree.left(root)), final_result);
					tree.remove(tree.left(root));
					tree.remove(tree.right(root));
				} else if (operation.equals("*") && tree.left(root).getElement().equals("1")){
					final_result = final_result + tree.right(root).getElement();
					tree.set(tree.parent(tree.left(root)), final_result);
					tree.remove(tree.left(root));
					tree.remove(tree.right(root));
				} else if(operation.equals("*") && tree.left(root).getElement().equals("-1")){
					final_result = final_result +"-" +tree.right(root).getElement();
					tree.set(tree.parent(tree.left(root)), final_result);
					tree.remove(tree.left(root));
					tree.remove(tree.right(root));
				}else if (operation.equals("*")) {
					final_result = final_result + tree.left(root).getElement() + tree.right(root).getElement();
					tree.set(tree.parent(tree.left(root)), final_result);
					tree.remove(tree.left(root));
					tree.remove(tree.right(root));
				} else if (operation.equals("+") && tree.left(root).getElement().equals("0")) {
					final_result = final_result + tree.right(root).getElement();
					tree.set(tree.parent(tree.left(root)), final_result);
					tree.remove(tree.left(root));
					tree.remove(tree.right(root));
				} else if (operation.equals("-") && tree.left(root).getElement().equals("0")){
					final_result = final_result +"-" +tree.right(root).getElement();
					tree.set(tree.parent(tree.left(root)), final_result);
					tree.remove(tree.left(root));
					tree.remove(tree.right(root));
				}

			} else if (stringCheck(tree.left(root).getElement())
					&& stringCheck(tree.right(root).getElement())){
				if (operation.equals("-")) {
					if (tree.left(root).getElement().equals(tree.right(root).getElement())) {
						final_result = final_result + "0";
					}
					tree.set(tree.parent(tree.left(root)), final_result);
					tree.remove(tree.left(root));
					tree.remove(tree.right(root));
				}
			}
		}

	}
	
	/* Helper method for simplify fancy function to to delete the left subtree of the main tree
	 * @param tree - tree that we want to delete the part of 
	 * @param root - root of the tree
	 * @param modified_tree - tree in which we will store the new values
	 * 
	*/
	
	private static void  simpliyFancyCopySubTree(LinkedBinaryTree<String> tree,Position<String> root , LinkedBinaryTree<String> modified_tree , Position<String> root2){
		
		if (tree.left(root) == null || tree.right(root) == null)
			return;

		modified_tree.addLeft(root2 , tree.left(root).getElement());
		modified_tree.addRight(root2 , tree.right(root).getElement());
		
		simpliyFancyCopySubTree(tree, tree.left(root) , modified_tree , modified_tree.left(root2));
		simpliyFancyCopySubTree(tree, tree.right(root) , modified_tree , modified_tree.right(root2));
	}
	
	
	/* Helper method to check if the string given can be parsed to Integer
	 * @param toCheck if the string can be parsed to Integer
	 * 		  that means string has integer values stored in it.
	 * @return - true if the String can be parsed to Integer
	 *           false otherwis
	 */
	
	private static boolean intParseCheck(String toCheck){
		try{
			int temp = Integer.parseInt(toCheck);
		}catch(NumberFormatException e){
			return false;
		}
		return true;
	}
	/*
	 * Helper function to check if the given string is a string 
	 * i.e not null, not +,-,* and not Integers valued string 
	 * 
	 * @param toCheck - String to Check
	 * return true if satisfy the condition false otherwise
	 */
	private static boolean stringCheck(String toCheck){
		
		if(toCheck == null) return false;
		
		if(toCheck.equals("*") || toCheck.equals("+") || toCheck.equals("-")){
			return false;
		}
		try{
			int temp = Integer.parseInt(toCheck);
			return false;
		}catch(NumberFormatException e){}
		return true;
	}
	/**
	 * Given a tree, a variable label and a value, this should replace all
	 * instances of that variable in the tree with the given value
	 * 
	 * Ideally, this method should run in O(n) time (quite hard! O(n^2) is
	 * easier.)
	 * 
	 * @param tree
	 *            - a tree representing an arithmetic expression
	 * @param variable
	 *            - a variable label that might exist in the tree
	 * @param value
	 *            - an integer value that the variable represents
	 * @return Tree after replacing all instances of the specified variable with
	 *         it's numeric value
	 * @throws IllegalArgumentException
	 *             if tree was not a valid expression, or either of the other
	 *             arguments are null
	 */
	public static LinkedBinaryTree<String> substitute(LinkedBinaryTree<String> tree, String variable, int value)
			throws IllegalArgumentException {
		try {
			if (tree.isEmpty() || variable == null) {
				throw new IllegalArgumentException("Tree was not a valid arithmetic expression");
			}
			// Nothing to change
			if(variable.equals("+") || variable.equals("-") || variable.equals("*")){
				return tree;
			}
			boolean ifTreeValid = isArithmeticExpression(tree);
			if (!ifTreeValid) {
				throw new IllegalArgumentException("Tree wasn't valid");
			}
			substituteHelper(tree, tree.root(), variable, value);
			return tree;
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("Tree was not a valid arithmetic expression");
		}
	}

	/*
	 * Given a tree, a variable label and a value, this should replace all
	 * instances of that variable in the tree with the given value Uses the Post
	 * order traversal
	 * 
	 * @param tree - a tree representing an arithmetic expression
	 * 
	 * @param root - root position of the given tree
	 * 
	 * @param variable - a variable label that might exist in the tree
	 * 
	 * @param value - an integer value that the variable represents
	 * 
	 * @return Tree after replacing all instances of the specified variable with
	 * it's numeric value
	 * 
	 */
	private static void substituteHelper(LinkedBinaryTree<String> tree, Position<String> root, String variable,
			int value) {

		if (tree.left(root) == null || tree.right(root) == null)
			return;
		substituteHelper(tree, tree.left(root), variable, value);
		substituteHelper(tree, tree.right(root), variable, value);

		// checking it the variable matches the values in the tree
		if (tree.left(root).getElement().equals(variable)) {
			tree.set(tree.left(root), String.valueOf(value));
		}
		if (tree.right(root).getElement().equals(variable)) {
			tree.set(tree.right(root), String.valueOf(value));
		}
	}

	/**
	 * Given a tree and a a map of variable labels to values, this should
	 * replace all instances of those variables in the tree with the
	 * corresponding given values
	 * 
	 * Ideally, this method should run in O(n) expected time
	 * 
	 * @param tree
	 *            - a tree representing an arithmetic expression
	 * @param map
	 *            - a map of variable labels to integer values
	 * @return Tree after replacing all instances of variables which are keys in
	 *         the map, with their numeric values
	 * @throws IllegalArgumentException
	 *             if tree was not a valid expression, or map is null, or tries
	 *             to substitute a null into the tree
	 */
	public static LinkedBinaryTree<String> substitute(LinkedBinaryTree<String> tree, HashMap<String, Integer> map)
			throws IllegalArgumentException {

		try {
			if (tree.isEmpty()) {
				throw new IllegalArgumentException("Tree was not a valid arithmetic expression");
			}
			boolean ifTreeValid = isArithmeticExpressionHelper(tree, tree.root());
			if (!ifTreeValid) {
				throw new IllegalArgumentException("Tree wasn't valid");
			}
			if(map.isEmpty()) {
				return tree;
			}

			/* Getting the Tree in prefix notation and getting rid of the operation
			* And Storing those values to sets to check for the null values stored in Hashmap
			*/
			String prefix = Assignment.tree2prefix(tree);
			Set<String> operands = new HashSet<String>();
			for (String token : prefix.split(" ")) {
				if (token.equals("+") == false && token.equals("-") == false && token.equals("*") == false) {
					operands.add(token);
				}
			}
			
			// Null value check in HashMap
			for (String operand : operands) {
				if (map.containsKey(operand)) {
					if (map.get(operand) == null) {
						throw new IllegalArgumentException("Values wasn't valid");
					}
				}
			}
			substituteHelper(tree, tree.root(), map);
			return tree;

		} catch (NullPointerException e) {
			throw new IllegalArgumentException("Was not a valid input");
		}
	}

	/*
	 * Given a tree and a a map of variable labels to values, this should
	 * replace all instances of those variables in the tree with the
	 * corresponding given values Uses the Post order traversal
	 * 
	 * @param tree - a tree representing an arithmetic expression
	 * 
	 * @param root - root position of the tree
	 * 
	 * @param map - a map of variable labels to integer values
	 * 
	 * @return Tree after replacing all instances of variables which are keys in
	 * the map, with their numeric values
	 * 
	 */
	private static void substituteHelper(LinkedBinaryTree<String> tree, Position<String> root,
			HashMap<String, Integer> map) {

		if (tree.left(root) == null || tree.right(root) == null)
			return;

		substituteHelper(tree, tree.left(root), map);
		substituteHelper(tree, tree.right(root), map);

		// Check the values of trees are in that map's key
		// if yes get the Integer values from the map and replace the values.
		if (tree.left(root).getElement().equals("+") == false && tree.left(root).getElement().equals("-") == false
				&& tree.left(root).getElement().equals("*") == false) {
			if (map.containsKey(tree.left(root).getElement())) {
				Integer value = map.get(tree.left(root).getElement());
				tree.set(tree.left(root), String.valueOf(value));
			}
		}
		if (tree.right(root).getElement().equals("+") == false && tree.right(root).getElement().equals("-") == false
				&& tree.right(root).getElement().equals("*") == false) {
			if (map.containsKey(tree.right(root).getElement())) {
				Integer value = map.get(tree.right(root).getElement());
				tree.set(tree.right(root), String.valueOf(value));
			}
		}

	}

	/**
	 * Given a tree, identify if that tree represents a valid arithmetic
	 * expression (possibly with variables)
	 * 
	 * Ideally, this method should run in O(n) expected time
	 * 
	 * @param tree
	 *            - a tree representing an arithmetic expression
	 * @return true if the tree is not null and it obeys the structure of an
	 *         arithmetic expression. Otherwise, it returns false
	 */
	public static boolean isArithmeticExpression(LinkedBinaryTree<String> tree) {
		// TODO: Implement this method
		try {
			try {
				if (tree.root() == null) {
					return false;
				}
				Position<String> root = tree.root();
				return isArithmeticExpressionHelper(tree, root);
			} catch (NullPointerException e) {
				return false;
			}
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	/*
	 * Given a tree check if represents valid arithmetic expression 
	 * Uses the pre order traversal to get to all the tree values.
	 * 
	 * @param tree - LinkedBinaryTree<String> tree that we need to check if its
	 * valid arithmetic expression
	 * 
	 * @param root - root position of the given tree
	 * @return - true if valid Arithmetic Expression tree , false otherwise
	 * 
	 */
	private static boolean isArithmeticExpressionHelper(LinkedBinaryTree<String> tree, Position<String> root) {

		if (root == null) {
			return true;
		}
		if (root.getElement() == null && (tree.parent(root).getElement().equals("+") == false
				|| tree.parent(root).getElement().equals("-") == false
				|| tree.parent(root).getElement().equals("*") == false)) {
			return false;
		}
		// if the values in trees are +,-,* then it need to have not
		// null left and right child to be valid arithmetic expression
		if (root.getElement().equals("+") || root.getElement().equals("-") || root.getElement().equals("*")) {
			if (tree.left(root) == null || tree.right(root) == null) {
				return false;
			}
			// if values are not null and are not (+ ,-,*) then it should have
			// left and roght child as null
		} else {
			if (tree.left(root) != null || tree.right(root) != null) {
				return false;
			}
		}
		return isArithmeticExpressionHelper(tree, tree.left(root))
				&& isArithmeticExpressionHelper(tree, tree.right(root));
	}

}

