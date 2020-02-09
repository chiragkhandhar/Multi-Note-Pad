package ml.chiragkhandhar.multinodepad;

import java.util.Date;

public class Notes
{
    private String title,notesText;
    private Date lut;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDesc()
    {
        return notesText;
    }

    public void setDesc(String notesText)
    {
        this.notesText = notesText;
    }

//    public Date getLut()
//    {
//        return lut;
//    }
//
//    public void setLut(Date lut)
//    {
//        this.lut = lut;
//    }
}
