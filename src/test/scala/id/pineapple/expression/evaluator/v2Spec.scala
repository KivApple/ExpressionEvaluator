package id.pineapple.expression.evaluator

import org.scalatest._
import id.pineapple.expression.evaluator.lexer.SourceText
import id.pineapple.expression.evaluator.v2.{Parser, PrintEvaluator, SimpleEvaluator}

class v2Spec extends FlatSpec {
	"A PrintEvaluator" should "correctly process expression -2 + 2 * 2" in {
		val e = new PrintEvaluator {}
		assert(
			e.binOp(
				"+",
				e.uOp(
					"-",
					e.int(BigInt(2))
				),
				e.binOp(
					"*",
					e.int(BigInt(2)),
					e.int(BigInt(2))
				)
			) == "(-2 + (2 * 2))"
		)
	}
	"A Parser and PrintEvaluator" should "correctly process expression (-2 + 2) * 2" in {
		implicit val printEvaluator: PrintEvaluator = new PrintEvaluator {}
		
		assert(new Parser {}.parseExpression(SourceText("(-2 + 2) * 2", "test.txt").headToken)._1 == "((-2 + 2) * 2)")
		assert(new Parser {}.parseExpression(SourceText("(-2+2)*2", "test.txt").headToken)._1 == "((-2 + 2) * 2)")
		assert(new Parser {}.parseExpression(SourceText("(-2 +2) *2", "test.txt").headToken)._1 == "((-2 + 2) * 2)")
		assert(new Parser {}.parseExpression(SourceText("\t (-2 + 2) * 2", "test.txt").headToken)._1 == "((-2 + 2) * 2)")
		assert(new Parser {}.parseExpression(SourceText("(-2 + 2) * 2 ", "test.txt").headToken)._1 == "((-2 + 2) * 2)")
		
		assert(new Parser {}.parseExpression(SourceText("(-2 + 2) * 2", "test.txt").headToken)._2.stop.char == 13)
		assert(new Parser {}.parseExpression(SourceText("(-2 + 2) * 2 ", "test.txt").headToken)._2.stop.char == 13)
	}
	"A SimpleEvaluator" should "correctly process expression 2 + 2 * 2" in {
		val e = new SimpleEvaluator {}
		assert(
			e.binOp(
				"+",
				e.int(BigInt(2)),
				e.binOp(
					"*",
					e.int(BigInt(2)),
					e.int(BigInt(2))
				)
			) == 6
		)
	}
	"A SimpleEvaluator" should "correctly process expression 2 / 3 - 3 % 2" in {
		val e = new SimpleEvaluator {}
		assert(
			e.binOp(
				"-",
				e.binOp(
					"/",
					e.int(BigInt(2)),
					e.int(BigInt(3))
				),
				e.binOp(
					"%",
					e.int(BigInt(3)),
					e.int(BigInt(2))
				)
			) == -1
		)
	}
	"A SimpleEvaluator" should "correctly process expression (+2 - 1) * -2" in {
		val e = new SimpleEvaluator {}
		assert(
			e.binOp(
				"*",
				e.binOp(
					"-",
					e.uOp("+", BigInt(2)),
					e.int(BigInt(1))
				),
				e.uOp(
					"-",
					BigInt(2)
				)
			) == -2
		)
	}
}
