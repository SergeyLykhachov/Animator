package com.yahoo.slykhachov.animator.util;

public class MyQueue<E> implements Queue<E> {
	private java.util.LinkedList<E> theList;
	public MyQueue() {
		theList = new java.util.LinkedList<>();
	}
	public MyQueue(java.util.Collection<? extends E> c) {
		theList = new java.util.LinkedList<>(c);
	}
	@Override
	public boolean offer(E e) {
		return this.theList.offer(e);
	}
	@Override
	public E peek() {
		return this.theList.peek();
	}
	@Override
	public E poll() {
		return this.theList.poll();
	}
	@Override
	public int size() {
		return this.theList.size();
	}
	@Override
	public boolean addAll(java.util.Collection<? extends E> c) {
		return this.theList.addAll(c);
	}
}
