package com.example;

public class App 
{
    public static void main( String[] args ) {
        String nameSearchPack = "papich";
        Website combotSite = new Website("https://combot.org/telegram/stickers?q=" + nameSearchPack, "combot");
        // Website chpicSite = new Website("https://chpic.su/ru/stickers/search/" + nameSearchPack + "/?searchModule=stickers", "chpic");
        
        MapPack combotPack = new MapPack(combotSite);
        // MapPack chpicPack = new MapPack(chpicSite);

        combotPack.PrintPackInfo();
        
        // chpicPack.PrintPackInfo();
    }
}
