package id.pineapple.expression.evaluator.v1

import id.pineapple.expression.evaluator.lexer.Token

trait Parser {
	protected val BINARY_OPERATORS: Vector[Set[String]] = Vector(
		Set("+", "-"),
		Set("*", "/", "%")
	)
	
	def parseIntegerLiteral[R](start: Token)(implicit evaluator: Evaluator[R]): (R, Token) = start match {
		case i: Token.Integer =>
			try {
				(evaluator.int(BigInt(i.text)), start)
			} catch {
				case _: IllegalArgumentException =>
					throw new IllegalArgumentException(s"Invalid number format ${start.text} at ${start.locationStr}")
			}
		case _ => throw new IllegalArgumentException(s"Integer expected at ${start.locationStr}")
	}
	
	def parseBinaryOperator[R](start: Token)(implicit evaluator: Evaluator[R]): (R, Token) =
		parseBinaryOperator(start, 0)
	
	protected def parseBinaryOperator[R](start: Token, level: Int)(implicit evaluator: Evaluator[R]): (R, Token) = {
		if (level < BINARY_OPERATORS.length) {
			var (left, leftStop) = parseBinaryOperator(start, level + 1)
			while (
				leftStop.next match {
					case operator: Token.Operator if BINARY_OPERATORS(level).contains(operator.text) =>
						val (right, rightStop) = parseBinaryOperator(operator.next, level + 1)
						left = evaluator.binOp(operator.text, left, right)
						leftStop = rightStop
						true
					case _ => false
				}
			) {}
			(left, leftStop)
		} else
			parseIntegerLiteral(start)
	}
	
	def parseExpression[R](start: Token)(implicit evaluator: Evaluator[R]): (R, Token) =
		parseBinaryOperator(start)
}
