package interpreter.parsing.factory.operator;

import interpreter.lexer.model.Token;
import interpreter.parsing.model.tokens.operators.OperatorToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static interpreter.parsing.model.tokens.operators.OperatorAssociativity.LEFT_TO_RIGHT;
import static interpreter.parsing.model.tokens.operators.OperatorAssociativity.RIGHT_TO_LEFT;
import static interpreter.parsing.model.tokens.operators.OperatorCode.*;
import static interpreter.parsing.model.tokens.operators.OperatorPriority.*;

@Service
public class OperatorFactory {
    private Map<String, Supplier<OperatorToken>> operatorsProducer = new HashMap<>();

    public OperatorFactory() {
        operatorsProducer.put("=", () -> new OperatorToken(2, RIGHT_TO_LEFT, LEVEL_10, ASSIGN));
        operatorsProducer.put("==", () -> new OperatorToken(2, LEFT_TO_RIGHT, LEVEL_20, EQ));
        operatorsProducer.put("~=", () -> new OperatorToken(2, LEFT_TO_RIGHT, LEVEL_20, NEQ));
        operatorsProducer.put(">", () -> new OperatorToken(2, LEFT_TO_RIGHT, LEVEL_20, GT));
        operatorsProducer.put(">=", () -> new OperatorToken(2, LEFT_TO_RIGHT, LEVEL_20, GE));
        operatorsProducer.put("<", () -> new OperatorToken(2, LEFT_TO_RIGHT, LEVEL_20, LT));
        operatorsProducer.put("<=", () -> new OperatorToken(2, LEFT_TO_RIGHT, LEVEL_20, LE));
        operatorsProducer.put("+", () -> new OperatorToken(2, LEFT_TO_RIGHT, LEVEL_30, ADD));
        operatorsProducer.put("-", () -> new OperatorToken(2, LEFT_TO_RIGHT, LEVEL_30, SUB));
        operatorsProducer.put("*", () -> new OperatorToken(2, LEFT_TO_RIGHT, LEVEL_40, MULT));
        operatorsProducer.put("/", () -> new OperatorToken(2, LEFT_TO_RIGHT, LEVEL_40, DIV));
        operatorsProducer.put(":", () -> new OperatorToken(2, LEFT_TO_RIGHT, LEVEL_25, RANGE));
        operatorsProducer.put("$::", () -> new OperatorToken(3, LEFT_TO_RIGHT, LEVEL_25, RANGE3));
    }

    public OperatorToken getOperator(final Token token) {
        OperatorToken operatorToken = operatorsProducer.get(token.getLexeme()).get();
        operatorToken.setToken(token);
        return operatorToken;
    }

    public OperatorToken getOperator(final String token) {
        return operatorsProducer.get(token).get();
    }
}
