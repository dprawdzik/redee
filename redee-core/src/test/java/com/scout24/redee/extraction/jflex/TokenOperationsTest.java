package com.scout24.redee.extraction.jflex;

import jflex.Tokenizer;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.List;

/**
 * Created by dprawdzik on 11.01.18.
 */
public class TokenOperationsTest {
    @Test
    public void calculateDistance() throws Exception {

        Tokenizer tokenizer = new Tokenizer("Es gibt keinen Balkon!");
        List<Token> tokens = tokenizer.toList();
        TokenOperations operations = new TokenOperations();
        int distance = operations.calculateDistance(tokens, TokenType.BALCONY, TokenType.NEGATION);
        TestCase.assertEquals(0, distance);

        tokenizer = new Tokenizer("Ein Balkon ist nicht vorhanden!");
        tokens = tokenizer.toList();
        distance = operations.calculateDistance(tokens, TokenType.BALCONY, TokenType.NEGATION);
        TestCase.assertEquals(1, distance);

        tokenizer = new Tokenizer("Ein Balkon vorhanden! Aber es ist Küche nicht!");
        tokens = tokenizer.toList();
        distance = operations.calculateDistance(tokens, TokenType.BALCONY, TokenType.NEGATION);
        TestCase.assertEquals(1000, distance);

    }

    @Test
    public void direction() throws Exception {

        Tokenizer tokenizer = new Tokenizer("Es gibt keinen Balkon!");
        List<Token> tokens = tokenizer.toList();
        TokenOperations operations = new TokenOperations();
        String direction = operations.direction(tokens, TokenType.BALCONY, TokenType.NEGATION);
        TestCase.assertEquals("backward", direction);

        tokenizer = new Tokenizer("Einen Balkon gibt es nicht!");
        tokens = tokenizer.toList();
        direction = operations.direction(tokens, TokenType.BALCONY, TokenType.NEGATION);
        TestCase.assertEquals("forward", direction);

    }

}