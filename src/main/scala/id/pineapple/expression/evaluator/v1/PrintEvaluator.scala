package id.pineapple.expression.evaluator.v1

trait PrintEvaluator extends Evaluator[String] {
	override def int(e: BigInt): String = e.toString
	override def binOp(op: String, a: String, b: String): String = s"($a $op $b)"
}
