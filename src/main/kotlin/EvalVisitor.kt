import Node.Expression.BinaryExpression
import Node.Expression.Constant
import Node.Expression.UnaryExpression
import Node.Conditional
import Node.Expression

class EvalVisitor : Visitor<LiteralValue> {
    override fun visitConstant(constantExpr: Constant): LiteralValue {
        return constantExpr.value
    }

    override fun visitUnaryExpr(unaryExpression: UnaryExpression): LiteralValue {
        return when (unaryExpression.operand){
            is BinaryExpression -> visitUnaryExpr(turnIntoUnary(visitBinaryExpr(unaryExpression.operand)))
            is Constant -> {
               when (unaryExpression.operand.value){
                   is LiteralValue.IntValue -> LiteralValue.IntValue(unaryExpression.operand.value.value * -1 )
                   is LiteralValue.BooleanValue -> LiteralValue.BooleanValue(unaryExpression.operand.value.value.not())
                   else -> LiteralValue.Error()
               }
            }
            is UnaryExpression -> visitUnaryExpr(turnIntoUnary(visitUnaryExpr(unaryExpression.operand)))
        }
    }

    override fun visitBinaryExpr(binaryExpression: BinaryExpression): LiteralValue {
        return when (binaryExpression.operator){
            BinaryOperator.ADDITION -> processFunction(binaryExpression, ::adding)
            BinaryOperator.SUBTRACTION -> processFunction(binaryExpression, ::takingAway)
            BinaryOperator.MULTIPLICATION -> processFunction(binaryExpression, ::multiplying)
            BinaryOperator.DIVISION -> processFunction(binaryExpression, ::dividing)
            BinaryOperator.EQUAL_TO -> processFunction(binaryExpression, ::equal)
            BinaryOperator.NOT_EQUAL_TO -> processFunction(binaryExpression, ::notEqual)
            BinaryOperator.LESS_THAN -> processFunction(binaryExpression, ::lessThan)
            BinaryOperator.GREATER_THAN -> processFunction(binaryExpression, ::moreThan)
            BinaryOperator.LOGICAL_AND -> processFunction(binaryExpression, ::adding)
            BinaryOperator.LOGICAL_OR -> processFunction(binaryExpression, ::or)
        }
    }

    override fun visitConditional(conditional: Conditional): LiteralValue {
        val condition = when (conditional.guard){
            is BinaryExpression -> visitBinaryExpr(conditional.guard)
            is Constant ->  when (conditional.guard.value){
                is LiteralValue.IntValue -> LiteralValue.BooleanValue(conditional.guard.value.value > 0)
                is LiteralValue.BooleanValue -> conditional.guard.value
                else -> LiteralValue.Error()
            }
            is UnaryExpression -> visitUnaryExpr(conditional.guard)
        }
        return if (condition is LiteralValue.BooleanValue) {
            val a = condition.value
            if (a) visit(conditional.trueBranch) else visit(conditional.falseBranch)
        } else LiteralValue.Error()
    }

    override fun visit(node: Node): LiteralValue {
        return when(node){
            is Conditional -> visitConditional(node)
            is BinaryExpression -> visitBinaryExpr(node)
            is Constant -> node.value
            is UnaryExpression -> visitUnaryExpr(node)
        }
    }

    private fun processFunction(binaryExpression: BinaryExpression, f: (LiteralValue, LiteralValue)-> LiteralValue) : LiteralValue {
        val a = when (binaryExpression.left) {
            is BinaryExpression -> visitBinaryExpr(binaryExpression.left)
            is Constant ->  binaryExpression.left.value
            is UnaryExpression -> visitUnaryExpr(binaryExpression.left)
        }
        val b = when (binaryExpression.right) {
            is BinaryExpression -> visitBinaryExpr(binaryExpression.right)
            is Constant -> binaryExpression.right.value
            is UnaryExpression -> visitUnaryExpr(binaryExpression.right)
        }
        return f.invoke(a,b)
    }

    private fun adding(a: LiteralValue, b: LiteralValue) :LiteralValue{
        if (b is LiteralValue.Error) return LiteralValue.Error()
        return when (a){
            is LiteralValue.IntValue -> LiteralValue.IntValue(a.value + (b as LiteralValue.IntValue).value)
            is LiteralValue.BooleanValue -> LiteralValue.BooleanValue(a.value && (b as LiteralValue.BooleanValue).value)
            else -> LiteralValue.Error()
        }
    }

    private fun or(a: LiteralValue, b: LiteralValue) :LiteralValue{
        if (b is LiteralValue.Error) return LiteralValue.Error()
        return when (a){
            is LiteralValue.IntValue -> LiteralValue.Error()
            is LiteralValue.BooleanValue -> LiteralValue.BooleanValue(a.value || (b as LiteralValue.BooleanValue).value)
            else -> LiteralValue.Error()
        }
    }

    private fun equal(a: LiteralValue, b: LiteralValue) :LiteralValue{
        return LiteralValue.BooleanValue(a == b)
    }

    private fun notEqual(a: LiteralValue, b: LiteralValue) :LiteralValue{
        return LiteralValue.BooleanValue(a != b)
    }

    private fun takingAway(a: LiteralValue, b: LiteralValue) :LiteralValue{
        if (b is LiteralValue.Error) return LiteralValue.Error()
        return when (a){
            is LiteralValue.IntValue -> LiteralValue.IntValue(a.value - (b as LiteralValue.IntValue).value)
            is LiteralValue.BooleanValue -> LiteralValue.Error()
            else -> LiteralValue.Error()
        }
    }

    private fun multiplying(a: LiteralValue, b: LiteralValue) :LiteralValue{
        if (b is LiteralValue.Error) return LiteralValue.Error()
        return when (a){
            is LiteralValue.IntValue -> LiteralValue.IntValue(a.value * (b as LiteralValue.IntValue).value)
            is LiteralValue.BooleanValue -> LiteralValue.Error()
            else -> LiteralValue.Error()
        }
    }

    private fun dividing(a: LiteralValue, b: LiteralValue) :LiteralValue{
        if (b is LiteralValue.Error) return LiteralValue.Error()
        return when (a){
            is LiteralValue.IntValue -> LiteralValue.IntValue(a.value / (b as LiteralValue.IntValue).value)
            is LiteralValue.BooleanValue -> LiteralValue.Error()
            else -> LiteralValue.Error()
        }
    }

    private fun lessThan(a: LiteralValue, b: LiteralValue) :LiteralValue{
        if (b is LiteralValue.Error) return LiteralValue.Error()
        return when (a){
            is LiteralValue.IntValue -> LiteralValue.BooleanValue(a.value < (b as LiteralValue.IntValue).value)
            is LiteralValue.BooleanValue -> LiteralValue.Error()
            else -> LiteralValue.Error()
        }
    }

    private fun moreThan(a: LiteralValue, b: LiteralValue) :LiteralValue{
        if (b is LiteralValue.Error) return LiteralValue.Error()
        return when (a){
            is LiteralValue.IntValue -> LiteralValue.BooleanValue(a.value > (b as LiteralValue.IntValue).value)
            is LiteralValue.BooleanValue -> LiteralValue.Error()
            else -> LiteralValue.Error()
        }
    }

    private fun turnIntoUnary(literalValue: LiteralValue): UnaryExpression{
        return UnaryExpression(operand = Constant(literalValue), operator = UnaryOperator.NEGATION)
    }

}
