package id.pineapple.expression.evaluator.lexer

final class StringSearchTree(values: Seq[String], offset: Int = 0) {
	private val children: Map[Char, StringSearchTree] = values
  	  .filter(value => value.length > offset)
	  .groupBy(value => value.charAt(offset))
	  .mapValues(value => new StringSearchTree(value, offset + 1))
	val isTerminal: Boolean = values.exists(value => value.length == offset)
	
	def this(values: String*) {
		this(values)
	}
	
	def search(key: Char): Option[StringSearchTree] = children.get(key)
}
