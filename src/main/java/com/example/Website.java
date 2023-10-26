package com.example;

import java.io.IOException;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Website {
    
    private String url;
    private String name;
    private Document htmlCode;
    
    //Constructor
    public Website(String url, String site){
        this.url = url;
        name = site;
        htmlCode = FillHtml(url);
    }
    
    private Document FillHtml(String url){
        try {
            Document doc = Jsoup.connect(url).
                userAgent("Chrome").
                get();
                
            return doc;

        } 
        catch (HttpStatusException e) {
            System.out.println(e.getStatusCode());
            return null;

        } 
        catch (IOException e) {
            e.printStackTrace(System.out);
            return null;
            
        }
    }

    //public methods
    public String GetUrlPage(){
        //System.out.println(url);
        return url;
    }

    public String GetNamePage(){
        //System.out.println(url);
        return name;
    }

    public Document GetAllHtmlPage(){
        return htmlCode;
    }

}
