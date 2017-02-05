package com.when0matters.xlist.Models;

/**
 * Created by HuiMin on 1/29/2017.
 */

public class Category {
    private int _Id;
    private String Name;

    public Category(int _Id, String name) {
        this._Id = _Id;
        Name = name;
    }

    public int get_Id() {
        return _Id;
    }

    public void set_Id(int _Id) {
        this._Id = _Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
