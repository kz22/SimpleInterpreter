/*
 The root class encompassing all types of nodes
 */
sealed class Node {

    sealed class Expression : Node() {
    // Expression encapsulates various expression-related nodes
        data class Constant(val value: LiteralValue) : Expression()

        data class UnaryExpression(
            val operator: UnaryOperator,
            val operand: Expression
        ) : Expression()

        data class BinaryExpression(
            val operator: BinaryOperator,
            val left: Expression,
            val right: Expression
        ) : Expression()
    }

    class Conditional(
        val guard: Expression,
        val trueBranch: Node,
        val falseBranch: Node
    ) : Node()

}
