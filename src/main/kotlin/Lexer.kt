class Lexer private constructor(private val input: String) {

    private var position = 0
    private var readPosition = 0
    private var ch: Char = 0.toChar()

    companion object {
        fun newInstance(input: String): Lexer {
            val l = Lexer(input)
            l.readChar()
            return l
        }
    }


    fun nextToken(): Token {
        skipWhitespace()

        val tok = when (ch) {
            '=' -> newToken(TokenType.EQ, ch)
            '+' -> newToken(TokenType.PLUS, ch)
            '&' -> newToken(TokenType.PLUS, ch)
            '-' -> newToken(TokenType.MINUS, ch)
            '!' -> if (peekChar() == '=') {
                val ex = ch
                readChar()
                val literal = ex + ch.toString()
                Token(TokenType.NOT_EQ, literal)
            } else {
                newToken(TokenType.NOT, ch)
            }
            '/' -> newToken(TokenType.DIVIDE, ch)
            '*' -> newToken(TokenType.TIMES, ch)
            '<' -> newToken(TokenType.LT, ch)
            '>' -> newToken(TokenType.GT, ch)
            ':' -> newToken(TokenType.DIVIDE, ch)
            '|' -> newToken(TokenType.OR, ch)
            '{' -> newToken(TokenType.LPAREN, ch)
            '}' -> newToken(TokenType.RPAREN, ch)
            '(' -> newToken(TokenType.LPAREN, ch)
            ')' -> newToken(TokenType.RPAREN, ch)
            '[' -> newToken(TokenType.LPAREN, ch)
            ']' -> newToken(TokenType.RPAREN, ch)
            'T' -> newToken(TokenType.True, ch)
            'F' -> newToken(TokenType.False, ch)
            0.toChar() -> Token(TokenType.EOF, "")
            else -> when {
                isDigit(ch) -> return Token(TokenType.Num, readNumber())
                else -> newToken(TokenType.ILLEGAL, ch)
            }
        }

        readChar()
        return tok
    }

    fun readChar() {
        ch = if (readPosition >= input.length) {
            0.toChar()
        } else {
            input[readPosition]
        }
        position = readPosition
        readPosition += 1
    }

    private fun peekChar(): Char {
        return if (readPosition >= input.length) {
            0.toChar()
        } else {
            input[readPosition]
        }
    }

    private fun readNumber(): String {
        val pos = position
        while (isDigit(ch)) {
            readChar()
        }
        return input.substring(pos until position)
    }

    private fun skipWhitespace() {
        while (ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r') {
            readChar()
        }
    }

    private fun isDigit(ch: Char): Boolean {
        return ch in '0'..'9'
    }

    data class Token(
        val type: TokenType,
        val text: String
    )

    enum class TokenType {
        Num,
        PLUS,
        MINUS,
        EQ,
        OR,
        NOT_EQ,
        NOT,
        DIVIDE,
        TIMES,
        LPAREN,
        RPAREN,
        GT,
        LT,
        True,
        False,
        ILLEGAL,
        EOF,
    }
    private fun newToken(tokenType: TokenType, ch: Char) = Token(tokenType, ch.toString())

}