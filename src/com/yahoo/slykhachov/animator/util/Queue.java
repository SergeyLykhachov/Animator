package com.yahoo.slykhachov.animator.util;

import java.util.Collection;

public interface Queue<T> {
	/*
	*	Inserts the specified element into this queue if it is
	*	possible to do so immediately without violating capacity restrictions.
	*	 When using a capacity-restricted queue, this method is generally
	*	preferable to add(E), which can fail to insert an element only by
	*	throwing an exception.
	*/
	boolean offer(T t);
	/*
	*	Retrieves and removes the head of this queue, or returns
	*	null if this queue is empty.
	*/
	T poll();
	/*
	*	Retrieves, but does not remove, the head of this queue, or
	*	returns null if this queue is empty.
	*/
	T peek();
	/*
	*	Returns the number of elements in this list.
	*/
	int	size();
	/*
	*	Appends all of the elements in the specified collection to the
	*	end of this list, in the order that they are returned by the
	*	specified collection's iterator. The behavior of this operation
	*	is undefined if the specified collection is modified while the
	*	operation is in progress. (Note that this will occur if the
	*	specified collection is this list, and it's nonempty.)
	*/
	boolean addAll(Collection<? extends T> c);
}
