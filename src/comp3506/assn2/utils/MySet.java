package comp3506.assn2.utils;

import java.util.Iterator;

public class MySet<E> implements Iterable<E> {

	/**
	 * An iterable collection
	 * 
	 * @author Weizhou Ren
	 *
	 */
	private class Node {
		private E element;
		private Node next;

		public E getElement() {
			return element; 
		}

		public void setElement(E e) {
			element = e;
		}

		public Node getNext() {
			return next;
		}

		public void setNext(Node n) {
			next = n;
		}
	}

	private int size = 0; // number of nodes in set
	private Node head = null; // first node in the set
	private Node tail = null;

	public MySet() {
	}

	/**
	 * 
	 * @return size of the set
	 */
	public int size() {
		return size;
	}

	/**
	 * Check if the set is empty
	 * 
	 * @return true or false
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Add element at the last position
	 * 
	 * @param e The element need to be added
	 */
	public void add(E e) {

		Node node = new Node();
		node.setElement(e);
		node.setNext(null);
		if (isEmpty()) {
			head = node;
			tail = head;
		} else {
			tail.setNext(node);
			tail = node;
		}
		size++;
	}

	/**
	 * Remove element e from the set
	 * 
	 * @param e The element that need to be removed
	 */
	public void remove(E e) {

		if (contains(e)) {

			Node temp = head;

			while (temp.getNext() != null) {
				if (temp.equals(e)) {
					head = temp.getNext();
				}
				if (temp.getNext().getElement().equals(e)) {
					if (temp.getNext().getNext() != null) {
						temp.setNext(temp.getNext().getNext());
					} else {
						temp.setNext(null);
						tail = temp;
					}

				} else {
					temp = temp.getNext();
				}
			}
			size--;
		} else {
			System.out.println("doesn't contain" + e.toString());
		}
	}

	/**
	 * Check if the set has already had e
	 * 
	 * @param e the element need to be checked
	 * @return true iff contains e, otherwise false
	 */
	public boolean contains(E e) {
		boolean hasContained = false;
		if (isEmpty()) {
			return false;
		} else {
			Node temp = head;
			while (temp.getNext() != null) {
				if (temp.getElement().equals(e)) {
					return true;
				} else {
					temp = temp.getNext();
				}
			}
			if (tail.getElement().equals(e)) {
				return true;
			}
		}
		return hasContained;
	}

	/**
	 * Override iterator for my set
	 */
	@Override
	public Iterator<E> iterator() {
		return new ITerator();
	}

	private class ITerator implements Iterator<E> {

		private Node current = head;

		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public E next() {
			// TODO Auto-generated method stub
			if (hasNext()) {
				E element = current.getElement();
				current = current.getNext();
				return element;
			}
			return null;
		}
	}

	/**
	 * Get intersection between two sets
	 * 
	 * @param set The set need to be compared
	 * @return New set that has common elements
	 */
	public MySet<E> getIntersection(MySet<E> set) {
		MySet<E> intersection = new MySet<>();
		for (E e : set) {
			if (contains(e)) {
				intersection.add(e);
			}
		}
		return intersection;
	}

	/**
	 * Get difference between two sets
	 * 
	 * @param set The set need to be compared
	 * @return New set that contains difference
	 */
	public MySet<E> getSubtraction(MySet<E> set) {
		MySet<E> subtraction = new MySet<>();
		for (E e : set) {
			if (!contains(e)) {
				subtraction.add(e);
			}
		}
		return subtraction;
	}

	/**
	 * Merge two set together
	 * 
	 * @param set The set need to be added
	 */
	public void getUnion(MySet<E> set) {
		for (E e : set) {
			if (!contains(e)) {
				add(e);
			}
		}
	}

	/**
	 * Convert an array to a set
	 * 
	 * @param array need to be converted
	 * @return new set
	 */
	public MySet<E> toSet(E[] array) {
		MySet<E> set = new MySet<>();
		for (E e : array) {
			set.add(e);
		}
		return set;
	}

}
