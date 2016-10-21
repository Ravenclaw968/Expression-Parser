This project is designed for developers who need to efficiently evaluate matematical expressions that are represented as strings.

```java
// The two lines below are required so the Function class can determine the variables in the expression.
ArrayList<String> externalVariables = new ArrayList<String>();
externalVariables.add("x");

// The line below is to create an expression (I on mistake named the class Function).
Function function = new Function("x ^ 2", externalVariables);

// The next two lines sets up evaluating the expression at x = 3
HashMap<String, Double> map = new HashMap<String, Double>();
map.put("x", 3.0)

// The last line evaluates the expression at x = 3, which should be 9.0
System.out.println(function.evaluate(map));
```
