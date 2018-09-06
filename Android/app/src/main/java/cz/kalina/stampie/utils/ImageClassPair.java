package cz.kalina.stampie.utils;

public class ImageClassPair<U, V> {

    private U pageId;
    private V pageClass;

    public ImageClassPair(U pageId, V pageClass){
        this.pageId = pageId;
        this.pageClass = pageClass;
    }

    public U getPageId() {
        return this.pageId;
    }

    public void setPageId(U pageId) {
        this.pageId = pageId;
    }

    public V getPageClass() {
        return this.pageClass;
    }

    public void setPageClass(V pageClass) {
        this.pageClass = pageClass;
    }
}
