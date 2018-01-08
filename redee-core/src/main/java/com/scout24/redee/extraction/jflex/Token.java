package com.scout24.redee.extraction.jflex;

import com.scout24.redee.extraction.Position;

/**
 * Created by dprawdzik on 08.01.18.
 */
public class Token {

    private final String text;
    private String type;
    private Position position;

    public Token(String text, String type, int start, int end){
        this.text = text;
        this.type = type;
        this.position = new Position("", "", start, end);

    }

    public String getText() {
        return text;
    }

    public String getType() {
        return type;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Token{");
        sb.append("text='").append(text).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", position=").append(position);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        if (!text.equals(token.text)) return false;
        if (!type.equals(token.type)) return false;
        return position.equals(token.position);
    }

    @Override
    public int hashCode() {
        int result = text.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + position.hashCode();
        return result;
    }
}
