package id.pineapple.expression.evaluator.v2

import id.pineapple.expression.evaluator.v1
import id.pineapple.expression.evaluator.v2

trait SimpleEvaluator extends v1.SimpleEvaluator with v2.Evaluator[BigInt] {
	override def uOp(op: String, a: BigInt): BigInt = op match {
		case "+" => a
		case "-" => -a
		case _ => throw new IllegalArgumentException(s"Invalid unary operator $op")
	}
}
