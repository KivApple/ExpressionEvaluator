package id.pineapple.expression.evaluator.v2

import id.pineapple.expression.evaluator.v1
import id.pineapple.expression.evaluator.v2

trait PrintEvaluator extends v1.PrintEvaluator with v2.Evaluator[String] {
	override def uOp(op: String, a: String): String = s"$op$a"
}
