package id.pineapple.expression.evaluator.v1

import id.pineapple.expression.evaluator.v1.JvmEvaluator.CodeGenerator
import org.objectweb.asm.{ClassWriter, MethodVisitor, Opcodes}

trait JvmEvaluator extends Evaluator[CodeGenerator] {
	override def int(i: BigInt): CodeGenerator = (visitor: MethodVisitor) => {
		visitor.visitLdcInsn(i.toInt)
		1
	}
	
	override def binOp(op: String, a: CodeGenerator, b: CodeGenerator): CodeGenerator = (visitor: MethodVisitor) => {
		val result = math.max(a.generate(visitor), b.generate(visitor))
		visitor.visitInsn(op match {
			case "+" => Opcodes.IADD
			case "-" => Opcodes.ISUB
			case "*" => Opcodes.IMUL
			case "/" => Opcodes.IDIV
			case "%" => Opcodes.IREM
			case _ => throw new IllegalArgumentException(s"Invalid binary operator $op")
		})
		result
	}
}

object JvmEvaluator {
	trait CodeGenerator {
		def generate(visitor: MethodVisitor): Int
		
		def generate(): GeneratedEvaluator = {
			val classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS)
			classWriter.visit(Opcodes.V1_6, Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL,
				"EvaluatorImpl", null, "java/lang/Object",
				Array("id/pineapple/expression/evaluator/v1/JvmEvaluator$GeneratedEvaluator"))
			val ctorVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC,
				"<init>", "()V", null, null)
			ctorVisitor.visitCode()
			ctorVisitor.visitVarInsn(Opcodes.ALOAD, 0)
			ctorVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object",
				"<init>", "()V", false)
			ctorVisitor.visitInsn(Opcodes.RETURN)
			ctorVisitor.visitMaxs(1, 1)
			ctorVisitor.visitEnd()
			val methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC,
				"evaluate", "()I", null, null)
			methodVisitor.visitCode()
			val stackDepth = generate(methodVisitor)
			methodVisitor.visitInsn(Opcodes.IRETURN)
			methodVisitor.visitMaxs(stackDepth, 1)
			methodVisitor.visitEnd()
			val loader = new DynamicClassLoader()
			val cls = loader.defineClass("EvaluatorImpl", classWriter.toByteArray)
			cls.newInstance()
		}
		
		def execute(): Int = generate().evaluate()
	}
	
	trait GeneratedEvaluator {
		def evaluate(): Int
	}
	
	class DynamicClassLoader extends ClassLoader {
		def defineClass(name: String, data: Array[Byte]): Class[GeneratedEvaluator] =
			defineClass(name, data, 0, data.length).asInstanceOf[Class[GeneratedEvaluator]]
	}
}
