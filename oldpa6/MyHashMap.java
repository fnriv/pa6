import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class MyHashMap<K, V> implements DefaultMap<K, V> {
	public static final double DEFAULT_LOAD_FACTOR = 0.75;
	public static final int DEFAULT_INITIAL_CAPACITY = 16;
	public static final String ILLEGAL_ARG_CAPACITY = "Initial Capacity must be non-negative";
	public static final String ILLEGAL_ARG_LOAD_FACTOR = "Load Factor must be positive";
	public static final String ILLEGAL_ARG_NULL_KEY = "Keys must be non-null";

	private double loadFactor;
	private int capacity;
	private int size;

	// Use this instance variable for Separate Chaining conflict resolution
	private List<HashMapEntry<K, V>>[] buckets;

	// Use this instance variable for Linear Probing
	// private HashMapEntry<K, V>[] entries;

	public MyHashMap() {
		this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
	}

	/**
	 * 
	 * @param initialCapacity the initial capacity of this MyHashMap
	 * @param loadFactor      the load factor for rehashing this MyHashMap
	 * @throws IllegalArgumentException if initialCapacity is negative or loadFactor
	 *                                  not positive
	 */
	@SuppressWarnings("unchecked")
	public MyHashMap(int initialCapacity, double loadFactor) throws IllegalArgumentException {
		// TODO Finish initializing instance fields
		if (initialCapacity >= 0) {
			this.capacity = initialCapacity;
		} else {
			throw new IllegalArgumentException(ILLEGAL_ARG_CAPACITY);
		}

		if (loadFactor > 0) {
			this.loadFactor = loadFactor;
		} else {
			throw new IllegalArgumentException(ILLEGAL_ARG_LOAD_FACTOR);
		}

		// if you use Separate Chaining
		buckets = (List<HashMapEntry<K, V>>[]) new List<?>[capacity];
		for (int i = 0; i < this.buckets.length; i++) {
			this.buckets[i] = new ArrayList<HashMapEntry<K, V>>();
		}

		// if you use Linear Probing
		//entries = (HashMapEntry<K, V>[]) new HashMapEntry<?, ?>[initialCapacity];
	}

	/**
	 * Adds the specified key, value pair to this DefaultMap
	 * Note: duplicate keys are not allowed
	 * 
	 * @return true if the key value pair was added to this DefaultMap
	 * @throws IllegalArgument exception if the key is null
	 */
	@Override
	public boolean put(K key, V value) throws IllegalArgumentException {
		// can also use key.hashCode() assuming key is not null
		int keyHash = Math.abs(Objects.hashCode(key));
		int index = keyHash % this.buckets.length;
		if (key == null) {
			throw new IllegalArgumentException(ILLEGAL_ARG_NULL_KEY);
		}
		if (this.containsKey(key)) {
			return false;
		} else {
			if (loadFactor < (this.size / this.buckets.length)) {
				this.expandCapacity();
			}
			this.buckets[index].add(new HashMapEntry<K, V>(key, value));
			this.size++;
			return true;
		}
	}

	/**
	 * Replaces the value that maps to the key if it is present
	 * @param key The key whose mapped value is being replaced
	 * @param newValue The value to replace the existing value with
	 * @return true if the key was in this DefaultMap
	 * @throws IllegalArgument exception if the key is null
	 */
	@Override
	public boolean replace(K key, V newValue) throws IllegalArgumentException {
		int keyHash = Math.abs(Objects.hashCode(key));
		int index = keyHash % this.buckets.length;
		if (key == null) {
			throw new IllegalArgumentException(ILLEGAL_ARG_NULL_KEY);
		}
		if (!this.containsKey(key)) {
			return false;
		} else {
			List<HashMapEntry<K, V>> bucket = this.buckets[index];
			boolean checkIfnewSet = false;
			for (HashMapEntry<K, V> hmEntry : bucket) {
				if (hmEntry.getKey().equals(key)) {
					hmEntry.setValue(newValue);
					checkIfnewSet = true;
				}
			}
			return checkIfnewSet;

		}
	}

	/**
	 * Remove the entry corresponding to the given key
	 * 
	 * @return true if an entry for the given key was removed
	 * @throws IllegalArgument exception if the key is null
	 */
	@Override
	public boolean remove(K key) throws IllegalArgumentException {
		int keyHash = Math.abs(Objects.hashCode(key));
		int index = keyHash % this.buckets.length;

		if (key == null) {
			throw new IllegalArgumentException(ILLEGAL_ARG_NULL_KEY);
		}

		List<HashMapEntry<K, V>> bucket = this.buckets[index];
		if (bucket.isEmpty()) {
			return false;
		}
		boolean checkIfRemove = false;
		for (HashMapEntry<K, V> hmEntry : bucket) {
			if (hmEntry.getKey().equals(key)) {
				bucket.remove(hmEntry);
				this.size--;
				return true;
			}
		}
		return checkIfRemove;
	}

	/**
	 * Adds the key, value pair to this DefaultMap if it is not present,
	 * otherwise, replaces the value with the given value
	 * @throws IllegalArgument exception if the key is null
	 */
	@Override
	public void set(K key, V value) throws IllegalArgumentException {
		int keyHash = Math.abs(Objects.hashCode(key));
		int index = keyHash % this.buckets.length;

		if (key == null) {
			throw new IllegalArgumentException(ILLEGAL_ARG_NULL_KEY);
		}
		if (!this.containsKey(key)) {
			this.put(key, value);
		} else {
			List<HashMapEntry<K, V>> bucket = this.buckets[index];
			for (HashMapEntry<K, V> hmEntry : bucket) {
				if (hmEntry.getKey().equals(key)) {
					hmEntry.setValue(value);
				}
			}
		}

	}

	/**
	 * @return the value corresponding to the specified key, null if key doesn't 
	 * exist in hash map
	 * @throws IllegalArgument exception if the key is null
	 */
	@Override
	public V get(K key) throws IllegalArgumentException {
		if (key == null) {
			throw new IllegalArgumentException(ILLEGAL_ARG_NULL_KEY);
		}

		if (this.isEmpty()) {
			return null;
		}

		int keyHash = Math.abs(Objects.hashCode(key));
		int index = keyHash % this.buckets.length;

		if (!this.containsKey(key)) {
			return null;
		} else {
			List<HashMapEntry<K, V>> bucket = this.buckets[index];
			V toReturn = null;
			for (HashMapEntry<K, V> hmEntry : bucket) {
				if (hmEntry.getKey().equals(key)) {
					toReturn = hmEntry.getValue();
				}
			}
			return toReturn;
		}
	}

	/**
	 * 
	 * @return The number of (key, value) pairs in this DefaultMap
	 */
	@Override
	public int size() {
		return this.size;
	}

	/**
	 * 
	 * @return true iff this.size() == 0 is true
	 */
	@Override
	public boolean isEmpty() {
		return this.size() == 0;
			
	}

	/**
	 * @param key the Key to be searched for in this DefaultMap
	 * @return true if the specified key is in this DefaultMap
	 * @throws IllegalArgument exception if the key is null
	 */
	@Override
	public boolean containsKey(K key) throws IllegalArgumentException {
		int keyHash = Math.abs(Objects.hashCode(key));
		int index = keyHash % this.buckets.length;
		if (key == null) {
			throw new IllegalArgumentException(ILLEGAL_ARG_NULL_KEY);
		} else {
			List<HashMapEntry<K, V>> bucket = this.buckets[index];
			boolean containsKey = false;
			for (HashMapEntry<K, V> hmEntry : bucket) {
				if (hmEntry.getKey().equals(key)) {
					containsKey = true;
				}
			}
			return containsKey;
		}

	}

	/**
	 * 
	 * @return an array containing the keys of this DefaultMap. If this DefaultMap is 
	 * empty, returns array of length zero. 
	 */
	
	@Override
	public List<K> keys() {
		if (this.isEmpty()) {
			return new ArrayList<K>(0);
		}
		List<K> toReturn = new ArrayList<K>();
		for (List<HashMapEntry<K, V>> b : this.buckets) {
			for (HashMapEntry<K, V> hmentry : b) {
				toReturn.add(hmentry.getKey());
			}
		}
		return toReturn;
	}

	private static class HashMapEntry<K, V> implements DefaultMap.Entry<K, V> {

		K key;
		V value;

		private HashMapEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		/**
		 * @return key of this HashMapEntry
		 */
		@Override
		public K getKey() {
			return key;
		}

		/**
		 * @return value of this HashMapEntry
		 */
		@Override
		public V getValue() {
			return value;
		}

		/**
		 * replaces value of this HashMapEntry 
		 * @param value the value replacting the current value in this HashMapEntry
		 */
		@Override
		public void setValue(V value) {
			this.value = value;
		}
	}

//	HELPER METHODS -------------

	/**
	 * creates a new array with double the capacity
	 * reenters the old values into the new DefaultMap
	 */
	@SuppressWarnings("unchecked")
	public void expandCapacity() {
		List<MyHashMap.HashMapEntry<K, V>>[] newBuckets = 
			(List<HashMapEntry<K, V>>[]) new List<?>[this.buckets.length * 2];

		for (int i = 0; i < this.buckets.length; i++) {
			this.buckets[i] = new ArrayList<HashMapEntry<K, V>>();
		}
		List<HashMapEntry<K, V>>[] oldBuckets = this.buckets;
		this.buckets = newBuckets;
		this.capacity = newBuckets.length;
		this.size = 0;

		for (List<HashMapEntry<K, V>> b : oldBuckets) {
			for (MyHashMap.HashMapEntry<K, V> hmEntry : b) {
				this.set(hmEntry.getKey(), hmEntry.getValue());
			}
		}

	}
}
