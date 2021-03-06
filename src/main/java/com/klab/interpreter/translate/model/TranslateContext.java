package com.klab.interpreter.translate.model;

import com.klab.interpreter.parsing.model.ParseToken;
import com.klab.interpreter.parsing.model.expression.Expression;

public class TranslateContext {
    private Expression<ParseToken> expression;
    private MacroInstruction macroInstruction = new MacroInstruction();

    public TranslateContext(Expression<ParseToken> expression) {
        this.expression = expression;
    }

    public MacroInstruction getMacroInstruction() {
        return macroInstruction;
    }

    public void setMacroInstruction(MacroInstruction macroInstruction) {
        this.macroInstruction = macroInstruction;
    }

    public Expression<ParseToken> getExpression() {
        return expression;
    }
}
