package com.example.LC_receipt;

public class Receipt {
    private String retailer;
    private String purchaseDate;
    private String purchaseTime;

 
    static class Item{
        private String shortDescription;
        private Float price;

        public String getShortDescription(){
            return shortDescription;
        }

        public Float getPrice(){
            return price;
        }

    }

    private Item[] items;
    private Float total;

    public String getRetailer(){
        return this.retailer;
    }

    public String getPurchaseDate(){
        return this.purchaseDate;
    }

    public String getPurchaseTime(){
        return this.purchaseTime;
    }

    public Item[] getItems(){
        return this.items;
    }

    public Float getTotal(){
        return this.total;
    }
}