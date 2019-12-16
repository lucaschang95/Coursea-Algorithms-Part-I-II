import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;
import java.util.NoSuchElementException;

// resizing-array implementation
public class RandomizedQueue<Item> implements Iterable<Item> {
	
	private Item[] s;
	private int n = 0;
	
	public RandomizedQueue() {
		s = (Item[]) new Object[1]; 
	}
	
	public boolean isEmpty() {
		return n == 0;
	}
	
	public int size() {
		return n;
	}
	
	private void resize(int capacity) {
		Item[] copy = (Item[]) new Object[capacity];
		for (int i = 0; i < n; i++) {
			copy[i] = s[i];
		}
		s = copy;
	}
	
	public void enqueue(Item item) {
		if (item == null) throw new IllegalArgumentException();
		if (n == s.length) resize(2*s.length);
		s[n++] = item;
	}
	
	public Item dequeue() {
		if (n == 0) {
			throw new NoSuchElementException();
		} else {
			int j = StdRandom.uniform(n);
			Item item = s[j];
			s[j] = s[n-1];
			s[n-1] = null;
			n--;
			if (n > 0 && n == s.length/4) resize(s.length/2);
			return item;
		}
		
	}
	
	public Item sample() {
		if (n == 0) throw new NoSuchElementException();
		return s[StdRandom.uniform(n)];
	}
	
	public Iterator<Item> iterator() {
		return new RandomArrayIterator();
	}
	
	private class RandomArrayIterator implements Iterator<Item> {
		private int i = n;
		private Item[] iterableArray;
		private RandomArrayIterator() {
			iterableArray = s;
		}
		public boolean hasNext() {
			return i > 0;
		}
		public void remove() {
			throw new UnsupportedOperationException();
		}
		public Item next() {
			if (n == 0) {
				throw new NoSuchElementException();
			}
			else {
				int j = StdRandom.uniform(i);
				i--;
				Item item = iterableArray[j];
				iterableArray[j] = iterableArray[i];
				iterableArray[i] = item;
				return item;
			}

		}
	}	
}
