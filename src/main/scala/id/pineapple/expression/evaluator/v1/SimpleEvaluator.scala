package id.pineapple.expression.evaluator.v1

trait SimpleEvaluator extends Evaluator[BigInt] {
	override def int(e: BigInt): BigInt = e
	
	override def binOp(op: String, a: BigInt, b: BigInt): BigInt = op match {
		case "+" => a + b
		case "-" => a - b
		case "*" => a * b
		case "/" => a / b
		case "%" => a % b
		case _ => throw new IllegalArgumentException(s"Invalid binary operator $op")
	}
}
