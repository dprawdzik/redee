package com.scout24.redee.extraction;

import java.io.Serializable;

/**
 * This class represents of position in a given text. A text itself is a list of paragraphs.
 * Thus we need the paragraph, the starting position and the end position.
 * <p>
 * User: dprawdzik
 */
public class Position implements Serializable {

    public enum ParagraphType {
        TITLE,
        BODY,
        VERBATIM,
        DEFAULT;
    }

    protected String docId;

    private int start;

    private int end;

    public String getParagraphType() {
        return paragraph;
    }

    private String paragraph;


    private Position() {
    }

    public static Position createEmptyPosition() {
        return new Position("-1", ParagraphType.DEFAULT.name(), -1, -1);
    }

    /**
     * @param paragraph
     * @param start
     * @param end
     */
    public Position(String docId, String paragraph, int start, int end) {
        this.start = start;
        this.end = end;
        this.paragraph = paragraph;
        this.docId = docId;
    }

    public String getDocId() {
        return docId;
    }

    /**
     * The starting position of the given element in the text.
     *
     * @return the start position of the element.
     */
    public int getStart() {
        return start;
    }

    /**
     * @param start
     */
    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (start != position.start) return false;
        if (end != position.end) return false;
        if (!docId.equals(position.docId)) return false;
        return paragraph.equals(position.paragraph);

    }

    @Override
    public int hashCode() {
        int result = docId.hashCode();
        result = 31 * result + start;
        result = 31 * result + end;
        result = 31 * result + paragraph.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Position{");
        sb.append("start=").append(start);
        sb.append(", end=").append(end);
        sb.append(", paragraph=").append(paragraph);
        sb.append('}');
        return sb.toString();
    }


    public void setDocId(String docId) {
        this.docId = docId;
    }

    public void setParagraphType(String paragraphType) {
        this.paragraph = paragraphType;
    }
}
