# ExpressionEvaluator
A simple calculator written in Scala in the final tagless style.
This project was created primarily for educational reasons and 
has no direct practical value.

| Package                                   | Contents                                                                                                           |
|-------------------------------------------|--------------------------------------------------------------------------------------------------------------------|
| `id.pineapple.expression.evaluator`       | The main package of the project                                                                                    |
| `id.pineapple.expression.evaluator.lexer` | Source text container and tokenizer                                                                                |
| `id.pineapple.expression.evaluator.v1`    | Basic version of parser and evaluators. Supports integer numbers and five binary operators (+ - * / %)             |
| `id.pineapple.expression.evaluator.v2`    | Advanced version of parser and evaluators based on basic version. Added support for unary plus and minus and braces |

This project shows two kinds of extensibility of the final tagless style:

* You can write multiple evaluators for using with the same parser
* You can inherit existing parser or evaluator to implement new features (v2 inherit everything from v1)

Both parsers are simple recurrent descending analyzers.

There is four kinds of evaluators:

* `PrintEvaluator` just prints parsed expression
* `SimpleEvaluator` evaluates expression result using Scala `BigInt`
* `JvmEvaluator` generates JVM bytecode which evaluates expression using Java `int`
* `WrappedEvaluator` builds Abstract Syntax Tree (AST) to allow you run multiple another evaluators later without re-parsing input text

Object `id.pineapple.expression.evaluator.Main` and the tests directory contain comprehensive usage examples of all parsers and evaluators.
