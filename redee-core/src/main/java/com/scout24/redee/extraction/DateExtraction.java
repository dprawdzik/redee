package com.scout24.redee.extraction;

import java.util.Date;

/**
 * This pojo is encapsulating all information of a extracted date.
 *
 * Created by dprawdzik on 09.01.18.
 */
public class DateExtraction extends ExtractionImpl {

    private final Date start;
    private final Date end;

    public DateExtraction(Date start, Date end, String text, String type, Position position) {
        super(text, type, position);
        this.start = start;
        this.end = end;
    }

    /**
     * Gets the starting time of the extracted date / time range.
     *
     * @return the starting time of the extracted date / time range.
     */
    public Date getStart() {
        return start;
    }

    /**
     * Gets the end time of the extracted date / time range. Might be null.
     *
     * @return the end time of the extracted date / time range. Might be null.
     */
    public Date getEnd() {
        return end;
    }
}
