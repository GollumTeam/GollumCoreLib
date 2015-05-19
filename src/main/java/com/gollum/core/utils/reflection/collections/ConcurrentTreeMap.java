package com.gollum.core.utils.reflection.collections;

import java.io.IOException;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentTreeMap<K, V> extends TreeMap<K, V>{
	
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	public ConcurrentTreeMap() {
		super();
	}
	
	public ConcurrentTreeMap(Comparator<? super K> comparator) {
		super(comparator);
	}
	
	public ConcurrentTreeMap(Map<? extends K, ? extends V> m) {
		super(m);
	}
	
	public ConcurrentTreeMap(SortedMap<K, ? extends V> m) {
		super(m);
	}
	
	public boolean containsKey(Object key) {
		lock.readLock().lock();
		try {
			return super.containsKey(key);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public boolean containsValue(Object value) {
		lock.readLock().lock();
		try {
			return super.containsValue(value);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public V get(Object key) {
		lock.readLock().lock();
		try {
			return super.get(key);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public Comparator<? super K> comparator() {
		lock.readLock().lock();
		try {
			return super.comparator();
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public K firstKey() {
		lock.readLock().lock();
		try {
			return super.firstKey();
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public K lastKey() {
		lock.readLock().lock();
		try {
			return super.lastKey();
		} finally {
			lock.readLock().unlock();
		}
	}
	

	public void putAll(Map<? extends K, ? extends V> map) {
		lock.readLock().lock();
		try {
			super.putAll(map);
		} finally {
			lock.readLock().unlock();
		}
	}


	public V put(K key, V value) {
		lock.readLock().lock();
		try {
			return super.put(key, value);
		} finally {
			lock.readLock().unlock();
		}
	}


	public V remove(Object key) {
		lock.readLock().lock();
		try {
			return super.remove(key);
		} finally {
			lock.readLock().unlock();
		}
	}


	public void clear() {
		lock.readLock().lock();
		try {
			super.clear();
		} finally {
			lock.readLock().unlock();
		}
	}


	public Object clone() {
		lock.readLock().lock();
		try {
			return super.clone();
		} finally {
			lock.readLock().unlock();
		}
	}


	public Map.Entry<K, V> firstEntry() {
		lock.readLock().lock();
		try {
			return super.firstEntry();
		} finally {
			lock.readLock().unlock();
		}
	}
	

	public Map.Entry<K, V> lastEntry() {
		lock.readLock().lock();
		try {
			return super.lastEntry();
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public Map.Entry<K,V> pollFirstEntry() {
		lock.readLock().lock();
		try {
			return super.pollFirstEntry();
		} finally {
			lock.readLock().unlock();
		}
	}

	public Map.Entry<K,V> pollLastEntry() {
		lock.readLock().lock();
		try {
			return super.pollLastEntry();
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public Map.Entry<K,V> lowerEntry(K key) {
		lock.readLock().lock();
		try {
			return super.lowerEntry(key);
		} finally {
			lock.readLock().unlock();
		}
	}

	public K lowerKey(K key) {
		lock.readLock().lock();
		try {
			return super.lowerKey(key);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public Map.Entry<K,V> floorEntry(K key) {
		lock.readLock().lock();
		try {
			return super.floorEntry(key);
		} finally {
			lock.readLock().unlock();
		}
	}

	public K floorKey(K key) {
		lock.readLock().lock();
		try {
			return super.floorKey(key);
		} finally {
			lock.readLock().unlock();
		}
	}

	public Map.Entry<K,V> ceilingEntry(K key) {
		lock.readLock().lock();
		try {
			return super.ceilingEntry(key);
		} finally {
			lock.readLock().unlock();
		}
	}

	public K ceilingKey(K key) {
		lock.readLock().lock();
		try {
			return super.ceilingKey(key);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public Map.Entry<K,V> higherEntry(K key) {
		lock.readLock().lock();
		try {
			return super.higherEntry(key);
		} finally {
			lock.readLock().unlock();
		}
	}

	public K higherKey(K key) {
		lock.readLock().lock();
		try {
			return super.higherKey(key);
		} finally {
			lock.readLock().unlock();
		}
	}

	public Set<K> keySet() {
		lock.readLock().lock();
		try {
			return super.keySet();
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public NavigableSet<K> navigableKeySet() {
		lock.readLock().lock();
		try {
			return super.navigableKeySet();
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public NavigableSet<K> descendingKeySet() {
		lock.readLock().lock();
		try {
			return super.descendingKeySet();
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public Collection<V> values() {
		lock.readLock().lock();
		try {
			return Collections.synchronizedCollection(super.values());
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public Set<Map.Entry<K,V>> entrySet() {
		lock.readLock().lock();
		try {
			return super.entrySet();
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public NavigableMap<K, V> descendingMap() {
		lock.readLock().lock();
		try {
			return super.descendingMap();
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public NavigableMap<K,V> subMap(K fromKey, boolean fromInclusive, K toKey,   boolean toInclusive) {
		lock.readLock().lock();
		try {
			return super.subMap(fromKey, fromInclusive, toKey, toInclusive);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public NavigableMap<K,V> headMap(K toKey, boolean inclusive) {
		lock.readLock().lock();
		try {
			return super.headMap(toKey, inclusive);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public NavigableMap<K,V> tailMap(K fromKey, boolean inclusive) {
		lock.readLock().lock();
		try {
			return super.tailMap(fromKey, inclusive);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public SortedMap<K,V> subMap(K fromKey, K toKey) {
		lock.readLock().lock();
		try {
			return super.subMap(fromKey, toKey);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public SortedMap<K,V> headMap(K toKey) {
		lock.readLock().lock();
		try {
			return super.headMap(toKey);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public SortedMap<K,V> tailMap(K fromKey) {
		lock.readLock().lock();
		try {
			return super.tailMap(fromKey);
		} finally {
			lock.readLock().unlock();
		}
	}

}