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

Functions available for use.
|Function |    Example    |
|---------|---------------|
|  sin(x) |  sin(90) = 1  |
|  cos(x) |  cos(90) = 0  |
|  tan(x) |  tan(45) = 1  |
|  csc(x) |  csc(90) = 1  |
|  sec(x) |  sec(0) = 1   |
|  cot(x) |  cot(45) = 1  |
|arcsin(x)| arcsin(1) = 90|
|arccos(x)| arccos(1) = 0 |
|arctan(x)| arctan(1) = 45|
|arccsc(x)| arccsc(1) = 90|
|arcsec(x)| arcsec(1) = 0 |
|arccot(x)| arccot(1) = 45|
|  ln(x)  |  ln(e) = 1    |
|  abs(x) |  abs(-5) = 5  |
