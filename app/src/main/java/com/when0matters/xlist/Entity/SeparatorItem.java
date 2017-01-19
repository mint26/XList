package com.when0matters.xlist.Entity;

/**
 * Created by HuiMin on 1/19/2017.
 */

public class SeparatorItem implements Item {

    public SeparatorItem(String dateHeader){
        this.dateHeader = dateHeader;
    }
    public boolean isSection(){
        return true;
    }

    public String dateHeader;

    public String getDateHeader() {
        return dateHeader;
    }

    public void setDateHeader(String dateHeader) {
        this.dateHeader = dateHeader;
    }
}
