package org.math.functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Stack;

public class Function
{
    private static String[] functions = {"sin", "cos", "tan", "csc", "sec", "tan", "arcsin", "arccos", "arctan", "arccsc", "arcsec", "arccot", "ln", "abs"};
    private static ArrayList<String> constants = new ArrayList<String>();
    private static HashMap<String, Integer> precedence = new HashMap<String, Integer>();
    private Node root;
    private static class Node
    {
        String value = null;
        Node left = null;
        Node right = null;
    }
    public Function(String function, ArrayList<String> externalVariables)
    {
        constants.add("pi");
        constants.add("e");
        for (String str : externalVariables)
        {
            constants.add(str);
        }
        precedence.put("(", 0);
        precedence.put(")", 0);
        precedence.put("+", 1);
        precedence.put("-", 1);
        precedence.put("*", 2);
        precedence.put("/", 2);
        precedence.put("^", 3);
        String stage1 = format(function);
        root = obtainTree(getPostfix(stage1));
    }
    public void preOrder()
    {
        preOrder(root, 0);
    }
    public double evaluate(HashMap<String, Double> map) throws Exception
    {
        return evaluate(root, map);
    }
    private double evaluate(Node node, HashMap<String, Double> map) throws Exception
    {
        double value = 0;
        if (node.value.equals("+"))
        {
            value = evaluate(node.left, map) + evaluate(node.right, map);
        }
        else if (node.value.equals("-"))
        {
            value = evaluate(node.left, map) - evaluate(node.right, map);
        }
        else if (node.value.equals("*"))
        {
            value = evaluate(node.left, map) * evaluate(node.right, map);
        }
        else if (node.value.equals("/"))
        {
            value = evaluate(node.left, map) / evaluate(node.right, map);
        }
        else if (node.value.equals("^"))
        {
            value = Math.pow(evaluate(node.left, map), evaluate(node.right, map));
        }
        else if (node.value.equals("()"))
        {
            if (node.left.value.equals("sin"))
            {
                value = Math.sin(Math.toRadians(evaluate(node.right, map)));
            }
            else if (node.left.value.equals("cos"))
            {
                value = Math.cos(Math.toRadians(evaluate(node.right, map)));
            }
            else if (node.left.value.equals("tan"))
            {
                value = Math.tan(Math.toRadians(evaluate(node.right, map)));
            }
            else if (node.left.value.equals("csc"))
            {
                value = 1 / Math.sin(Math.toRadians(evaluate(node.right, map)));
            }
            else if (node.left.value.equals("sec"))
            {
                value = 1 / Math.cos(Math.toRadians(evaluate(node.right, map)));
            }
            else if (node.left.value.equals("cot"))
            {
                value = 1 / Math.tan(Math.toRadians(evaluate(node.right, map)));
            }
            else if (node.left.value.equals("arcsin"))
            {
                value = Math.toDegrees(Math.asin(evaluate(node.right, map)));
            }
            else if (node.left.value.equals("arccos"))
            {
                value = Math.toDegrees(Math.acos(evaluate(node.right, map)));
            }
            else if (node.left.value.equals("arctan"))
            {
                value = Math.toDegrees(Math.atan(evaluate(node.right, map)));
            }
            else if (node.left.value.equals("arccsc"))
            {
                value = Math.toDegrees(Math.asin(1 / evaluate(node.right, map)));
            }
            else if (node.left.value.equals("arcsec"))
            {
                value = Math.toDegrees(Math.acos(1 / evaluate(node.right, map)));
            }
            else if (node.left.value.equals("arccot"))
            {
                value = Math.toDegrees(Math.atan(1 / evaluate(node.right, map)));
            }
            else if (node.left.value.equals("ln"))
            {
                value = Math.log(evaluate(node.right, map));
            }
            else if (node.left.value.equals("abs"))
            {
                value = Math.abs(evaluate(node.right, map));
            }
        }
        else
        {
            try
            {
                value = Double.parseDouble(node.value);
            }
            catch (Exception e)
            {
                if (! map.containsKey(node.value))
                {
                    throw new VariableNotFoundException(node.value);
                }
                else
                {
                    value = map.get(node.value);
                }
            }
        }
        return value;
    }
    private void preOrder(Node node, int tabs)
    {
        if (node == null)
        {
            return;
        }
        String str = "";
        for (int i = 0; i < tabs; i ++)
        {
            str += "\t";
        }
        System.out.println(str + node.value);
        preOrder(node.left, tabs + 1);
        preOrder(node.right, tabs + 1);
    }
    private Node obtainTree(String postfix)
    {
        Stack<Node> stack = new Stack<Node>();
        ArrayList<Integer> list = positionOfFunctions(postfix);
        for (int i = 0; i < postfix.length(); i ++)
        {
            if (list.contains(i))
            {
                int positionOfFirstParantheses = i;
                for (int j = i; j < postfix.length(); j ++)
                {
                    if (postfix.substring(j, j + 1).equals("("))
                    {
                        break;
                    }
                    positionOfFirstParantheses ++;
                }
                Stack<String> openParantheses = new Stack<String>();
                int positionOfLastParantheses = positionOfFirstParantheses;
                for (int j = positionOfFirstParantheses + 1; j < postfix.length(); j ++)
                {
                    if (postfix.substring(j, j + 1).equals("("))
                    {
                        openParantheses.push("(");
                    }
                    else if (postfix.substring(j, j + 1).equals(")"))
                    {
                        if (openParantheses.isEmpty())
                        {
                            positionOfLastParantheses = j;
                            break;
                        }
                        else
                        {
                            openParantheses.pop();
                        }
                    }
                }
                String functionName = "";
                for (int j = i; j < postfix.length(); j ++)
                {
                    if (postfix.substring(j, j + 1).equals("("))
                    {
                        break;
                    }
                    else
                    {
                        functionName += postfix.substring(j, j + 1);
                    }
                }
                String inside = postfix.substring(positionOfFirstParantheses + 1, positionOfLastParantheses);
                Node insidePostfix = obtainTree(inside);
                Node left = new Node();
                left.value = functionName;
                Node top = new Node();
                top.value = "()";
                top.left = left;
                top.right = insidePostfix;
                stack.push(top);
                i = positionOfLastParantheses;
            }
            else
            {
                if (postfix.substring(i, i + 1).equals("["))
                {
                    int lastBracket = i;
                    for (int j = i; j < postfix.length(); j ++)
                    {
                        if (postfix.substring(j, j + 1).equals("]"))
                        {
                            lastBracket = j;
                            break;
                        }
                    }
                    Node addition = new Node();
                    addition.value = postfix.substring(i + 1, lastBracket);
                    stack.push(addition);
                    i = lastBracket;
                }
                else
                {
                    Node second = stack.pop();
                    Node first = stack.pop();
                    Node top = new Node();
                    top.left = first;
                    top.right = second;
                    if (postfix.substring(i, i + 1).equals("+"))
                    {
                        top.value = "+";
                    }
                    else if (postfix.substring(i, i + 1).equals("-"))
                    {
                        top.value = "-";
                    }
                    else if (postfix.substring(i, i + 1).equals("*"))
                    {
                        top.value = "*";
                    }
                    else if (postfix.substring(i, i + 1).equals("/"))
                    {
                        top.value = "/";
                    }
                    else if (postfix.substring(i, i + 1).equals("^"))
                    {
                        top.value = "^";
                    }
                    stack.push(top);
                }
            }
        }
        return stack.pop();
    }
    private ArrayList<Integer> positionOfFunctions(String str)
    {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (String function : functions)
        {
            for (int i = 0; i <= str.length() - function.length(); i ++)
            {
                if (str.substring(i, i + function.length()).equals(function))
                {
                    list.add(i);
                }
            }
        }
        Collections.sort(list);
        return list;
    }
    private ArrayList<Integer> positionOfConstants(String str)
    {
        ArrayList<Integer> positions = new ArrayList<Integer>();
        for (String s : constants)
        {
            for (int i = 0; i <= str.length() - s.length(); i ++)
            {
                if (str.substring(i, i + s.length()).equals(s))
                {
                    positions.add(i);
                }
            }
        }
        return positions;
    }
    private String format(String function)
    {
        String build = "";
        ArrayList<Integer> positionsOfConstants = positionOfConstants(function);
        for (int i = 0; i < function.length(); i ++)
        {
            try
            {
                int test = Integer.parseInt(function.substring(i, i + 1));
                String addition = "";
                for (int j = i; j < function.length(); j ++)
                {
                    try
                    {
                        int test2 = Integer.parseInt(function.substring(j, j + 1));
                        addition += test2;
                    }
                    catch (Exception e)
                    {
                        if (function.substring(j, j + 1).equals("."))
                        {
                            addition += ".";
                        }
                        else
                        {
                            break;
                        }
                    }
                }
                build += "[" + addition + "]";
                i += addition.length() - 1;
            }
            catch (Exception e)
            {
                if (positionsOfConstants.contains(i))
                {
                    String addition = "";
                    for (String s : constants)
                    {
                        try
                        {
                            if (function.substring(i, i + s.length()).equals(s))
                            {
                                addition = s;
                                break;
                            }
                        }
                        catch (Exception f)
                        {
                        }
                    }
                    build += "[" + addition + "]";
                    i += addition.length() - 1;
                }
                else
                {
                    build += function.substring(i, i + 1);
                }
            }
        }
        build = build.replaceAll("\\s+", "");
        String fixBuild = "";
        for (int i = 0; i < build.length(); i ++)
        {
            if (! build.substring(i, i + 1).equals("-"))
            {
                fixBuild += build.substring(i, i + 1);
            }
            else
            {
                boolean negativeSign = false;
                if ((i == 0) || (isOperator(build.substring(i - 1, i))) || build.substring(i - 1, i).equals("("))
                {
                    negativeSign = true;
                }
                if (negativeSign)
                {
                    int firstBracket = i + 1;
                    int lastBracket = firstBracket;
                    for (int j = firstBracket; j < build.length(); j ++)
                    {
                        if (build.substring(j, j + 1).equals("]"))
                        {
                            lastBracket = j;
                            break;
                        }
                    }
                    fixBuild += "[-" + build.substring(firstBracket + 1, lastBracket) + "]";
                    i = lastBracket;
                }
                else
                {
                    fixBuild += build.substring(i, i + 1);
                }
            }
        }
        return fixBuild;
    }
    private String getPostfix(String function)
    {
        String postfix = "";
        ArrayList<Integer> positions = positionOfFunctions(function);
        Stack<String> operators = new Stack<String>();
        for (int i = 0; i < function.length(); i ++)
        {
            if (positions.contains(i))
            {
                int positionOfFirstParantheses = i;
                for (int j = i; j < function.length(); j ++)
                {
                    if (function.substring(j, j + 1).equals("("))
                    {
                        break;
                    }
                    positionOfFirstParantheses ++;
                }
                Stack<String> openParantheses = new Stack<String>();
                int positionOfLastParantheses = positionOfFirstParantheses;
                for (int j = positionOfFirstParantheses + 1; j < function.length(); j ++)
                {
                    if (function.substring(j, j + 1).equals("("))
                    {
                        openParantheses.push("(");
                    }
                    else if (function.substring(j, j + 1).equals(")"))
                    {
                        if (openParantheses.isEmpty())
                        {
                            positionOfLastParantheses = j;
                            break;
                        }
                        else
                        {
                            openParantheses.pop();
                        }
                    }
                }
                String inside = function.substring(positionOfFirstParantheses + 1, positionOfLastParantheses);
                String insidePostfix = getPostfix(inside);
                postfix += function.substring(i, positionOfFirstParantheses) + "(" + insidePostfix + ")";
                i = positionOfLastParantheses;
            }
            else
            {
                if (! isOperator(function.substring(i, i + 1)))
                {
                    postfix += function.substring(i, i + 1);
                }
                else
                {
                    boolean treatAsOperator = true;
                    if (function.substring(i, i + 1).equals("-"))
                    {
                        boolean inBrackets = false;
                        for (int j = i; j >= 0; j --)
                        {
                            if (function.substring(j, j + 1).equals("["))
                            {
                                inBrackets = true;
                                break;
                            }
                            else if (function.substring(j, j + 1).equals("]"))
                            {
                                break;
                            }
                        }
                        if (inBrackets)
                        {
                            postfix += function.substring(i, i + 1);
                            treatAsOperator = false;
                        }
                    }
                    if (treatAsOperator)
                    {
                        if (operators.isEmpty() || function.substring(i, i + 1).equals("("))
                        {
                            operators.push(function.substring(i, i + 1));
                        }
                        else
                        {
                            if (function.substring(i, i + 1).equals(")"))
                            {
                                while (! operators.peek().equals("("))
                                {
                                    postfix += operators.pop();
                                }
                                operators.pop();
                            }
                            else
                            {
                                if (precedence.get(function.substring(i, i + 1)) >= precedence.get(operators.peek()))
                                {
                                    operators.push(function.substring(i, i + 1));
                                }
                                else
                                {
                                    while ((! operators.isEmpty()) && (precedence.get(function.substring(i, i + 1)) <= precedence.get(operators.peek())))
                                    {
                                        postfix += operators.pop();
                                    }
                                    operators.push(function.substring(i, i + 1));
                                }
                            }
                        }
                    }
                }
            }
        }
        while (! operators.isEmpty())
        {
            postfix += operators.pop();
        }
        return postfix;
    }
    private boolean isOperator(String item)
    {
        return (item.equals("+") || item.equals("-") || item.equals("*") || item.equals("/") || item.equals("^") || item.equals("(") || item.equals(")"));
    }
}