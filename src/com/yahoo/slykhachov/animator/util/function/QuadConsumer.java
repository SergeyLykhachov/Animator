package com.yahoo.slykhachov.animator.util.function;
@FunctionalInterface
public interface QuadConsumer<A> {
	void accept(A a, double shift, double x, double y);
}
