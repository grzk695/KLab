package interpreter.parsing.makers

import com.natpryce.makeiteasy.Instantiator
import com.natpryce.makeiteasy.Property
import com.natpryce.makeiteasy.PropertyLookup
import interpreter.lexer.model.Token
import interpreter.parsing.model.NumberType
import interpreter.parsing.model.tokens.NumberToken

import static com.natpryce.makeiteasy.MakeItEasy.a
import static com.natpryce.makeiteasy.MakeItEasy.make
import static com.natpryce.makeiteasy.Property.newProperty
import static interpreter.lexer.makers.TokenMaker.saveToken

class NumberTokenMaker {

    static final Property<NumberToken, NumberType> numberType = newProperty()
    static final Property<NumberToken, Token> token = newProperty()

    static final saveNumberToken = new Instantiator<NumberToken>() {

        @Override
        NumberToken instantiate(PropertyLookup<NumberToken> lookup) {
            NumberToken numberToken = new NumberToken();
            numberToken.numberType = lookup.valueOf(numberType, NumberType.DOUBLE)
            numberToken.token = lookup.valueOf(token, make(a(saveToken)))
            return numberToken;
        }
    }
}
