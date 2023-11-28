open class LiteralValue {
    data class IntValue(val value: Int) : LiteralValue()
    data class BooleanValue(val value: Boolean) : LiteralValue()
    class Error() : LiteralValue()
}