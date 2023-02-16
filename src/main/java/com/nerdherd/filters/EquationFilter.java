package com.nerdherd.filters;

import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.Map;

import net.objecthunter.exp4j.Expression;


public class EquationFilter implements Filter {
    Expression e;

    public EquationFilter(String equation, Map<String, Double> variables) {
        ExpressionBuilder expressionBuilder = new ExpressionBuilder(equation).variable("x");
        variables.forEach((str, dbl) -> expressionBuilder.variable(str));
        e = expressionBuilder.build();
    }

    public double calculate(double input) {
        e.setVariable("x", input);
        return e.evaluate();
    }


}
