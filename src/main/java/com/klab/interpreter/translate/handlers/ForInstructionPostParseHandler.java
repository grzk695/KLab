package com.klab.interpreter.translate.handlers;

import com.google.common.collect.Maps;
import com.klab.interpreter.commons.memory.IdentifierMapper;
import com.klab.interpreter.lexer.model.CodeAddress;
import com.klab.interpreter.parsing.model.ParseClass;
import com.klab.interpreter.parsing.model.ParseToken;
import com.klab.interpreter.parsing.model.expression.Expression;
import com.klab.interpreter.parsing.model.tokens.operators.OperatorCode;
import com.klab.interpreter.parsing.model.tokens.operators.OperatorToken;
import com.klab.interpreter.translate.exception.KeywordParseException;
import com.klab.interpreter.translate.model.*;
import com.klab.interpreter.translate.service.ExpressionTranslator;
import com.klab.interpreter.types.IdentifierObject;
import com.klab.interpreter.types.TokenIdentifierObject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ForInstructionPostParseHandler extends AbstractPostParseHandler implements InitializingBean {
    private static final String DATA_FORMAT = "$$%s$iterator%d%d/%s";
    private ForInstructionContext forInstructionContext = new ForInstructionContext();
    private IdentifierMapper identifierMapper;
    private Map<ExpressionPredicate, HandleAction> handlers = Maps.newLinkedHashMap();

    @Override
    public void afterPropertiesSet() throws Exception {
        handlers.put(this::isForStart, this::handleForStart);
        handlers.put(this::isForEnd, this::handleForEnd);
        handlers.put(this::isContinue, this::handleContinue);
        handlers.put(this::isBreak, this::handleBreak);
    }

    @Override
    protected Map<ExpressionPredicate, HandleAction> getHandlersMap() {
        return handlers;
    }

    @Override
    public void reset() {
        forInstructionContext = new ForInstructionContext();
    }

    private boolean isContinue(List<Expression<ParseToken>> expressions) {
        return expressions.size() == 1 && isParseClass(expressions, ParseClass.CONTINUE_FOR, 0);
    }

    @Override
    public boolean isInstructionCompletelyTranslated() {
        return forInstructionContext.size() == 0;
    }

    private MacroInstruction handleContinue(List<Expression<ParseToken>> expressions, ExpressionTranslator expressionTranslator) {
        setupNoPrintNoAns(expressions, 0);
        JumperInstruction jmp = createJmpInstruction();
        jmp.setJumpIndex(forInstructionContext.getFlhNextAddress());
        return new MacroInstruction().add(jmp, expressions.get(0).getValue().getAddress());
    }

    private MacroInstruction handleBreak(List<Expression<ParseToken>> expressions, ExpressionTranslator expressionTranslator) {
        setupNoPrintNoAns(expressions, 0);
        JumperInstruction jmp = createJmpInstruction();
        forInstructionContext.addFalseJumper(jmp);
        return new MacroInstruction().add(jmp, expressions.get(0).getValue().getAddress());
    }

    private MacroInstruction handleForStart(List<Expression<ParseToken>> expressions, ExpressionTranslator translator) {
        setupNoPrintNoAns(expressions, 1);
        checkIfAssignOperator(expressions);
        CodeAddress forAddress = expressions.get(0).getValue().getAddress();
        FLNextInstruction flnextInstruction = new FLNextInstruction();
        MacroInstruction macroInstruction = translator.translate(expressions.get(1));
        forInstructionContext.push(code.size() + macroInstruction.size() + 1, flnextInstruction);
        forInstructionContext.setCodeAddress(forAddress);
        forInstructionContext.setScriptId(code.getSourceId());
        findIteratorTarget(macroInstruction, flnextInstruction);
        CodeAddress address = expressions.get(0).getValue().getAddress();
        return macroInstruction
                .add(createFlInit(), address)
                .add(flnextInstruction, address);
    }

    private Instruction createFlInit() {
        Instruction instruction = new Instruction(InstructionCode.FLINIT, 0);
        String name = forInstructionContext.getIteratorDataName();
        instruction.addData(new TokenIdentifierObject(name, identifierMapper.getMainAddress(name)));
        return instruction;
    }

    private MacroInstruction handleForEnd(List<Expression<ParseToken>> expressions, ExpressionTranslator expressionTranslator) {
        JumperInstruction flhnextJump = createJmpInstruction();
        flhnextJump.setJumpIndex(forInstructionContext.getFlhNextAddress());
        forInstructionContext.setJumpsOnFalse(code.size() + 1);
        String iteratorName = forInstructionContext.getIteratorDataName();
        Integer iteratorAddress = identifierMapper.getMainAddress(iteratorName);
        Instruction clearInstruction = new Instruction(InstructionCode.CLEAR, 0);
        clearInstruction.addData(new TokenIdentifierObject(iteratorName, iteratorAddress));
        forInstructionContext.pop();
        CodeAddress address = expressions.get(0).getValue().getAddress();
        return new MacroInstruction().add(flhnextJump, address).add(clearInstruction, address);
    }

    private void findIteratorTarget(MacroInstruction macroInstruction, FLNextInstruction flnextInstruction) {
        Instruction instruction = macroInstruction.get(0);
        IdentifierObject id = (IdentifierObject) instruction.getData(0);
        forInstructionContext.setName(id.getId());
        flnextInstruction.setIteratorId(new TokenIdentifierObject(id.getId(), id.getAddress()));
        CodeAddress codeAddress = forInstructionContext.getCodeAddress();
        String name = String.format(DATA_FORMAT, id.getId(), codeAddress.getLine(), codeAddress.getColumn(), forInstructionContext.getScriptId());
        forInstructionContext.setIteratorDataName(name);
        Integer address = identifierMapper.registerMainIdentifier(name);
        flnextInstruction.setIteratorData(new TokenIdentifierObject(name, address));
        id.setAddress(address);
        id.setId(name);
    }

    private void checkIfAssignOperator(List<Expression<ParseToken>> expressions) {
        Expression<ParseToken> root = expressions.get(1);
        if (!ParseClass.OPERATOR.equals(root.getValue().getParseClass())) {
            throw new KeywordParseException(KeywordParseException.FOR_KEYWORD_ASSIGNMENT_EXPECTED);
        }
        if (!((OperatorToken) root.getValue()).getOperatorCode().equals(OperatorCode.ASSIGN)) {
            throw new KeywordParseException(KeywordParseException.FOR_KEYWORD_ASSIGNMENT_EXPECTED);
        }
    }

    private boolean isForEnd(List<Expression<ParseToken>> expressions) {
        return expressions.size() == 1 && isParseClass(expressions, ParseClass.ENDFOR_KEYWORD, 0);
    }

    private boolean isForStart(List<Expression<ParseToken>> expressions) {
        return expressions.size() == 2 && isParseClass(expressions, ParseClass.FOR_KEYWORD, 0);
    }

    private boolean isBreak(List<Expression<ParseToken>> expressions) {
        return expressions.size() == 1 && isParseClass(expressions, ParseClass.BREAK_FOR, 0);
    }

    @Autowired
    public void setIdentifierMapper(IdentifierMapper identifierMapper) {
        this.identifierMapper = identifierMapper;
    }
}
