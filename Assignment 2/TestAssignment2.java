import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import textbook.LinkedBinaryTree;

public class TestAssignment {
	
	// Set up JUnit to be able to check for expected exceptions
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	// Some simple testing of prefix2tree
	@Test(timeout = 100)
	public void testPrefix2tree() {
		
		LinkedBinaryTree<String> tree;

		tree = Assignment.prefix2tree("hi");
		assertEquals(1, tree.size());
		assertEquals("hi", tree.root().getElement());

		tree = Assignment.prefix2tree("+ 5 10");
		assertEquals(3, tree.size());
		assertEquals("+", tree.root().getElement());
		assertEquals("5", tree.left(tree.root()).getElement());
		assertEquals("10", tree.right(tree.root()).getElement());
		
		tree = Assignment.prefix2tree("- 5 10");
		assertEquals(3, tree.size());
		assertEquals("-", tree.root().getElement());
		assertEquals("5", tree.left(tree.root()).getElement());
		assertEquals("10", tree.right(tree.root()).getElement());
		
		tree = Assignment.prefix2tree("* 5 10");
		assertEquals(3, tree.size());
		assertEquals("*", tree.root().getElement());
		assertEquals("5", tree.left(tree.root()).getElement());
		assertEquals("10", tree.right(tree.root()).getElement());
				
		tree = Assignment.prefix2tree("+ 5 - 4 3");
		assertEquals(5, tree.size());
		assertEquals("+", tree.root().getElement());
		assertEquals("5", tree.left(tree.root()).getElement());
		assertEquals("-", tree.right(tree.root()).getElement());
		assertEquals("4", tree.left(tree.right(tree.root())).getElement());
		assertEquals("3", tree.right(tree.right(tree.root())).getElement());
		
		thrown.expect(IllegalArgumentException.class);
		tree = Assignment.prefix2tree("+ 5 - 4");

	}

	@Test(timeout = 100)
	public void testTree2prefix() {
		
		LinkedBinaryTree<String> tree =  Assignment.prefix2tree("+ 5 10");
		String prefix = Assignment.tree2prefix(tree);
		assertEquals("+ 5 10" , prefix);
		
		LinkedBinaryTree<String> tree2 = new LinkedBinaryTree<>();
		tree2.addRoot("0");
		prefix = Assignment.tree2prefix(tree2);
		assertEquals("0" , prefix);
		
		tree = Assignment.prefix2tree("- + 2 15 -4");
		prefix = Assignment.tree2prefix(tree);
		assertEquals("- + 2 15 -4", prefix);
		
		tree = Assignment.prefix2tree("+ 5 - 4 3");
		prefix = Assignment.tree2prefix(tree);
		assertEquals("+ 5 - 4 3" , prefix);
		
		//Illegal Argument check
		LinkedBinaryTree<String> tree3 = new LinkedBinaryTree<>();
		tree3.addRoot("+");
		thrown.expect(IllegalArgumentException.class);
		prefix = Assignment.tree2prefix(tree3);
		tree3.addLeft(tree3.root(), "3");
		thrown.expect(IllegalArgumentException.class);
		prefix = Assignment.tree2prefix(tree3);
		tree3.addRight(tree.root(), "+");
		thrown.expect(IllegalArgumentException.class);
		prefix = Assignment.tree2infix(tree3);
		
	}
	
	@Test(timeout = 100)
	public void testTree2infix() {
		
		LinkedBinaryTree<String> tree = Assignment.prefix2tree("+ 5 10");
		String infix = Assignment.tree2infix(tree);
		assertNotEquals("(5+5)" , infix);
		assertEquals("(5+10)" , infix);
		
		tree = Assignment.prefix2tree("* - 1 + b 3 d");
		infix = Assignment.tree2infix(tree);
		assertEquals("((1-(b+3))*d)" , infix);
		
		tree = Assignment.prefix2tree("a");
		infix = Assignment.tree2infix(tree);
		assertEquals("a" , infix);
		
		tree = Assignment.prefix2tree("+ + + a b c d");
		infix = Assignment.tree2infix(tree);
		assertEquals("(((a+b)+c)+d)" , infix);
		
		tree = Assignment.prefix2tree("+ - * + A B + C D - E F G");
		infix = Assignment.tree2infix(tree);
		assertEquals("((((A+B)*(C+D))-(E-F))+G)" , infix);
		
		//Illegal Argument check
		LinkedBinaryTree<String> tree3 = new LinkedBinaryTree<>();
		tree3.addRoot("+");
		thrown.expect(IllegalArgumentException.class);
		infix = Assignment.tree2infix(tree3);
		tree3.addLeft(tree3.root(), "3");
		thrown.expect(IllegalArgumentException.class);
		infix = Assignment.tree2infix(tree3);
		tree3.addRight(tree.root(), "+");
		thrown.expect(IllegalArgumentException.class);
		infix = Assignment.tree2infix(tree3);
		
	}
	@Test(timeout = 100)
	public void testSimplify() {
		
		// Normal simplify check
		LinkedBinaryTree<String> tree = Assignment.prefix2tree("* - 1 + b 3 d");
		tree = Assignment.simplify(tree);
		LinkedBinaryTree<String> expected =Assignment.prefix2tree("* - 1 + b 3 d");
		LinkedBinaryTree<String> not_expected =Assignment.prefix2tree("* 1 d");
		assertTrue(Assignment.equals(tree, expected));
		assertFalse(Assignment.equals(tree, not_expected));
		
		tree = Assignment.prefix2tree("- - 2 2 c");
		tree = Assignment.simplify(tree);
		expected = Assignment.prefix2tree("- 0 c");
		assertTrue(Assignment.equals(tree, expected));
		
		tree = Assignment.prefix2tree("- 2 5");
		tree = Assignment.simplify(tree);
		expected = Assignment.prefix2tree("-3");
		assertTrue(Assignment.equals(tree, expected));
		
		tree = Assignment.prefix2tree("- 0 0");
		tree = Assignment.simplify(tree);
		expected = Assignment.prefix2tree("0");
		assertTrue(Assignment.equals(tree, expected));
		
		tree = Assignment.prefix2tree("- + 2 5 4");
		tree = Assignment.simplify(tree);
		expected = Assignment.prefix2tree("3");
		assertTrue(Assignment.equals(tree, expected));
		
		tree = Assignment.prefix2tree("+ - * + 1 2 + 3 4 - 5 6 7");
		tree = Assignment.simplify(tree);
		expected = Assignment.prefix2tree("29");
		assertTrue(Assignment.equals(tree, expected));
		
		tree = Assignment.prefix2tree("- x + 1 2");
		tree = Assignment.simplify(tree);
		expected = Assignment.prefix2tree("- x 3");
		not_expected = Assignment.prefix2tree("- 3 x");
		assertTrue(Assignment.equals(tree, expected));
		assertFalse(Assignment.equals(tree, not_expected));
		
		//Illegal Argument check
		LinkedBinaryTree<String> tree3 = new LinkedBinaryTree<>();
		tree3.addRoot("+");
		thrown.expect(IllegalArgumentException.class);
		tree3 =Assignment.simplify(tree3);
		tree3.addLeft(tree3.root(), "3");
		thrown.expect(IllegalArgumentException.class);
		tree3 = Assignment.simplify(tree3);
		tree3.addRight(tree.root(), "+");
		thrown.expect(IllegalArgumentException.class);
		tree3 = Assignment.simplify(tree3);
		
	}

	@Test(timeout = 100)
	public void testSimplifyFancy() {
		
		// All simplify fancy check
		// They tries do check all the examples from the assignment spec
		LinkedBinaryTree<String> tree = Assignment.prefix2tree("- * 1 c + c 0");
		LinkedBinaryTree<String> expected = Assignment.prefix2tree("0");
		LinkedBinaryTree<String> not_expected = Assignment.prefix2tree("c");
		tree = Assignment.simplifyFancy(tree);
		assertTrue(Assignment.equals(tree, expected));
		assertFalse(Assignment.equals(tree, not_expected));
		
		tree = Assignment.prefix2tree("* 1 a");
		expected = Assignment.prefix2tree("a");
		not_expected = Assignment.prefix2tree("1");
		tree = Assignment.simplifyFancy(tree);
		assertTrue(Assignment.equals(tree, expected));
		assertFalse(Assignment.equals(tree, not_expected));
		
		tree = Assignment.prefix2tree("+ - + 1 1 2 c");
		expected = Assignment.prefix2tree("c");
		tree = Assignment.simplifyFancy(tree);
		assertTrue(Assignment.equals(tree, expected));
		
		tree = Assignment.prefix2tree("- * 1 c + c 1");
		expected = Assignment.prefix2tree("- c + c 1");
		not_expected = Assignment.prefix2tree("- c 1");
		tree = Assignment.simplifyFancy(tree);
		assertTrue(Assignment.equals(tree, expected));
		assertFalse(Assignment.equals(tree, not_expected));
		
		// Checking some edge case to see if tree simplify to 0 or not
		tree = Assignment.prefix2tree("- * 1 x x");
		expected = Assignment.prefix2tree("0");
		not_expected = Assignment.prefix2tree("-1");
		tree = Assignment.simplifyFancy(tree);
		assertTrue(Assignment.equals(tree, expected));
		assertFalse(Assignment.equals(tree, not_expected));
		
		// Checking some edge case to see if tree simplify to x or not
		tree = Assignment.prefix2tree("- x 0");
		expected = Assignment.prefix2tree("x");
		tree = Assignment.simplifyFancy(tree);
		assertTrue(Assignment.equals(tree, expected));
		assertFalse(Assignment.equals(tree, not_expected));
		
		tree = Assignment.prefix2tree("* x -1");
		expected = Assignment.prefix2tree("-x");
		tree = Assignment.simplifyFancy(tree);
		assertTrue(Assignment.equals(tree, expected));
		
		tree = Assignment.prefix2tree("* x -2");
		expected = Assignment.prefix2tree("-2x");
		tree = Assignment.simplifyFancy(tree);
		assertTrue(Assignment.equals(tree, expected));
		
		tree = Assignment.prefix2tree("- 0 2");
		expected = Assignment.prefix2tree("-2");
		tree = Assignment.simplifyFancy(tree);
		assertTrue(Assignment.equals(tree, expected));
		
		// Checking for some special edge cases
		tree = Assignment.prefix2tree("* 0 + 9 + d c");
		expected = Assignment.prefix2tree("0");
		tree = Assignment.simplifyFancy(tree);
		assertTrue(Assignment.equals(tree, expected));
		
		tree = Assignment.prefix2tree("* + a b 1");
		expected = Assignment.prefix2tree("+ a b");
		tree = Assignment.simplifyFancy(tree);
		assertTrue(Assignment.equals(tree, expected));
		
		tree = Assignment.prefix2tree("* 1 + - 4 c 3");
		expected = Assignment.prefix2tree("+ - 4 c 3");
		tree = Assignment.simplifyFancy(tree);
		assertTrue(Assignment.equals(tree, expected));
		
		//Illegal Argument check
		LinkedBinaryTree<String> tree3 = new LinkedBinaryTree<>();
		
		tree3.addRoot("+");
		thrown.expect(IllegalArgumentException.class);
		tree3 =Assignment.simplifyFancy(tree3);
		
		tree3.addLeft(tree3.root(), "3");
		thrown.expect(IllegalArgumentException.class);
		tree3 = Assignment.simplifyFancy(tree3);
		
		tree3.addRight(tree.root(), "a");
		expected = Assignment.prefix2tree("+ 3 a");
		tree = Assignment.simplifyFancy(tree);
		assertTrue(Assignment.equals(tree, expected));
		
		tree3.addLeft(tree.left(tree.root()), "a");
		thrown.expect(IllegalArgumentException.class);
		tree3 = Assignment.simplifyFancy(tree3);
		
	}
	
	@Test(timeout = 100)
	public void testSubstitute_Variable_and_Value(){
		
		// Checking variable and values
		LinkedBinaryTree<String> tree = Assignment.prefix2tree("* - 1 + b 3 d");
		String variable = "b";
		int value = 0;
		tree = Assignment.substitute(tree, variable, value);
		LinkedBinaryTree<String> expected =Assignment.prefix2tree("* - 1 + 0 3 d");
		LinkedBinaryTree<String> not_expected =Assignment.prefix2tree("* - 1 + b 3 0");
		assertTrue(Assignment.equals(tree, expected));
		assertFalse(Assignment.equals(tree, not_expected));
		
		tree = Assignment.prefix2tree("+ - * + A B + C D - E F G");
		variable ="A";
		value = 1;
		tree = Assignment.substitute(tree, variable, value);
		expected =Assignment.prefix2tree("+ - * + 1 B + C D - E F G");
		not_expected =Assignment.prefix2tree("+ - * + A B + C D - E F G");
		assertTrue(Assignment.equals(tree, expected));
		assertFalse(Assignment.equals(tree, not_expected));
		
		variable = "G";
		value = -7;
		tree = Assignment.substitute(tree, variable, value);
		expected =Assignment.prefix2tree("+ - * + 1 B + C D - E F -7");
		not_expected =Assignment.prefix2tree("+ - * + A B + C D - E F G");
		assertTrue(Assignment.equals(tree, expected));
		assertFalse(Assignment.equals(tree, not_expected));
		
		// Check for not existing tree's operand
		variable = "Z";
		value = 26;
		tree = Assignment.substitute(tree, variable, value);
		expected =Assignment.prefix2tree("+ - * + 1 B + C D - E F -7");
		not_expected =Assignment.prefix2tree("+ - * + A B + C D - E F 26");
		assertTrue(Assignment.equals(tree, expected));
		assertFalse(Assignment.equals(tree, not_expected));
		
		// checking operation values -> nothing happens to tree in this case
		tree = Assignment.prefix2tree("+ - * + A B + C D - E F G");
		expected =Assignment.prefix2tree("+ - * + A B + C D - E F G");
		variable = "+"; value = 0;
		tree = Assignment.substitute(tree, variable, value);
		assertTrue(Assignment.equals(tree, expected));
		
		//Illegal Argument check
		LinkedBinaryTree<String> tree3 = new LinkedBinaryTree<>();
		thrown.expect(IllegalArgumentException.class);
		tree3 =Assignment.substitute(tree3,variable,value);
		
		tree3.addRoot("+");
		thrown.expect(IllegalArgumentException.class);
		tree3 =Assignment.substitute(tree3,variable,value);
		
		tree = Assignment.prefix2tree("+ - * + A B + C D - E F G");
		variable = null;
		value = 0;
		thrown.expect(IllegalArgumentException.class);
		tree3 =Assignment.substitute(tree3,variable,value);
		
	}
	
	@Test(timeout = 100)
	public void testSubstitute_HashmapValues(){
		
		// Hashmap multiple key to replace tree's value check
		LinkedBinaryTree<String> tree = Assignment.prefix2tree("+ - * + A B + C D - E F G");
		HashMap<String,Integer> map = new HashMap<>();
		map.put("C", new Integer(3));
		map.put("B", new Integer(2));
		map.put("D", new Integer(4));
		tree = Assignment.substitute(tree, map);
		
		LinkedBinaryTree<String> expected = Assignment.prefix2tree("+ - * + A 2 + 3 4 - E F G");
		LinkedBinaryTree<String> not_expected = Assignment.prefix2tree("+ - * + A B + C D - E F G");
		assertTrue(Assignment.equals(tree, expected));
		assertFalse(Assignment.equals(tree, not_expected));
		
		map.put("2", new Integer(-20));
		map.put("3" , new Integer(-30));
		tree = Assignment.substitute(tree, map);
		expected = Assignment.prefix2tree("+ - * + A -20 + -30 4 - E F G");
		assertTrue(Assignment.equals(tree, expected));
		
		map.clear();
		tree = Assignment.prefix2tree("+ - * + A B + C D - E F G");
		tree = Assignment.substitute(tree, map);
		expected = Assignment.prefix2tree("+ - * + A B + C D - E F G");
		assertTrue(Assignment.equals(tree, expected));
		
		// Map's key not matching tree's operand check
		map.put("Z", new Integer(26));
		map.put("Y", new Integer(25));
		tree = Assignment.prefix2tree("+ - * + A B + C D - E F G");
		tree = Assignment.substitute(tree, map);
		expected = Assignment.prefix2tree("+ - * + A B + C D - E F G");
		not_expected = Assignment.prefix2tree("+ - * + 25 B + 26 D - E F G");
		assertTrue(Assignment.equals(tree, expected));
		assertFalse(Assignment.equals(tree, not_expected));
		
		map.clear();
		tree = Assignment.prefix2tree("+ - * + A B + C D - E F G");
		map.put("+" , new Integer(0));
		expected = Assignment.prefix2tree("+ - * + A B + C D - E F G");
		assertTrue(Assignment.equals(tree, expected));
		
		//Illegal Argument check
		LinkedBinaryTree<String> tree3 = new LinkedBinaryTree<>();
		thrown.expect(IllegalArgumentException.class);
		tree3 =Assignment.substitute(tree3,map);
		
		tree3.addRoot("+");
		thrown.expect(IllegalArgumentException.class);
		tree3 =Assignment.substitute(tree3,map);
		
		//HashMap null value check which throws IllegalArgumentException
		tree3 = Assignment.prefix2tree("+ A B");
		map.put("A", null);
		map.put("B" , new Integer(9));
		thrown.expect(IllegalArgumentException.class);
		tree3 =Assignment.substitute(tree3,map);
		
	}
	@Test(timeout = 100)
	public void testArithmeticExpression(){
		
		// Valid Arithmetic Expression check
		LinkedBinaryTree<String> tree = Assignment.prefix2tree("- + * B 2 * * 4 A C * 1 2");
		assertTrue(Assignment.isArithmeticExpression(tree));
		
		tree = Assignment.prefix2tree("0");
		assertTrue(Assignment.isArithmeticExpression(tree));
		
		tree = Assignment.prefix2tree("- A 0");
		assertTrue(Assignment.isArithmeticExpression(tree));
		
		tree = Assignment.prefix2tree("+ - * + A B + C D - E F G");
		assertTrue(Assignment.isArithmeticExpression(tree));
		
		// Invalid Arithmetic Expression check
		LinkedBinaryTree<String> tree2 = new LinkedBinaryTree<>();
		assertFalse(Assignment.isArithmeticExpression(tree2));
		
		tree2.addRoot("+");
		tree2.addLeft(tree2.root(), "0");
		assertFalse(Assignment.isArithmeticExpression(tree2));
		
		tree2.addRight(tree2.root(), "+");
		assertFalse(Assignment.isArithmeticExpression(tree2));
		
		LinkedBinaryTree<String> tree3 = new LinkedBinaryTree<>();
		tree3.addRoot("+");
		Position<String> temp = tree3.addLeft(tree3.root(), "+");
		tree3.addLeft(temp, "A");
		tree3.addRight(temp, "+");
		tree3.addRight(tree3.root(), "B");
		assertFalse(Assignment.isArithmeticExpression(tree3));
		
	}

}
