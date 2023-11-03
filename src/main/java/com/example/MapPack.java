package com.example;
import java.util.ArrayList;


public class MapPack {
    // Contains links to stickers
    private ArrayList<Pack> packs = new ArrayList<Pack>();

    // Constructor 1 
    public MapPack(Website site) {
        switch (site.GetNamePage()) {
            case "combotSticker":
                CombotStickersFillPack(site);
                break;

            case "chpicSticker":
                ChpicStickersFillPack(site);
                break;

            case "chpicEmoji":
                ChpicEmojiFillPack(site);
                break;
            // Add site method here
            // case "siteName":
            //     MethodSite;
            //     break;

            default:
                break;
        }
    }
    // Constructor 2 
    public MapPack(Website site1, Website site2) {
        ChpicStickersFillPack(site1);
        CombotStickersFillPack(site2);
        // Add site method here
    }


    //Парсеры для стикеров
    private void CombotStickersFillPack(Website htmlDom) {
        if (htmlDom.GetAllHtmlPage() != null){
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
    }

    private void ChpicStickersFillPack(Website htmlDom) {
        if (htmlDom.GetAllHtmlPage() != null){
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
        
    }
    
    
    //Парсеры для емоджи 
    private void ChpicEmojiFillPack(Website htmlDom) {
        if (htmlDom.GetAllHtmlPage() != null){
            int countPacks = htmlDom.GetAllHtmlPage().getElementsByClass("emojis_list_item clickable_area").size();
            //System.out.println("Кол-во емоджи: " + countPacks);

            for (int i = 0; i < countPacks; i++) {

                String urlPack =
                    "tg://addstickers?set=" + htmlDom.GetAllHtmlPage().getElementsByClass("collectionid").get(i).child(0)
                            .text();

                String namePack = htmlDom.GetAllHtmlPage().getElementsByClass("collectionid").get(i).child(0)
                            .text();

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

    public String getPackInfo(int count) {
        StringBuilder info = new StringBuilder();
        
        //Если нужно вывести 20 элементов, а в наборе нашлось меньше чем 20
        if (packs.size()< count) count = packs.size();


        info.append("Было найдено ").append(count).append(" наборов. \n").append("\n");
        
        for (int i = 0; i < count; i++) {
            info.append("Название: ").append(packs.get(i).GetName()).append("\n");
            info.append("Ссылка на добавление: ").append(packs.get(i).GetUrl()).append("\n");
            info.append("\n");
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
