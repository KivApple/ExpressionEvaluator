package id.pineapple.expression.evaluator.v1

trait Evaluator[R] {
	def int(i: BigInt): R
	def binOp(op: String, a: R, b: R): R
}
