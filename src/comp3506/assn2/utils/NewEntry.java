package comp3506.assn2.utils;

/**
 * Entry pair which can be linked to next entry
 * @author Weizhou Ren
 *
 * @param <K> key
 * @param <V> value
 */
public class NewEntry<K, V> {

	private K key;
	private V value;
	private NewEntry<K, V> next = null;

	public NewEntry(K k, V v) {
		key = k;
		value = v;
	}

	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}
 
	public V get(K k) {
		return value;
	}

	public void setNext(NewEntry<K, V> n) {
		next = n;
	}

	public NewEntry<K, V> getNext() {
		return next;
	}

	public void setValue(V v) {
		value = v;
	}

}