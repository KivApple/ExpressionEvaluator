package id.pineapple.expression.evaluator

import org.scalatest._
import id.pineapple.expression.evaluator.lexer.{SourceText, StringSearchTree, Token}

class LexerSpec extends FlatSpec {
	"A StringSearchTree" should "search strings char by char" in {
		val tree = new StringSearchTree("+", "-", "++")
		
		assert(!tree.isTerminal)
		
		assert(tree.search('+').isDefined)
		assert(tree.search('+').get.isTerminal)
		assert(tree.search('+').get.search('+').isDefined)
		assert(tree.search('+').get.search('+').get.isTerminal)
		assert(tree.search('+').get.search('+').get.search('+').isEmpty)
		
		assert(tree.search('-').isDefined)
		assert(tree.search('-').get.isTerminal)
		assert(tree.search('-').get.search('-').isEmpty)
		
		assert(tree.search('*').isEmpty)
	}
	"A SourceText" should "output SourceChar one by one with proper location information" in {
		val source = SourceText("12\n3", "test.txt")
		
		assert(source.headChar.valid)
		assert(source.headChar.line == 1)
		assert(source.headChar.char == 1)
		assert(source.headChar.value == '1')
		
		assert(source.headChar.next.valid)
		assert(source.headChar.next.line == 1)
		assert(source.headChar.next.char == 2)
		assert(source.headChar.next.value == '2')
		
		assert(source.headChar.next.next.valid)
		assert(source.headChar.next.next.line == 1)
		assert(source.headChar.next.next.char == 3)
		assert(source.headChar.next.next.value == '\n')
		
		assert(source.headChar.next.next.next.valid)
		assert(source.headChar.next.next.next.line == 2)
		assert(source.headChar.next.next.next.char == 1)
		assert(source.headChar.next.next.next.value == '3')
		
		assert(!source.headChar.next.next.next.next.valid)
		assert(!source.headChar.next.next.next.next.next.valid)
		assert(!source.headChar.next.next.next.next.next.next.valid)
	}
	it should "output tokens one by one with proper location information" in {
		val source = SourceText("1 12 a ab + ++")
		
		assert(source.headToken.isInstanceOf[Token.Integer])
		assert(source.headToken.text == "1")
		assert(source.headToken.start.char == 1)
		assert(source.headToken.stop.char == 2)
		
		assert(source.headToken.next.isInstanceOf[Token.Integer])
		assert(source.headToken.next.text == "12")
		assert(source.headToken.next.start.char == 3)
		assert(source.headToken.next.stop.char == 5)
		
		assert(source.headToken.next.next.isInstanceOf[Token.Name])
		assert(source.headToken.next.next.text == "a")
		assert(source.headToken.next.next.start.char == 6)
		assert(source.headToken.next.next.stop.char == 7)
		
		assert(source.headToken.next.next.next.isInstanceOf[Token.Name])
		assert(source.headToken.next.next.next.text == "ab")
		assert(source.headToken.next.next.next.start.char == 8)
		assert(source.headToken.next.next.next.stop.char == 10)
		
		assert(source.headToken.next.next.next.next.isInstanceOf[Token.Operator])
		assert(source.headToken.next.next.next.next.text == "+")
		assert(source.headToken.next.next.next.next.start.char == 11)
		assert(source.headToken.next.next.next.next.stop.char == 12)
		
		assert(source.headToken.next.next.next.next.next.isInstanceOf[Token.Operator])
		assert(source.headToken.next.next.next.next.next.text == "++")
		assert(source.headToken.next.next.next.next.next.start.char == 13)
		assert(source.headToken.next.next.next.next.next.stop.char == 15)
		
		assert(source.headToken.next.next.next.next.next.next.isInstanceOf[Token.End])
		assert(source.headToken.next.next.next.next.next.next.start.char == 15)
		assert(source.headToken.next.next.next.next.next.next.stop.char == 16)
		
		assert(source.headToken.next.next.next.next.next.next.next.isInstanceOf[Token.End])
		assert(source.headToken.next.next.next.next.next.next.next.next.isInstanceOf[Token.End])
	}
}
