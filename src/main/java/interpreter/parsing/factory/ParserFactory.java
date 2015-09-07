package interpreter.parsing.factory;

import interpreter.lexer.model.TokenClass;
import interpreter.parsing.service.Parser;
import interpreter.parsing.service.ParserService;
import interpreter.parsing.service.handlers.NumberHandler;
import interpreter.parsing.service.handlers.OperatorHandler;

public class ParserFactory {

    public static Parser getParser() {
        Parser parser = new ParserService();
        parser.addParseHandler(TokenClass.NUMBER, new NumberHandler());
        parser.addParseHandler(TokenClass.OPERATOR, new OperatorHandler());
        return parser;
    }
}
