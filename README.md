# Interpreter Test

This repository hosts an interpreter test designed to assess candidates' ability to implement an interpreter based on a provided skeleton.

## Test Paper

### Overview

This test is designed to evaluate your ability to implement an interpreter for a simple expression language. The interpreter should handle various expression types defined by specific nodes and operators.

### Provided Structures

The interpreter should be implemented using the following structures:

### Nodes

- `Expression`: Encapsulates different expression-related nodes.
    - `Constant`: Holds literal values.
    - `UnaryExpression`
    - `BinaryExpression`
- `Conditional`: Represents conditional operations, requiring a guard expression and true/false branches.

### Literal Values

Literal values are used to represent the types supported by the interpreter.

- `IntValue`: Represents integer literal values.
- `BooleanValue`: Represents boolean literal values.

### Supported Operators

The interpreter should support the unary and binary operators defined in the `UnaryOperator.kt` and `BinaryOperator.kt` `enum classes`. For example, the interpreter should handle binary expressions such as addition, multiplication, etc. of integer values.

### Requirements

Implement an interpreter that can evaluate the defined nodes and perform the defined operations:

- Evaluate constant expressions (literal values)
- Handle unary operations
- Perform binary operations (both arithmetic and logical)
- Evaluate conditional nodes based on guard conditions and branches. For simplicity, a Conditional Node ultimately evaluates to one of the defined literal values.
  **Note**: The guard expression should evaluate to a boolean value.

**Note**: Nested expressions should also be supported.

You can make changes to the `Visitor` interface and to the `EvalVisitor` class. You can add in methods if you see fit to do so.

Write comprehensive and thorough unit tests to validate the correctness of the implementation for various scenarios, including edge cases.
You can run the tests using `./gradlew test`.

In addition, you will also be asked questions on various related aspects to the AST and Interpreter.

### Evaluation Criteria

- Correctness and accuracy of interpreter implementation.
- Completeness of implementation.
- Quality and coverage of unit tests.
- Code readability, efficiency, and adherence to best practices.

