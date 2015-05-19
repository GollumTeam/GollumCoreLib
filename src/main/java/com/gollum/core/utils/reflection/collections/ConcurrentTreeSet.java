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
		if(lock != null)lock.readLock().lock();
		try {
			return super.iterator();
		} finally {
			if(lock != null)lock.readLock().unlock();
		}
	}
	
	public Iterator<E> descendingIterator() {
		if(lock != null)lock.readLock().lock();
		try {
			return super.descendingIterator();
		} finally {
			if(lock != null)lock.readLock().unlock();
		}
	}
	
	public NavigableSet<E> descendingSet() {
		if(lock != null)lock.readLock().lock();
		try {
			return super.descendingSet();
		} finally {
			if(lock != null)lock.readLock().unlock();
		}
	}
	
	public int size() {
		if(lock != null)lock.readLock().lock();
		try {
			return super.size();
		} finally {
			if(lock != null)lock.readLock().unlock();
		}
	}
	
	public boolean isEmpty() {
		if(lock != null)lock.readLock().lock();
		try {
			return super.isEmpty();
		} finally {
			if(lock != null)lock.readLock().unlock();
		}
	}
	
	public boolean contains(Object o) {
		if(lock != null)lock.readLock().lock();
		try {
			return super.contains(o);
		} finally {
			if(lock != null)lock.readLock().unlock();
		}
	}
	
	public boolean add(E e) {
		if(lock != null)lock.writeLock().lock();
		try {
			return super.add(e);
		} finally {
			if(lock != null)lock.writeLock().unlock();
		}
	}
	
	public boolean remove(Object o) {
		if(lock != null)lock.writeLock().lock();
		try {
			return super.remove(o);
		} finally {
			if(lock != null)lock.writeLock().unlock();
		}
	}
	
	public void clear() {
		if(lock != null)lock.writeLock().lock();
		try {
			super.clear();
		} finally {
			if(lock != null)lock.writeLock().unlock();
		}
	}
	
	public  boolean addAll(Collection<? extends E> c) {
		if(lock != null)lock.writeLock().lock();
		try {
			return super.addAll(c);
		} finally {
			if(lock != null)lock.writeLock().unlock();
		}
	}
	
	public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement,   boolean toInclusive)  {
		if(lock != null)lock.writeLock().lock();
		try {
			return super.subSet(fromElement, fromInclusive, toElement, toInclusive);
		} finally {
			if(lock != null)lock.writeLock().unlock();
		}
	}
	
	public NavigableSet<E> headSet(E toElement, boolean inclusive) {
		if(lock != null)lock.writeLock().lock();
		try {
			return super.headSet(toElement, inclusive);
		} finally {
			if(lock != null)lock.writeLock().unlock();
		}
	}
	
	public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
		if(lock != null)lock.writeLock().lock();
		try {
			return super.tailSet(fromElement, inclusive);
		} finally {
			if(lock != null)lock.writeLock().unlock();
		}
	}
	
	public SortedSet<E> subSet(E fromElement, E toElement) {
		if(lock != null)lock.writeLock().lock();
		try {
			return super.subSet(fromElement, toElement);
		} finally {
			if(lock != null)lock.writeLock().unlock();
		}
	}
	
	public SortedSet<E> headSet(E toElement) {
		if(lock != null)lock.writeLock().lock();
		try {
			return super.headSet(toElement);
		} finally {
			if(lock != null)lock.writeLock().unlock();
		}
	}
	
	public SortedSet<E> tailSet(E fromElement) {
		if(lock != null)lock.writeLock().lock();
		try {
			return super.headSet(fromElement);
		} finally {
			if(lock != null)lock.writeLock().unlock();
		}
	}
	
	public Comparator<? super E> comparator() {
		if(lock != null)lock.readLock().lock();
		try {
			return super.comparator();
		} finally {
			if(lock != null)lock.readLock().unlock();
		}
	}
	
	public E first() {
		if(lock != null)lock.readLock().lock();
		try {
			return super.first();
		} finally {
			if(lock != null)lock.readLock().unlock();
		}
	}
	
	public E last() {
		if(lock != null)lock.readLock().lock();
		try {
			return super.last();
		} finally {
			if(lock != null)lock.readLock().unlock();
		}
	}
	
	public E lower(E e) {
		if(lock != null)lock.readLock().lock();
		try {
			return super.lower(e);
		} finally {
			if(lock != null)lock.readLock().unlock();
		}
	}
	
	public E floor(E e) {
		if(lock != null)lock.readLock().lock();
		try {
			return super.floor(e);
		} finally {
			if(lock != null)lock.readLock().unlock();
		}
	}
	
	public E ceiling(E e) {
		if(lock != null)lock.writeLock().lock();
		try {
			return super.ceiling(e);
		} finally {
			if(lock != null)lock.writeLock().unlock();
		}
	}
	
	public E higher(E e) {
		if(lock != null)lock.writeLock().lock();
		try {
			return super.higher(e);
		} finally {
			if(lock != null)lock.writeLock().unlock();
		}
	}
	
	public E pollFirst() {
		if(lock != null)lock.writeLock().lock();
		try {
			return super.pollFirst();
		} finally {
			if(lock != null)lock.writeLock().unlock();
		}
	}
	
	public E pollLast() {
		if(lock != null)lock.writeLock().lock();
		try {
			return super.pollLast();
		} finally {
			if(lock != null)lock.writeLock().unlock();
		}
	}
	
	public Object clone() {
		if(lock != null)lock.writeLock().lock();
		try {
			return super.clone();
		} finally {
			if(lock != null)lock.writeLock().unlock();
		}
	}
	
}