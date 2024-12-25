# **LaziCode Workflow Framework**

Welcome to the **LaziCode Workflow Framework**, an open-source Java library meticulously crafted to simplify the creation, management, and execution of complex workflows. Whether you're building automation pipelines, orchestrating business processes, or managing intricate data flows, our framework provides a robust and flexible foundation to streamline your workflow development.

## **🔍 Overview**

The **LaziCode Workflow Framework** empowers developers to construct workflows as interconnected nodes, each capable of performing sophisticated logical and mathematical operations. By abstracting the complexities of workflow management and expression evaluation, our framework allows you to focus on defining the logic and flow of your processes with ease and precision.

## **🚀 Key Features**

### **1. Modular Node Architecture**
- **Flexible Nodes:** Each node represents a distinct unit within your workflow, capable of executing custom expressions.
- **Bidirectional Connections:** Easily connect nodes to define input and output relationships, ensuring seamless data and control flow.
- **Connection Constraints:** Define minimum and maximum input/output limits to maintain workflow integrity and prevent configuration errors.

### **2. Powerful Expression Handling**
- **Logical Expressions:** Utilize a variety of logical operators (AND, OR, NOT, NAND, NOR, XOR, XNOR) to define decision-making processes within nodes.
- **Mathematical Expressions:** Perform complex calculations using arithmetic operators (+, -, *, /, %, ^) with support for both infix and postfix notations.
- **Expression Validation:** Ensure the correctness of expressions with comprehensive parsing and validation mechanisms.

### **3. Comprehensive Evaluation Engines**
- **Postfix Logic Evaluator:** Efficiently evaluate logical expressions in postfix notation, supporting both recursive and iterative approaches with optional short-circuiting.
- **Postfix Math Evaluator:** Accurately compute mathematical expressions, handling edge cases like division by zero and undefined variables gracefully.

### **4. JSON Serialization**
- **Persistent Workflows:** Serialize and deserialize workflows to and from JSON, enabling easy storage, sharing, and transmission of workflow configurations.
- **Detailed Representations:** Capture all essential node details, including connections and expressions, in a structured JSON format.

### **5. Extensible Interfaces**
- **Container Capabilities:** Implement the `ContainerAble` interface to create hierarchical or nested workflows, maintaining the sequence and structure of nodes.
- **Input/Output Management:** Leverage the `InputAble` and `OutputAble` interfaces to control how nodes receive and send data within the workflow.

### **6. Open-Source Freedom**
- **MIT License:** Fully open-source under the [MIT License](LICENSE), allowing you to use, modify, and contribute without restrictions.
- **Community-Driven:** Benefit from a growing community of developers contributing to and enhancing the framework, ensuring continuous improvement and support.

## **📦 Getting Started**

### **Installation**
Add the LaziCode Workflow Framework to your project using Maven:

```xml
<dependency>
    <groupId>com.lazicode.workflow</groupId>
    <artifactId>workflow-framework</artifactId>
    <version>1.0.0</version>
</dependency>
```

Or using Gradle:

```groovy
implementation 'com.lazicode.workflow:workflow-framework:1.0.0'
```

### **Basic Usage**

```java
import com.lazicode.workflow.node.Node;
import com.lazicode.workflow.expressions.LogicExpression;
import com.lazicode.workflow.expressions.MathExpression;
import com.lazicode.workflow.interfaces.ContainerAble;

// Create logical expressions
LogicExpression logicExpr = new LogicExpression("A B AND NOT");

// Create mathematical expressions
MathExpression mathExpr = new MathExpression("A B + C *");

// Initialize nodes with expressions
Node logicNode = new CustomLogicNode("LogicNode1", logicExpr, 1, 2, 1, 2);
Node mathNode = new CustomMathNode("MathNode1", mathExpr, 1, 2, 1, 2);

// Connect nodes
logicNode.connect(mathNode);

// Serialize to JSON
JSONObject workflowJson = logicNode.toJSON();
System.out.println(workflowJson.toString(4));
```

*Note: `CustomLogicNode` and `CustomMathNode` are hypothetical implementations extending the abstract `Node` class tailored for specific expression types.*

## **🧩 Detailed Components**

### **1. `Node` Abstract Class**

#### **Package:**
`com.lazicode.workflow.node`

#### **Overview:**
The `Node` class serves as the foundational building block of the workflow system. It represents individual units within a workflow that can be interconnected to define the workflow's execution path. Each `Node` can perform operations based on expressions and manage its connections with other nodes.

#### **Key Features:**

- **Attributes:**
  - `name`: Identifier for the node.
  - `connections`: A `Set<Node>` maintaining connections to other nodes.
  - `expression`: An `Expression` object defining the node's operation.
  - `maxInput`, `maxOutput`, `minInput`, `minOutput`: Constraints on the number of allowable input and output connections.
  - `creationTime`: Timestamp of node creation in UTC.

- **Constructors:**
  - **Default Constructor:** Initializes a node with a name and default connection limits.
  - **Parameterized Constructor:** Allows setting an expression and custom connection limits. It validates the provided expression to ensure it's valid before initialization.

- **Interfaces Implemented:**
  - `InputAble`: Manages input connections.
  - `OutputAble`: Manages output connections.
  - `JSONPersistable`: Facilitates JSON serialization for persistence or data transfer.

- **Core Methods:**
  - `connect(Node otherNode)`: Establishes a bidirectional connection with another node.
  - `disconnect(Node otherNode)`: Removes the connection with a specified node.
  - `getConnections()`: Retrieves all connected nodes.
  - `getConnectedNodeNames()`: Returns the names of all connected nodes.
  - `toString()`: Provides a string representation of the node, detailing its attributes and connections.
  - `toJSON()`: Serializes the node's state into a JSON object, including connections and expressions.

#### **Design Considerations:**

- **Bidirectional Connections:** When a node connects to another, both nodes acknowledge the connection, ensuring consistency.
  
- **Expression Integration:** Each node can encapsulate an `Expression`, allowing it to perform specific operations (logical or mathematical) during workflow execution.

- **JSON Serialization:** Implementing `JSONPersistable` ensures that nodes can be easily serialized and deserialized, facilitating storage and transmission.

### **2. Expression Evaluation Classes**

Expressions are central to defining the operations that each node performs. The framework abstracts expressions to support both logical and mathematical computations.

#### **2.1. `Expression` Abstract Class**

##### **Package:**
`com.lazicode.workflow.expressions`

##### **Overview:**
The `Expression` class is an abstract representation of a generic expression. It handles parsing, validation, and conversion between infix and postfix notations, and manages variables involved in the expressions.

##### **Key Features:**

- **Attributes:**
  - `expressionString`: The raw expression input.
  - `variables`: A `Set<String>` containing all variables used in the expression.
  - `variableValues`: A `Map<String, Object>` holding current values assigned to variables.
  - `output`: Caches the result of the expression evaluation.
  - `infixExpression`, `postfixExpression`: Different representations of the expression.

- **Core Methods:**
  - **Parsing and Validation:**
    - `extractVariables(String expression)`: Identifies and extracts variables from the expression.
    - `validateExpression(String expression)`: Ensures the expression contains only valid tokens.
    - `determineExpressionType(String expression, Set<String> SUPPORTED_OPERATORS)`: Determines if the expression is infix, postfix, or invalid.
    - `convertInfixToPostfix(String infix)`: Converts infix expressions to postfix.
    - `convertPostfixToInfix(String postfix)`: Converts postfix expressions back to infix.
  
  - **Variable Management:**
    - `setVariable(String variable, Object value)`: Assigns a value to a variable.
    - `getVariable(String variable)`: Retrieves the value of a variable.
  
  - **Evaluation:**
    - `getOutput()`: Calculates and returns the result of the expression.
    - `performCalculation()`: Abstract method to be implemented by subclasses for specific calculation logic.
  
  - **Serialization:**
    - `toJSON()`: Serializes the expression's state into JSON.

- **Abstract Methods:**
  - `isValid()`: Checks if the expression is valid.
  - `operatorType(String operator)`: Determines if an operator is unary or binary.
  - `isOperator(String token)`: Checks if a token is an operator.
  - `precedence(String operator)`: Defines operator precedence.
  - `isOperand(String token)`: Checks if a token is a valid operand.
  - `isLeftAssociative(String operator)`: Determines operator associativity.

##### **Design Considerations:**

- **Flexibility:** By abstracting expressions, the framework can support various types of operations (logical, mathematical) by extending this class.

- **Error Handling:** Comprehensive validation ensures that only well-formed expressions are processed, preventing runtime errors during workflow execution.

- **Caching Results:** Storing the output of evaluations optimizes performance by avoiding redundant computations.

#### **2.2. `LogicExpression` Class**

##### **Package:**
`com.lazicode.workflow.expressions`

##### **Overview:**
`LogicExpression` extends the `Expression` class to handle logical operations. It supports logical operators like AND, OR, NOT, NAND, NOR, XOR, and XNOR.

##### **Key Features:**

- **Attributes:**
  - `SUPPORTED_OPERATORS`: An unmodifiable `Set<String>` containing supported logical operators.
  - `isShortCircuit`: A boolean flag indicating whether to use short-circuit evaluation.

- **Constructors:**
  - Initializes the expression string and determines its type (infix or postfix).
  - Converts and validates the expression based on its notation.

- **Core Methods:**
  - **Operator Handling:**
    - `isOperand(String token)`: Validates operands specific to logical expressions.
    - `isOperator(String token)`: Checks if a token is a supported logical operator.
    - `precedence(String operator)`: Defines precedence for logical operators.
    - `operatorType(String operator)`: Differentiates between unary and binary operators.
    - `isLeftAssociative(String operator)`: Defines associativity; notably, NOT is treated differently.
  
  - **Evaluation:**
    - `performCalculation()`: Executes the logical expression using `PostfixLogic` evaluators, considering the `isShortCircuit` flag.

- **Serialization:**
  - `toString()`: Provides a detailed string representation, including expression types and evaluation results.

##### **Design Considerations:**

- **Expression Type Determination:** Automatically identifies whether the provided expression is infix or postfix, facilitating user flexibility.

- **Evaluation Strategies:** Supports both short-circuit and non-short-circuit evaluation, allowing optimization based on use-case requirements.

- **Operator Precedence and Associativity:** Carefully defines these to ensure accurate expression parsing and evaluation.

#### **2.3. `MathExpression` Class**

##### **Package:**
`com.lazicode.workflow.expressions`

##### **Overview:**
`MathExpression` extends the `Expression` class to handle mathematical operations. It supports operators like +, -, *, /, %, and ^ (exponentiation).

##### **Key Features:**

- **Attributes:**
  - `SUPPORTED_OPERATORS`: An unmodifiable `Set<String>` containing supported mathematical operators.

- **Constructors:**
  - Similar to `LogicExpression`, it initializes and validates the expression, converting between infix and postfix as necessary.

- **Core Methods:**
  - **Operator Handling:**
    - `isOperator(String token)`: Checks for supported mathematical operators.
    - `precedence(String operator)`: Defines precedence specific to mathematical operators.
    - `operatorType(String operator)`: Differentiates between binary and unsupported operators.
    - `isLeftAssociative(String operator)`: All supported operators are left-associative.
  
  - **Evaluation:**
    - `performCalculation()`: Executes the mathematical expression using `PostfixMath` evaluator.

- **Serialization:**
  - `toString()`: Provides a detailed string representation, including expression types and evaluation results.

##### **Design Considerations:**

- **Operator Support:** Exponentiation (`^`) is included, enhancing the range of mathematical operations that can be represented.

- **Validation:** Ensures that only valid mathematical expressions are processed, preventing computational errors.

### **3. Expression Evaluators**

To evaluate expressions efficiently, the framework employs postfix (Reverse Polish Notation) evaluation strategies for both logical and mathematical expressions.

#### **3.1. `PostfixLogic` Class**

##### **Package:**
`com.lazicode.workflow.expressions.evaluators`

##### **Overview:**
`PostfixLogic` provides mechanisms to evaluate logical expressions written in postfix notation. It supports a variety of logical operators and handles scenarios where variables might have undefined (`null`) values.

##### **Key Features:**

- **Evaluation Methods:**
  - `evalShortCircuit(String expression, Map<String, Boolean> values)`: Recursively evaluates the postfix logical expression, allowing for short-circuit behavior.
  - `eval(String expression, Map<String, Boolean> values)`: Iteratively evaluates the expression using a stack-based approach without short-circuiting.

- **Operator Handling:**
  - Supports operators: AND, OR, NOT, NAND, NOR, XOR, XNOR.
  - Handles undefined variables by propagating `null` results where appropriate.

- **Error Handling:**
  - Throws runtime exceptions for unsupported operators.
  - Manages invalid expressions by returning `null` or appropriate error indicators.

##### **Design Considerations:**

- **Flexibility in Evaluation:** Provides both recursive and iterative evaluation methods, catering to different performance and behavior needs.

- **Handling Undefined Variables:** Gracefully manages cases where some variables may not have assigned values, ensuring that evaluations remain robust.

#### **3.2. `PostfixMath` Class**

##### **Package:**
`com.lazicode.workflow.expressions.evaluators`

##### **Overview:**
`PostfixMath` evaluates mathematical expressions written in postfix notation. It supports basic arithmetic operations and exponentiation, handling cases like division by zero and invalid tokens.

##### **Key Features:**

- **Evaluation Method:**
  - `eval(String expression, Map<String, Double> values)`: Processes the postfix mathematical expression using a stack-based approach.

- **Operator Handling:**
  - Supports operators: +, -, *, /, %, ^.
  - Handles unary negation for negative numbers.
  - Manages division by zero by returning `Double.NaN`.

- **Error Handling:**
  - Returns `Double.NaN` for invalid tokens or operations.
  - Includes a `main` method demonstrating sample evaluations.

##### **Design Considerations:**

- **Robustness:** Ensures that invalid operations or tokens result in `NaN`, preventing incorrect computations from propagating silently.

- **Comprehensive Operator Support:** Includes exponentiation, enhancing the capability to represent complex mathematical expressions.

### **4. `ContainerAble` Interface**

#### **Package:**
`com.lazicode.workflow.interfaces`

#### **Overview:**
The `ContainerAble` interface defines the capabilities for nodes that can act as containers, holding child nodes in a specific sequence. This is essential for building hierarchical or nested workflows.

#### **Key Features:**

- **Core Methods:**
  - `addNode(Node node)`: Adds a child node to the container.
  - `removeNode(Node node)`: Removes a specified child node.
  - `getNodes()`: Retrieves all child nodes in a `LinkedHashSet`, preserving insertion order.
  - `hasNodes()`: Checks if the container holds any child nodes.

#### **Design Considerations:**

- **Order Preservation:** Using `LinkedHashSet` ensures that the sequence of child nodes is maintained, which can be crucial for workflows where order matters.

- **Flexibility:** Allows any implementing class to manage a collection of nodes, facilitating complex workflow structures.

## **🤝 Contributing**

We welcome contributions from the community! Whether it's reporting bugs, suggesting features, or submitting pull requests, your involvement helps make the **LaziCode Workflow Framework** better for everyone.

1. **Fork the Repository**
2. **Create a Feature Branch** (`git checkout -b feature/YourFeature`)
3. **Commit Your Changes** (`git commit -am 'Add new feature'`)
4. **Push to the Branch** (`git push origin feature/YourFeature`)
5. **Open a Pull Request**

## **📄 Documentation**

Comprehensive documentation is available [here](https://github.com/lazicode/workflow-framework/docs) to help you navigate and utilize all features of the framework effectively.

## **📫 Support**

For questions, issues, or suggestions, please open an issue on our [GitHub repository](https://github.com/lazicode/workflow-framework/issues) or reach out to our community channels.

---

Embark on building efficient and scalable workflows with the **LaziCode Workflow Framework**—your reliable partner in workflow automation and management!