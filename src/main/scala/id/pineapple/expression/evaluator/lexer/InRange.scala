package id.pineapple.expression.evaluator.lexer

final case class InRange[A](from: A, to: A)(implicit ordering: Ordering[A]) {
	import ordering._
	
	def unapply(arg: A): Boolean = from <= arg && arg < to
	
	def or(o: InRange[A]) = InMultiRange(List(this, o))
	
	def or(o: InMultiRange[A]) = InMultiRange(List(this) ++ o.ranges)
}

final case class InMultiRange[A](ranges: List[InRange[A]]) {
	def unapply(arg: A): Boolean = ranges.exists(x => x.unapply(arg))
	
	def or(o: InMultiRange[A]) = InMultiRange(ranges ++ o.ranges)
	
	def or(o: InRange[A]) = InMultiRange(ranges ++ List(o))
}
