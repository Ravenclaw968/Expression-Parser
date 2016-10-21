package org.math.functions;

/**
 * Created by Siddhartha on 10/20/2016.
 */
public class VariableNotFoundException extends Exception
{
    public VariableNotFoundException(String variable)
    {
        super("Variable name " + variable + " is not supplied.");
    }
}
