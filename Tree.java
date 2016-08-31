//	This tree has no delete because the latest version that had been on Blackboard at the time I wrote the program contained an incomplete delete and because the application does not require a delete.

package lab09;

public class Tree {
	private Node root;

	public Tree() {
		create();
	}

	public void create() {
		root = null;
	}

	public boolean isFull() {
		boolean result;
		Node temp = new Node();
		if(temp == null) 
			result = true;
		else {
			result = false;
			temp = null;
		}
		return result;
	}

	public boolean isEmpty() {
		boolean result = (root == null);
		return result;
	}

	public void destroy() {
		root = destroyTree(root);
	}

	private Node destroyTree(Node branch) {
		if(branch != null) {
			branch.setLeft(destroyTree(branch.getLeft()));
			branch.setRight(destroyTree(branch.getRight()));
			branch.setData(null);
			branch = null;
		}
		return branch;
	}

	public int retrieve(String search) {
		int depth = 0;
		if(!isEmpty() && !root.getData().getKey().equals(search)) {
			depth++;
			if(root.getData().getKey().compareTo(search) < 0)
				depth = retrieveNode(search, root.getRight(), depth);
			else
				depth = retrieveNode(search, root.getLeft(), depth);
		}
		return depth;
	}

	private int retrieveNode(String search, Node branch, int depth) {
		if(branch == null)
			depth = -1;
		else if(!branch.getData().getKey().equals(search)) {
			depth++;
			if(branch.getData().getKey().compareTo(search) < 0)
				depth = retrieveNode(search, branch.getRight(), depth);
			else
				depth = retrieveNode(search, branch.getLeft(), depth);
		}
		return depth;
	}

	public boolean add(Element data) {
		boolean result;
		if(isFull())
			result = false;
		else {
			root = addNode(data, root);
			result = true;
		}
		return result;
	}

	private Node addNode(Element data, Node branch) {
		Node temp;
		if(branch == null) {
			temp = new Node();
			temp.setData(data);
			temp.setLeft(null);
			temp.setRight(null);
			branch = temp;
			temp = null;
		} else if(branch.getData().getKey().compareTo(data.getKey()) < 0)
			branch.setRight(addNode(data, branch.getRight()));
		else
			branch.setLeft(addNode(data, branch.getLeft()));
		return branch;
	}
}

class Node {
	private Element data;
	private Node left, right;

	public Node() { }

	public Node(Element data) {
		setData(data);
	}

	public void setData(Element data) {
		this.data = data;
	}
	public  Element getData( ) {
		return data;
	}

	public void setLeft(Node left) {
		this.left = left;
	}

	public void setRight(Node right) {
		this.right = right;
	}

	public Node getLeft( ) {
		return left;
	}

	public Node getRight( ) {
		return right;
	}
}