package id.pineapple.expression.evaluator.v2

import id.pineapple.expression.evaluator.lexer.Token
import id.pineapple.expression.evaluator.v1

trait Parser extends v1.Parser {
	def parseUnaryOperator[R](start: Token)(implicit evaluator: Evaluator[R]): (R, Token) =
		if (start.isInstanceOf[Token.Operator]) {
			if (start.text == "(") {
				val (arg, argStop) = parseExpression(start.next)
				val secondBracket = argStop.next
				if (!secondBracket.isInstanceOf[Token.Operator] || secondBracket.text != ")")
					throw new IllegalArgumentException(s"Closing bracket expected at ${secondBracket.locationStr}")
				(arg, secondBracket)
			} else {
				val (arg, argStop) = parseUnaryOperator(start.next)
				(evaluator.uOp(start.text, arg), argStop)
			}
		} else
			parseIntegerLiteral(start)
	
	override def parseBinaryOperator[R](start: Token, level: Int)(implicit evaluator: v1.Evaluator[R]): (R, Token) =
		if (level < BINARY_OPERATORS.length)
			super.parseBinaryOperator(start, level)
		else evaluator match {
			case e2: Evaluator[R] => parseUnaryOperator(start)(e2)
			case _ => super.parseBinaryOperator(start, level)
		}
}
