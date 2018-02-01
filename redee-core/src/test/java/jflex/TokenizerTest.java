package jflex;

import com.scout24.redee.extraction.jflex.Token;
import com.scout24.redee.extraction.jflex.TokenType;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by dprawdzik on 11.01.18.
 */
public class TokenizerTest {
    @Test
    public void next() throws Exception {
        Tokenizer tokenizer = new Tokenizer("Kein Balkon!");
        List<Token> tokens = tokenizer.toList();
        TestCase.assertEquals(TokenType.NEGATION, tokens.get(0).getType());
        TestCase.assertEquals(TokenType.BALCONY, tokens.get(1).getType());
        tokenizer = new Tokenizer("kein Balkon!");
        tokens = tokenizer.toList();
        TestCase.assertEquals(TokenType.NEGATION, tokens.get(0).getType());
        TestCase.assertEquals(TokenType.BALCONY, tokens.get(1).getType());

        String content = "Großteil der Wohnungen ist zudem mit einem Balkon ausgestattet";
        tokenizer = new Tokenizer(content);
        tokens = tokenizer.toList();
        TestCase.assertEquals(tokens.get(0).getText(), TokenType.NEGATION, tokens.get(0).getType());
        TestCase.assertEquals(TokenType.BALCONY, tokens.get(7).getType());

        content = "Wohnung über k e i n e n Balkon";
        tokenizer = new Tokenizer(content);
        tokens = tokenizer.toList();
        TestCase.assertEquals(tokens.get(2).getText(), TokenType.NEGATION, tokens.get(2).getType());
        TestCase.assertEquals(TokenType.BALCONY, tokens.get(3).getType());
    }
}