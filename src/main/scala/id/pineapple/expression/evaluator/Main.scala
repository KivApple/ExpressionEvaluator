package id.pineapple.expression.evaluator

import scala.io.StdIn.readLine
import id.pineapple.expression.evaluator.lexer.SourceText

object Main extends App {
	try {
		println("Integer expression evaluator")
		println("Info:")
		println("\tv1 parser - supports only binary operators + - * / %")
		println("\tv2 parser - also supports unary operators + - and braces")
		readLine("Please select mode (0 - only dump tokens, 1 - v1 parser, 2 - v2 parser): ") match {
			case "0" =>
				val source = SourceText(readLine("Please enter expression: "), "stdin")
				for (token <- source) {
					println(token)
				}
			case "1" =>
				val source = SourceText(readLine("Please enter expression: "), "stdin")
				val ast = new v1.Parser {}.parseExpression(source.headToken)(new v1.WrappedEvaluator {})._1
				println(s"PrintEvaluator: ${ast.value(new v1.PrintEvaluator {})}")
				println(s"SimpleEvaluator: ${ast.value(new v1.SimpleEvaluator {})}")
				println(s"JvmEvaluator: ${ast.value(new v1.JvmEvaluator {}).execute()}")
			case "2" =>
				val source = SourceText(readLine("Please enter expression: "), "stdin")
				val ast = new v2.Parser {}.parseExpression(source.headToken)(new v2.WrappedEvaluator {}.asInstanceOf[v1.WrappedEvaluator])._1
				println(s"PrintEvaluator: ${ast.value(new v2.PrintEvaluator {})}")
				println(s"SimpleEvaluator: ${ast.value(new v2.SimpleEvaluator {})}")
				println(s"JvmEvaluator: ${ast.value(new v2.JvmEvaluator {}).execute()}")
			case _ => println("Error: you can enter only 0, 1 or 2")
		}
	} catch {
		case e: IllegalArgumentException => println(s"IllegalArgumentException: ${e.getMessage}")
	}
}
