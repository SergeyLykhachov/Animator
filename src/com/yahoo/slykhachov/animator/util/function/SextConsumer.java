package com.yahoo.slykhachov.animator.util.function;
@FunctionalInterface
public interface SextConsumer<A> {
	void accept(A a, double scaleFactor, double ex, double why,
		double parentAbscissaInfo, double parentOrdinateInfo);
}
