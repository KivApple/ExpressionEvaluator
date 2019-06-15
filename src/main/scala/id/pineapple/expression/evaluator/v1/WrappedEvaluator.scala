package id.pineapple.expression.evaluator.v1

import id.pineapple.expression.evaluator.v1.WrappedEvaluator.Wrapped

trait WrappedEvaluator extends Evaluator[Wrapped] {
	override def int(i: BigInt): Wrapped = new Wrapped {
		override def value[T](implicit e: Evaluator[T]): T = e.int(i)
	}
	
	override def binOp(op: String, a: Wrapped, b: Wrapped): Wrapped = new Wrapped {
		override def value[T](implicit e: Evaluator[T]): T = e.binOp(op, a.value, b.value)
	}
}

object WrappedEvaluator {
	trait Wrapped {
		def value[T](implicit e: Evaluator[T]): T
	}
}
