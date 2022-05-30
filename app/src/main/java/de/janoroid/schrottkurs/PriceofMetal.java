package de.janoroid.schrottkurs;



class PriceofMetal {

    private String title;
    private String marcetprice;
    private String price;
    private int imageResource;


    PriceofMetal(String title, String marcetprice, String price, int imageResource) {
        this.title = title;
        this.imageResource = imageResource;
        this.marcetprice = marcetprice;
        this.price = price;
    }


    public int getImageResource(){

        return imageResource;
    }

    public String getTitle() {

        return title;
    }


    public String getMarcetprice() {

        return marcetprice;
    }


    public String getPrice() {

        return price;

    }
}



