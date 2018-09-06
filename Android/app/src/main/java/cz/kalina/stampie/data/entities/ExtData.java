package cz.kalina.stampie.data.entities;

import java.util.Calendar;

public class ExtData extends BaseEntity {

    private String type;
    private String text1;
    private String text2;
    private String text3;
    private String text4;
    private Calendar created;

    public ExtData() {
    }

    public ExtData(String type, String text1, String text2, String text3, String text4, Calendar created) {
        this.type = type;
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
        this.text4 = text4;
        this.created = created;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public String getText3() {
        return text3;
    }

    public void setText3(String text3) {
        this.text3 = text3;
    }

    public String getText4() {
        return text4;
    }

    public void setText4(String text4) {
        this.text4 = text4;
    }

    public Calendar getCreated() {
        return created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "ExtData [id=" + id + ", type=" + type + ", text1=" + text1 + ", text2=" + text2 + ", text3=" + text3 + ", text4=" + text4 + ", created=" + ((created != null) ? created : "null") + "]";
    }
}
