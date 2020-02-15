package ml.chiragkhandhar.multinodepad;

import java.io.Serializable;
import java.util.Date;

class Notes implements Serializable
{
    private String title,notesText, date;

    public Notes() {
        this.title = "";
        this.notesText = "";
        this.date = "";
    }

    String getTitle()
    {
        return title;
    }

    void setTitle(String title)
    {
        this.title = title;
    }

    String getDesc()
    {
        return notesText;
    }

    void setDesc(String notesText)
    {
        this.notesText = notesText;
    }

    String getDate()
    {
        return date;
    }

    void setDate(String date)
    {
        this.date = date;
    }

}
