import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
	
	private int n = 0;
	private Node first = null;
	private Node last = null;
	
	private class Node {
		Item item;
		Node before;
		Node after;
	}
	
	public Deque() {
 
	}
	
	public boolean isEmpty() {
		return (n == 0);
	}
	
	public int size() {
		return n;
	}
	
	private void validate(Item item) {
		if (item == null) throw new IllegalArgumentException();
	}
	
	public void addFirst(Item item) {
		validate(item);
		if (n == 0) {
			Node root = new Node();
			root.item = item;
			root.before = null;
			root.after = null;
			first = root;
			last = root;
		} else {
			Node oldfirst = first;
			first = new Node();
			first.item = item;
			first.after = oldfirst;
			first.before = oldfirst.before;
			oldfirst.before = first;
		}
		n++;
	}
	
	public void addLast(Item item) {
		validate(item);
		if (n == 0) {
			Node root = new Node();
			root.item = item;
			root.before = null;
			root.after = null;
			first = root;
			last = root;
		} else {
			Node oldlast = last;
			last = new Node();
			last.item = item;
			last.before = oldlast;
			last.after = oldlast.after;
			oldlast.after = last;
		}
		n++;
	}
	
	public Item removeFirst() {
		if (n == 0) throw new NoSuchElementException();
		Item item = first.item;
		first = first.after;
		if (n == 1) {
			last = null;
		} else {
			first.before = null;
		}
		n--;
		return item;
	}
	
	public Item removeLast() {
		if (n == 0) throw new NoSuchElementException();
		Item item = last.item;
		last = last.before;
		if (n == 1) {
			first = null;
		} else {
			last.after = null;
		}
		n--;
		return item;
	}
	
	public Iterator<Item> iterator() {
		return new ListIterator();
	}
	
	private class ListIterator implements Iterator<Item> {
		private Node current = first;
		public boolean hasNext() {
			return current != null;
		}
		public void remove() {
			throw new UnsupportedOperationException();
		}
		public Item next() {
			if (current == null) throw new NoSuchElementException();
			Item item =  current.item;
			current = current.after;
			return item;
		}
	}
}
