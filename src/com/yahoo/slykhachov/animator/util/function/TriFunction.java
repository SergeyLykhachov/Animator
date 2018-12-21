package com.yahoo.slykhachov.animator.util.function;
@FunctionalInterface
public interface TriFunction<A, B> {
	B apply(A a, int x, int y);
}
