package com.scout24.redee.extraction;

/**
 * Created by dprawdzik on 08.01.18.
 */
public class ExtractionImpl implements Extraction {
    private String text;
    private Position position;
    private String type;

    public ExtractionImpl(String text, String type, Position position) {
        this.text = text;
        this.type = type;
        this.position = position;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ExtractionImpl{");
        sb.append("text='").append(text).append('\'');
        sb.append(", position=").append(position);
        sb.append(", type='").append(type).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExtractionImpl that = (ExtractionImpl) o;

        if (!text.equals(that.text)) return false;
        if (!position.equals(that.position)) return false;
        return type.equals(that.type);
    }

    @Override
    public int hashCode() {
        int result = text.hashCode();
        result = 31 * result + position.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}
