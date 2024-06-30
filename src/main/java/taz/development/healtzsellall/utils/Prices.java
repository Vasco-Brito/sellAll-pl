package taz.development.healtzsellall.utils;

public class Prices {

    int durability;
    double price;

    public Prices(int durability, double price) {
        this.durability = durability;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }




}
