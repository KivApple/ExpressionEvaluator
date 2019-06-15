package id.pineapple.expression.evaluator.v2

import id.pineapple.expression.evaluator.v1

trait Evaluator[R] extends v1.Evaluator[R] {
	def uOp(op: String, a: R): R
}
