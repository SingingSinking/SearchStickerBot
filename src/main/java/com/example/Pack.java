package com.example;

public class Pack {
    private String name;
    private String url;
    private String urlImg;
 
    public Pack(String name, String url, String urlImg){
        this.name = name;
        this.url = url;
        this.urlImg = urlImg;
    }

    public String GetName(){
        return name;
    }

    public String GetImg(){
        return urlImg;
    }

    public String GetUrl(){
        return url;
    }


}
