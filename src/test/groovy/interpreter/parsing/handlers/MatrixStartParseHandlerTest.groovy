package interpreter.parsing.handlers

import interpreter.lexer.makers.TokenMaker
import interpreter.lexer.model.TokenClass
import interpreter.parsing.model.BalanceType
import interpreter.parsing.model.ParseClass
import interpreter.parsing.model.tokens.MatrixStartToken
import interpreter.parsing.service.BalanceContextService
import interpreter.parsing.service.ParseContextManager
import spock.lang.Specification

import static com.natpryce.makeiteasy.MakeItEasy.*

class MatrixStartParseHandlerTest extends Specification {

    def matrixHandler = new MatrixStartParseHandler()
    def parseContextManager = Mock(ParseContextManager)
    def balanceContextService = Mock(BalanceContextService)

    def setup() {
        matrixHandler.setContextManager(parseContextManager)
        matrixHandler.setBalanceContextService(balanceContextService)
    }

    def "Test supported class"() {
        when:
        def result = matrixHandler.getSupportedTokenClass()

        then:
        result == TokenClass.OPEN_BRACKET
    }

    def "Test handle method"() {
        given:
        def token = make a(TokenMaker.saveToken, with(TokenMaker.tokenClass, TokenClass.OPEN_BRACKET))
        def parseTokenStack = new MatrixStartToken()
        def parseTokenExpression = new MatrixStartToken()

        when:
        matrixHandler.handle()

        then:
        1 * parseContextManager.tokenAt(0) >> token
        1 * parseContextManager.incrementTokenPosition(1)
        1 * parseContextManager.addExpressionNode({ parseTokenExpression = it } as MatrixStartToken)
        1 * parseContextManager.stackPush({ parseTokenStack = it } as MatrixStartToken)
        1 * balanceContextService.add(parseContextManager, BalanceType.INSIDE_MATRIX)
        parseTokenStack == parseTokenExpression
        parseTokenStack.token == token
        parseTokenStack.parseClass == ParseClass.MATRIX_START

    }
}
