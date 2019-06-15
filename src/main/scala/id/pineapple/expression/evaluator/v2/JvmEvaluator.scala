package id.pineapple.expression.evaluator.v2

import id.pineapple.expression.evaluator.v1
import id.pineapple.expression.evaluator.v2
import org.objectweb.asm.{MethodVisitor, Opcodes}

trait JvmEvaluator extends v1.JvmEvaluator with v2.Evaluator[v1.JvmEvaluator.CodeGenerator] {
	override def uOp(op: String, a: v1.JvmEvaluator.CodeGenerator): v1.JvmEvaluator.CodeGenerator = (visitor: MethodVisitor) => {
		val result = a.generate(visitor)
		op match {
			case "+" => // Do nothing
			case "-" => visitor.visitInsn(Opcodes.INEG)
			case _ => throw new IllegalArgumentException(s"Invalid unary operator $op")
		}
		result
	}
}
