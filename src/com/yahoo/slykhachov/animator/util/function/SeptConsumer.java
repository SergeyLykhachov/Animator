package com.yahoo.slykhachov.animator.util.function;
@FunctionalInterface
public interface SeptConsumer<A> {
	void accept(A a, double ex, double why, double widthVal, double heightVal,
		double parentAbscissaInfo, double parentOrdinateInfo);
}
