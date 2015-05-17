package com.gollum.core.utils.reflection.collections;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentTreeMap<K, V> extends TreeMap<K, V>{
	
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	@Override
	public boolean containsKey(Object key) {
		lock.readLock().lock();
		try {
			return super.containsKey(key);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean containsValue(Object value) {
		lock.readLock().lock();
		try {
			return super.containsValue(value);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	@Override
	public V get(Object key) {
		lock.readLock().lock();
		try {
			return super.get(key);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public Comparator<? super K> comparator() {
		lock.readLock().lock();
		try {
			return super.comparator();
		} finally {
			lock.readLock().unlock();
		}
	}
	
	@Override
	public K firstKey() {
		lock.readLock().lock();
		try {
			return super.firstKey();
		} finally {
			lock.readLock().unlock();
		}
	}
	
	@Override
	public K lastKey() {
		lock.readLock().lock();
		try {
			return super.lastKey();
		} finally {
			lock.readLock().unlock();
		}
	}
	
	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		lock.readLock().lock();
		try {
			super.putAll(map);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public V put(K key, V value) {
		lock.readLock().lock();
		try {
			return super.put(key, value);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public V remove(Object key) {
		lock.readLock().lock();
		try {
			return super.remove(key);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public void clear() {
		lock.readLock().lock();
		try {
			super.clear();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public Object clone() {
		lock.readLock().lock();
		try {
			return super.clone();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public Map.Entry<K, V> firstEntry() {
		lock.readLock().lock();
		try {
			return super.firstEntry();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public Map.Entry<K, V> lastEntry() {
		lock.readLock().lock();
		try {
			return super.lastEntry();
		} finally {
			lock.readLock().unlock();
		}
	}
}