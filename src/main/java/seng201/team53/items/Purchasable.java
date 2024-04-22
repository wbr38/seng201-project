package seng201.team53.items;

public interface Purchasable {
    String getName();

    String getDescription();

    Double getCostPrice();

    Double getSellPrice();

    Boolean isSellable();
}
