package id.pineapple.expression.evaluator.v2

import id.pineapple.expression.evaluator.v1
import id.pineapple.expression.evaluator.v2
import id.pineapple.expression.evaluator.v1.WrappedEvaluator.Wrapped

trait WrappedEvaluator extends v1.WrappedEvaluator with v2.Evaluator[Wrapped] {
	override def uOp(op: String, a: Wrapped): Wrapped = new Wrapped {
		override def value[T](implicit e: v1.Evaluator[T]): T = e.asInstanceOf[v2.Evaluator[T]].uOp(op, a.value)
	}
}
