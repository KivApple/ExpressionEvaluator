package id.pineapple.expression.evaluator.lexer

import scala.annotation.tailrec

sealed trait Token {
	val text: String
	val start: SourceChar
	val stop: SourceChar
	lazy val next: Token = Token.read(stop)
	
	override def toString: String = s"$locationStr ${getClass.getSimpleName} $text"
	
	def locationStr: String = {
		if (start.line == stop.line)
			if (start.char + 1 == stop.char)
				s"${start.source.fileName}:${start.line}:${start.char}"
			else
				s"${start.source.fileName}:${start.line}:${start.char}-${stop.char - 1}"
		else
			s"${start.source.fileName}:${start.line}:${start.char}-${stop.line}:${stop.char - 1}"
	}
}

object Token {
	final case class End(start: SourceChar) extends Token {
		override val text: String = ""
		override val stop: SourceChar = start.next
	}
	final case class Integer(text: String, start: SourceChar, stop: SourceChar) extends Token
	final case class Name(text: String, start: SourceChar, stop: SourceChar) extends Token
	final case class Operator(text: String, start: SourceChar, stop: SourceChar) extends Token
	final case class Invalid(start: SourceChar) extends Token {
		override val text: String = start.value.toString
		override val stop: SourceChar = start.next
	}
	
	private val DIGITS = InRange('0', '9')
	private val CAPITAL_LETTERS = InRange('A', 'Z')
	private val REGULAR_LETTERS = InRange('a', 'z')
	private val LETTERS = CAPITAL_LETTERS or REGULAR_LETTERS
	private val LETTERS_OR_DIGITS = LETTERS or DIGITS
	private val OPERATORS = new StringSearchTree(
		"::", "++", "--", "(", ")", "[", "]", ".", "->", "+", "-", "!", "~", "*", "&", ".*", "->*",
		"/", "%", "<<", ">>", "<", "<=", ">", ">=", "==", "!=", "^", "|", "&&", "||", "?", ":", "=", "+=", "-=",
		"*=", "/=", "%=", "<<=", ">>=", "&=", "^=", "|=", ","
	)
	
	@tailrec private def skipSpace(cur: SourceChar): SourceChar = cur.value match {
		case ' ' | '\t' | '\r' | '\n' => skipSpace(cur.next)
		case _ => cur
	}
	
	@tailrec private def readNameOrNumber(cur: SourceChar, text: String = ""): (SourceChar, String) =
		if (LETTERS_OR_DIGITS.unapply(cur.value))
			readNameOrNumber(cur.next, text + cur.value)
		else
			(cur, text)
	
	@tailrec private def readOperator(start: SourceChar, cur: SourceChar,
									  tree: StringSearchTree, lastValidChar: Option[SourceChar] = Option.empty,
									  lastValidText: Option[String] = Option.empty,
									  text: String = ""): (SourceChar, Option[String]) = {
		val nextTree = tree.search(cur.value)
		if (nextTree.isDefined) {
			val nextText = text + cur.value
			readOperator(start, cur.next, nextTree.get,
				if (nextTree.get.isTerminal) Option(cur) else lastValidChar,
				if (nextTree.get.isTerminal) Option(nextText) else lastValidText,
				nextText)
		} else {
			(if (lastValidChar.isDefined) lastValidChar.get.next else start, lastValidText)
		}
	}
	
	def read(start: SourceChar): Token = {
		val realStart = skipSpace(start)
		if (realStart.valid) {
			realStart.value match {
				case DIGITS() =>
					val (stop, text) = readNameOrNumber(realStart)
					Token.Integer(text, realStart, stop)
				case LETTERS() =>
					val (stop, text) = readNameOrNumber(realStart)
					Token.Name(text, realStart, stop)
				case _ =>
					val (stop, text) = readOperator(realStart, realStart, OPERATORS)
					if (text.isDefined)
						Token.Operator(text.get, realStart, stop)
					else
						Token.Invalid(realStart)
			}
		} else
			Token.End(realStart)
	}
}
