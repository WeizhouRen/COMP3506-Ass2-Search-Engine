package comp3506.assn2.utils;

public class NewMap<K, V> {

	private int size = 0; // number of entries in the map
	private MySet<K> keySet;
	private MySet<V> valueSet;
	private MySet<NewEntry<K, V>> entrySet;

	private int capacity; // length of hash table
	private NewEntry<K, V>[] bucketTable;

	@SuppressWarnings("unchecked")
	public NewMap(int cap) {
		capacity = cap;
		bucketTable = new NewEntry[capacity];
		keySet = new MySet<>();
		valueSet = new MySet<>();
		entrySet = new MySet<>();
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public int size() {
		return size;
	}

	public Iterable<K> keySet() {
		return keySet;
	}

	public Iterable<V> values() {
		return valueSet;
	}

	public Iterable<NewEntry<K, V>> entrySet() {
		return entrySet;
	}

	/**
	 * Returns the value according to the specific key Or return null if does not
	 * contain that key
	 * 
	 * @param key The specific key
	 * @return corresponding value or null
	 */

	public V get(K key) {
		// find index of bucket table
		// compare with the existing key
		// if not equal, check next
		// is next null, do not found, return null
		int index = getBucketIndex(key);
		NewEntry<K, V> temp = bucketTable[index];
		if (temp == null) {
			return null; // didn't find
		} else { // fist entry in this cell is not null
			// check two keys until get result
			while (temp != null) {
				if (temp.getKey().equals(key)) {
					return temp.getValue();
				} else {
					temp = temp.getNext();
				}
			}
			return null;
		}

	}

	/**
	 * Add new entry which has key and value into the map
	 * 
	 * @param key   the adding key
	 * @param value the adding value
	 * @return the adding value from entry
	 */

	public V put(K key, V value) {

		int index = getBucketIndex(key);
		NewEntry<K, V> temp = bucketTable[index];
		if (temp == null) { // add first entry in this cell
			NewEntry<K, V> entryNew = new NewEntry<K, V>(key, value);
			bucketTable[index] = entryNew;
			addToSet(key, value, entryNew);
			size++;
			return value;
		} else { // such sell has already have entry
			while (temp != null) {
				// compare two keys
				if (!temp.getKey().equals(key)) { // if not equal, check next
					if (temp.getNext() == null) { // if next is null, set next entry is new entry
						temp.setNext(new NewEntry<K, V>(key, value));
						addToSet(key, value, new NewEntry<K, V>(key, value));
						size++;
						return value;
					} else { // if next is not null, compare to next entry
						temp = temp.getNext();
					}
				} else { // two keys are equal, cover the old value
					// remove old value
					valueSet.remove(temp.getValue());
					valueSet.add(value);// add new value
					temp.setValue(value);
					return value;
				}
			}
			return null;
		}
	}

	/**
	 * Check the key k has already existed or not
	 * 
	 * @param k
	 * @return true or flase
	 */
	public boolean containsKey(K k) {
		if (get(k) != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Get the hashCode and position in bucketTable to store
	 * 
	 * @param k
	 * @return index
	 */
	private int getBucketIndex(K k) {
		return Math.abs(k.hashCode()) % capacity;
	}

	/**
	 * Add key k to keySet, Add value v to values Add Entry e to entrySet
	 * 
	 * @param k
	 * @param v
	 * @param e
	 */
	private void addToSet(K k, V v, NewEntry<K, V> e) {
		keySet.add(k);
		valueSet.add(v);
		entrySet.add(e);
	}

	/**
	 * The bucketTable size should be redefined, which will make the map more
	 * efficient I didn't finish this method but it should be contained in my data
	 * structure
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	private void resizeBucketTable() {
		int newCapacity = capacity * 2 - 1;
		NewEntry<K, V>[] newTable = new NewEntry[newCapacity];
		for (int i = 0; i < capacity; i++) {
			newTable[i] = bucketTable[i];
		}
		capacity = newCapacity;
		bucketTable = newTable;
	}
}
