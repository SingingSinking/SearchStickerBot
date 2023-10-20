package com.example;
import java.util.ArrayList;


public class MapPack {
    // Contains links to stickers
    private ArrayList<Pack> packs = new ArrayList<Pack>();

    // Constructor
    public MapPack(Website site) {
        switch (site.GetNamePage()) {
            case "combot":
                CombotFillPack(site);
                break;

            case "chpic":
                ChpicFillPack(site);
                break;

            // Add site here

            default:
                break;
        }
    }

    private void CombotFillPack(Website htmlDom) {
        int countPacks = (htmlDom.GetAllHtmlPage().getElementsByClass("sticker-pack__header").size()) / 2;
        for (int i = 0; i < countPacks; i++) {

            String urlPack =
                htmlDom.GetAllHtmlPage().getElementsByClass("sticker-pack sticker-packs-list__item").get(i).child(0)
                        .select("a").attr("href");

            String namePack = urlPack.substring(urlPack.lastIndexOf('/') + 1);

            String urlImgPack = htmlDom.GetAllHtmlPage().getElementsByClass("sticker-pack__header").get(i).child(0)
                    .child(0).select("div").attr("data-src");

            Pack pack = new Pack(namePack, urlPack, urlImgPack);

            packs.add(pack);
        }
    }

    private void ChpicFillPack(Website htmlDom) {
        int countPacks = htmlDom.GetAllHtmlPage().getElementsByClass("collectionid").size();
        for (int i = 0; i < countPacks; i++) {

            String urlPack =
                "https://t.me/addstickers/" + htmlDom.GetAllHtmlPage().getElementsByClass("collectionid").get(i).child(0)
                        .text();

            String namePack = urlPack.substring(urlPack.lastIndexOf('/') + 1);

            String urlImgPack = "https://chpic.su"
                    + htmlDom.GetAllHtmlPage().getElementsByClass("images").get(i).select("img").attr("src");

            if (urlImgPack.equals("https://chpic.su")) {
                // poster
                // urlImgPack +=
                // htmlDom.GetAllHtmlPage().getElementsByClass("images").get(i).select("video").attr("poster");
                // gif
                urlImgPack = "https://chpic.su"
                        + htmlDom.GetAllHtmlPage().getElementsByClass("images").get(i).select("source").attr("src");
            }

            Pack pack = new Pack(namePack, urlPack, urlImgPack);

            packs.add(pack);
        }
    }
    // Add new parse method here

    // public methods
    public String GetUrlPack(int index) {
        return packs.get(index).GetUrl();
    }

    public String GetNamePack(int index) {
        return packs.get(index).GetName();
    }

    public String GetUrlImgPack(int index) {
        return packs.get(index).GetImg();
    }

    public String getPackInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Count packs is: ").append(SizePack()).append(" elements\n");
        
        for (Pack pack : packs) {
            info.append("Name: ").append(pack.GetName()).append("\n");
            info.append("URL pack: ").append(pack.GetUrl()).append("\n");
            // info.append("URL image: ").append(pack.GetImg()).append("\n\n");
        }
    
        return info.toString();
    }

    public void PrintPackInfo() {
        System.out.println("Count packs is: " + SizePack() + " elements");
        for (Pack pack : packs) {
            System.out.println("Name: " + pack.GetName() + "\n" + "URL pack:  " + pack.GetUrl() + "\n"
                    + "URL image: " + pack.GetImg() + "\n");
        }
    }
    public int SizePack() {
        return packs.size();
    }

    public boolean PackListIsNull() {
        return packs.isEmpty();
    }
}
