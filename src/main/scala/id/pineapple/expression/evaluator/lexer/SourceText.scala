package id.pineapple.expression.evaluator.lexer

import scala.annotation.tailrec

final case class SourceText(text: String, fileName: String = "<unknown>") {
	val validOffsetRange = InRange(0, text.length - 1)
	
	def headChar: SourceChar = SourceChar(this)
	
	def headToken: Token = Token.read(headChar)
	
	@tailrec private def foreachHelper[B](f: Token => B, token: Token): Unit = {
		f(token)
		if (!token.isInstanceOf[Token.End]) foreachHelper(f, token.next)
	}
	
	def foreach[B](f: Token => B): Unit = foreachHelper(f, headToken)
}
