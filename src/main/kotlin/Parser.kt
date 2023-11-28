import Lexer.Token
import Lexer.TokenType
import Node.Expression.UnaryExpression
import Node.Expression

class Parser private constructor(private val lexer: Lexer) {

    private lateinit var curToken: Token
    private var peekToken: Token = Token(TokenType.ILLEGAL, "")//haven't handled much errors here

    lateinit var finalExpression: Expression

    companion object {
        fun newInstance(lexer: Lexer): Parser {
            return Parser(lexer).apply {
                // Read two tokens, so curToken and peekToken are both set
                nextToken()
                nextToken()

                val leftToken = curToken//this can be in the recursion Im sure of it
                nextToken()
                val midToken = curToken
                nextToken()
                if(curTokenIs(TokenType.EOF)){
                    finalExpression = parseExpression(leftToken,midToken)
                }else{
                    finalExpression = parseExpression(leftToken,midToken,curToken)
                    nextToken()
                    parseRightExpression()

                }
                finalExpression
            }
        }
    }

    fun nextToken() {
        curToken = peekToken
        peekToken = lexer.nextToken()
    }

    private fun parseExpression(vararg tok: Token): Expression {
//       println(tok[0].toString()+tok[1].toString()+tok[2].toString())
        return when (tok.size){
            3 -> Expression.BinaryExpression(tokenToOperator[tok[1].type] as BinaryOperator,tokToConstant(tok[0]),tokToConstant(tok[2])) //need to cast it because of potential null
            2 -> UnaryExpression(UnaryOperator.LOGICAL_NOT,tokToConstant(tok[1]))
            1 -> tokToConstant(tok[0])
            else -> tokToConstant(tok[0]) //not sure how to handle this error
        }
    }

    private fun tokToConstant(tok: Token): Expression {
        return if (tok.type == TokenType.Num) {
            numToConst(tok)
        } else if(tok.type == TokenType.MINUS || tok.type == TokenType.NOT) {//this is messy
            val tempToken = curToken
            nextToken()
            numToConst(tempToken, -1)
        } else {
            boolToConst(tok)
        }
    }

    private fun numToConst(tok: Token, nagative: Int = 1): Expression{
        val num = tok.text.toInt() * nagative
        return Expression.Constant(LiteralValue.IntValue(num))
    }

    private fun boolToConst(tok: Token): Expression{
        var value = LiteralValue.BooleanValue(false)
        if(tok.type == TokenType.True) {
            value = LiteralValue.BooleanValue(true)
        }
        return Expression.Constant(value)
    }

    fun parseRightExpression() {
        if(curTokenIs(TokenType.EOF)) return
        val leftToken = curToken
        nextToken()
        val midToken = curToken
        nextToken()
//        println(leftToken.toString()+midToken.toString()+curToken.toString())
        finalExpression= Expression.BinaryExpression(tokenToOperator[leftToken.type] as BinaryOperator,finalExpression,tokToConstant(midToken))

         if(!curTokenIs(TokenType.EOF)){
             parseRightExpression()
        }
    }

    private fun curTokenIs(t: TokenType): Boolean = curToken.type == t

    val tokenToOperator = mapOf(
        TokenType.TIMES to BinaryOperator.MULTIPLICATION,
        TokenType.LT to BinaryOperator.LESS_THAN,
        TokenType.GT to BinaryOperator.GREATER_THAN,
        TokenType.DIVIDE to BinaryOperator.DIVISION,
        TokenType.MINUS to BinaryOperator.SUBTRACTION,
        TokenType.PLUS to BinaryOperator.ADDITION ,
        TokenType.NOT_EQ to BinaryOperator.NOT_EQUAL_TO,
        TokenType.EQ to BinaryOperator.EQUAL_TO,
        TokenType.OR to BinaryOperator.LOGICAL_OR
    )
}