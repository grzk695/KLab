package interpreter.parsing.handlers;

import interpreter.lexer.model.TokenClass;
import interpreter.parsing.handlers.helpers.ExpressionHelper;
import interpreter.parsing.handlers.helpers.StackHelper;
import interpreter.parsing.model.BalanceType;
import interpreter.parsing.model.ParseClass;
import interpreter.parsing.model.ParseToken;
import interpreter.parsing.model.expression.ExpressionNode;
import interpreter.parsing.service.BalanceContextService;
import interpreter.parsing.service.ParseContextManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static interpreter.parsing.model.ParseClass.MATRIX_START;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MatrixEndParseHandler extends AbstractParseHandler {

    private StackHelper stackHelper;
    private ExpressionHelper expressionHelper;
    private BalanceContextService balanceContextService;
    private MatrixNewRowHandler matrixNewRowHandler;

    @Override
    public void handle() {
        handleAction();
        parseContextManager.incrementTokenPosition(1);
    }

    public void handleAction() {
        balanceContextService.popOrThrow(parseContextManager, BalanceType.INSIDE_MATRIX);
        endRow();
        moveStackToExpression();
        reduceExpression();
    }

    @Override
    public TokenClass getSupportedTokenClass() {
        return TokenClass.CLOSE_BRACKET;
    }

    private void endRow() {
        if (parseContextManager.expressionSize() > 0 && !parseContextManager.expressionPeek().getValue().getParseClass().equals(ParseClass.MATRIX_VERSE)) {
            matrixNewRowHandler.handleAction();
        }
    }

    private void reduceExpression() {
        ExpressionNode<ParseToken> matrixNode = new ExpressionNode<>();
        matrixNode.addChildren(expressionHelper.popUntilParseClass(parseContextManager, this::popUntilPredicate));
        matrixNode.setValue(parseContextManager.expressionPop().getValue());
        matrixNode.getValue().setParseClass(ParseClass.MATRIX);
        parseContextManager.addExpression(matrixNode);
    }

    private boolean popUntilPredicate(ParseClass parseClass) {
        return parseClass.equals(MATRIX_START);
    }

    private void moveStackToExpression() {
        if (!stackHelper.stackToExpressionUntilTokenClass(parseContextManager, MATRIX_START)) {
            throw new RuntimeException();
        }
        parseContextManager.stackPop();
    }

    @Override
    public void setContextManager(ParseContextManager parseContextManager) {
        super.setContextManager(parseContextManager);
        matrixNewRowHandler.setContextManager(parseContextManager);
    }

    @Autowired
    public void setStackHelper(StackHelper stackHelper) {
        this.stackHelper = stackHelper;
    }

    @Autowired
    public void setExpressionHelper(ExpressionHelper expressionHelper) {
        this.expressionHelper = expressionHelper;
    }

    @Autowired
    public void setBalanceContextService(BalanceContextService balanceContextService) {
        this.balanceContextService = balanceContextService;
    }

    @Autowired
    public void setMatrixNewRowHandler(MatrixNewRowHandler matrixNewRowHandler) {
        this.matrixNewRowHandler = matrixNewRowHandler;
    }
}
