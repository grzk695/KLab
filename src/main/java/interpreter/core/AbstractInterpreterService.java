package interpreter.core;

import interpreter.InstructionKeyword.PostParseHandler;
import interpreter.commons.utils.ExpressionPrinter;
import interpreter.commons.utils.MacroInstructionPrinter;
import interpreter.InstructionKeyword.IfInstructionPostParseHandler;
import interpreter.execution.service.ExecutionService;
import interpreter.lexer.service.Tokenizer;
import interpreter.parsing.service.Parser;
import interpreter.translate.service.InstructionTranslator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractInterpreterService {

    protected ExecutionService executionService;
    protected Parser parser;
    protected Tokenizer tokenizer;
    protected InstructionTranslator instructionTranslator;
    protected ExpressionPrinter expressionPrinter;
    protected MacroInstructionPrinter macroInstructionPrinter;
    protected IfInstructionPostParseHandler ifPostHandler;
    private Set<PostParseHandler> postParseHandlers = new HashSet<>();

    public void resetCodeAndStack() {
        executionService.resetCodeAndStack();
    }

    protected boolean executionCanStart() {
        for(PostParseHandler handler : postParseHandlers) {
            if(!handler.executionCanStart()) {
                return false;
            }
        }
        return true;
    }

    @Autowired
    public void setExecutionService(ExecutionService executionService) {
        this.executionService = executionService;
    }

    @Autowired
    public void setTokenizer(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    @Autowired
    public void setParser(Parser parser) {
        this.parser = parser;
    }

    @Autowired
    public void setInstructionTranslator(InstructionTranslator instructionTranslator) {
        this.instructionTranslator = instructionTranslator;
    }

    @Autowired
    public void setExpressionPrinter(ExpressionPrinter expressionPrinter) {
        this.expressionPrinter = expressionPrinter;
    }

    @Autowired
    public void setMacroInstructionPrinter(MacroInstructionPrinter macroInstructionPrinter) {
        this.macroInstructionPrinter = macroInstructionPrinter;
    }

    @Autowired
    protected void setIfPostHandler(IfInstructionPostParseHandler ifPostHandler) {
        this.ifPostHandler = ifPostHandler;
        postParseHandlers.add(ifPostHandler);
    }
}
