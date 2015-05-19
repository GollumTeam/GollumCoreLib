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
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentTreeSet<E> extends TreeSet<E>{
	
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	
	public ConcurrentTreeSet() {
		super ();
	}

	public ConcurrentTreeSet(Comparator<? super E> comparator) {
		super (comparator);
	}
	
	public ConcurrentTreeSet(Collection<? extends E> c) {
		super (c);
	}
	
	public ConcurrentTreeSet(SortedSet<E> s) {
		super(s);
	}
	
	public Iterator<E> iterator() {
		lock.readLock().lock();
		try {
			return super.iterator();
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public Iterator<E> descendingIterator() {
		lock.readLock().lock();
		try {
			return super.descendingIterator();
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public NavigableSet<E> descendingSet() {
		lock.readLock().lock();
		try {
			return super.descendingSet();
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public int size() {
		lock.readLock().lock();
		try {
			return super.size();
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public boolean isEmpty() {
		lock.readLock().lock();
		try {
			return super.isEmpty();
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public boolean contains(Object o) {
		lock.readLock().lock();
		try {
			return super.contains(o);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public boolean add(E e) {
		lock.readLock().lock();
		try {
			return super.add(e);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public boolean remove(Object o) {
		lock.readLock().lock();
		try {
			return super.remove(o);
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
	
	public  boolean addAll(Collection<? extends E> c) {
		lock.readLock().lock();
		try {
			return super.addAll(c);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement,   boolean toInclusive)  {
		lock.readLock().lock();
		try {
			return super.subSet(fromElement, fromInclusive, toElement, toInclusive);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public NavigableSet<E> headSet(E toElement, boolean inclusive) {
		lock.readLock().lock();
		try {
			return super.headSet(toElement, inclusive);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
		lock.readLock().lock();
		try {
			return super.tailSet(fromElement, inclusive);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public SortedSet<E> subSet(E fromElement, E toElement) {
		lock.readLock().lock();
		try {
			return super.subSet(fromElement, toElement);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public SortedSet<E> headSet(E toElement) {
		lock.readLock().lock();
		try {
			return super.headSet(toElement);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public SortedSet<E> tailSet(E fromElement) {
		lock.readLock().lock();
		try {
			return super.headSet(fromElement);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public Comparator<? super E> comparator() {
		lock.readLock().lock();
		try {
			return super.comparator();
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public E first() {
		lock.readLock().lock();
		try {
			return super.first();
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public E last() {
		lock.readLock().lock();
		try {
			return super.last();
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public E lower(E e) {
		lock.readLock().lock();
		try {
			return super.lower(e);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public E floor(E e) {
		lock.readLock().lock();
		try {
			return super.floor(e);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public E ceiling(E e) {
		lock.readLock().lock();
		try {
			return super.ceiling(e);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public E higher(E e) {
		lock.readLock().lock();
		try {
			return super.higher(e);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public E pollFirst() {
		lock.readLock().lock();
		try {
			return super.pollFirst();
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public E pollLast() {
		lock.readLock().lock();
		try {
			return super.pollLast();
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
	
}