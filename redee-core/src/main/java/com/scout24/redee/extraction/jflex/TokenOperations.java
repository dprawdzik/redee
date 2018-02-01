package com.scout24.redee.extraction.jflex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by dprawdzik on 11.01.18.
 */
public class TokenOperations {

    public int calculateDistance(List<Token> tokens, String tokenTypeA, String tokenTypeB) {
        List<Integer> distances = new ArrayList<>();
        int positionA = -1;
        int positionB = -1;

        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getType().equals(tokenTypeA)) {
                positionA = i;
                if(positionB != -1)
                    distances.add(Math.abs(positionA - positionB) - 1);
            } else if (token.getType().equals(tokenTypeB)) {

                positionB = i;
                if(positionA != -1)
                    distances.add(Math.abs(positionA - positionB) - 1);
            } else if (token.getType().equals(TokenType.SENTENCEMARKER)) {
                positionA = -1;
                positionB = -1;
            }
        }
        if(distances.isEmpty())
            return 1000;

        Collections.sort(distances);
        return distances.get(0);
    }

    public String direction(List<Token> tokens, String tokenTypeA, String tokenTypeB) {
        Token start = null;
        Token end = null;
        for (Token token : tokens) {
            if(!token.getType().equals(tokenTypeA) && !token.getType().equals(tokenTypeB))
                continue;

            if(token.getType().equals(tokenTypeA)) {
                if (start == null)
                    start = token;
                else
                    end = token;
            }
            if (token.getType().equals(tokenTypeB)) {
                if (start == null)
                    start = token;
                else
                    end = token;
            }
        }
        if(end != null && end.getType().equals(tokenTypeB))
            return "forward";

        if(start != null && start.getType().equals(tokenTypeB))
            return "backward";

        return "N/A";
    }
}
