package com.scout24.hackdays;

public class Expose {
    public final String id;
    public final String title;
    public final String descriptionNote;
    public final String furnishingNote;
    public final String locationNote;
    public final String otherNote;

    public Expose(String id, String title, String descriptionNote, String furnishingNote, String locationNote, String otherNote) {
        this.id = id;
        this.title = title;
        this.descriptionNote = descriptionNote;
        this.furnishingNote = furnishingNote;
        this.locationNote = locationNote;
        this.otherNote = otherNote;
    }
}
