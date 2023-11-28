import Node.*

interface Visitor<R> {

    fun visitConstant(constantExpr: Expression.Constant): R

    fun visitUnaryExpr(unaryExpression: Expression.UnaryExpression): R

    fun visitBinaryExpr(binaryExpression: Expression.BinaryExpression): R

    // For simplicity, it is assumed that the conditional ultimately evaluates to one of the two defined Literal Values
    fun visitConditional(conditional: Conditional): R

    fun visit(node: Node): R
}