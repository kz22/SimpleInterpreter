class Interpreter private constructor() {

    companion object {
        fun newInstance(parser: Parser): String {
            return when (val result = EvalVisitor().visit(parser.finalExpression)) {
                is LiteralValue.IntValue -> result.value.toString()
                is LiteralValue.BooleanValue -> result.value.toString()
                else -> "Error"
            }
        }
    }
}