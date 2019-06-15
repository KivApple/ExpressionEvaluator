package id.pineapple.expression.evaluator.lexer

final case class SourceChar(source: SourceText, offset: Int = 0, line: Int = 1, char: Int = 1) {
	def valid: Boolean = offset < source.text.length
	
	def value: Char =
		if (valid)
			source.text.charAt(offset)
		else
			'\0'
	
	lazy val next: SourceChar = {
		val newLine = value == '\n'
		val nextLine = if (newLine) line + 1 else line
		val nextChar = if (newLine) 1 else char + 1
		SourceChar(source, offset + 1, nextLine, nextChar)
	}
	
	override def toString: String = s"${source.fileName}:$line:$char $value"
}
