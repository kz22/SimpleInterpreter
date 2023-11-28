import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertThrows

class EvalVisitorTest {

    @Test
    fun `simple addition`() {
        val input = "2 + 5"
        assertEquals(evalTest(input), "7")
    }

    @Test
    fun `simple boolean`() {
        val input = "T + F"
        assertEquals(evalTest(input), "false")
    }

    @Test
    fun `simple greater than`() {
        val input = "1 > 3"
        assertEquals(evalTest(input), "false")
    }

    @Test
    fun `2 operators 3 numbers`() {
        val input = "2 * 7 + 5"
        assertEquals(evalTest(input), "19")
    }

    @Test
    fun `6 operators bigger numbers`() {//BEMDAS issue here
        val input = "22 / 11 + 500 - 234 * 10 + 1000 / 55"
        assertEquals(evalTest(input), "1836")
    }

    @Test
    fun `6 operators bigger numbers no BEMDAS issue`() {//this would have fractions which is a 2.0 feature
        val input = "22 * 11 / 500 - 234 + 10 + 1000 - 55"
        assertEquals(evalTest(input), "721")
    }

    @Test
    fun `6 operators less than`() {
        val input = "22 * 11 / 500 - 234 + 10 + 1000 < 55"
        assertEquals(evalTest(input), "false")
    }

    @Test
    fun `7 operators equal`() {//again lets ignore fractions for now
        val input = "22 * 11 / 500 - 234 + 10 + 1000 - 55 = 721"
        assertEquals(evalTest(input), "true")
    }

    @Test
    fun `brackets test right`() {//ran out of time to implement brackets
        val input = "2 + (7 * 5)"
        assertEquals(evalTest(input), "37")
    }

    @Test
    fun `brackets test left`() {
        val input = "(2 + 7) * 5"
        assertEquals(evalTest(input), "45")
    }

    @Test
    fun `unary operation test in equation`() {
        val input = "2 * 7 + -5"
        assertEquals(evalTest(input), "9")
    }
    @Test
    fun `unary operation test in front of equation`() {// this can only happen if I clean up my recursion and include the start inside it
        val input = "!2 * 7 + 5"
        assertEquals(evalTest(input), "-9")
    }
    @Test
    fun `unary operation test on its own`() {
        val input = "-5"
        assertEquals(evalTest(input), "-5")
    }

    @Test
    fun `complex boolean`() {
        val input = "T + F & T + T | F"
        assertEquals(evalTest(input), "false")
    }

    fun evalTest(input: String): String{
        val tokens = Lexer.newInstance(input)
        val ast = Parser.newInstance(tokens)
        val res = Interpreter.newInstance(ast)
        return res
    }
}
