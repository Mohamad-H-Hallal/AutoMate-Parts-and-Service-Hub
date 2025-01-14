package com.example.project.Model;

public class PartModel {
   private String make,model,category,subcategory,description,part_condition,name, year;
    private int id,scrapyard_id;
    private boolean negotiable;
    private double price;

    public PartModel(String make, String model, String category, String subcategory, String description, String part_condition, String name, int id, String year, int scrapyard_id, boolean negotiable, double price) {
        this.make = make;
        this.model = model;
        this.category = category;
        this.subcategory = subcategory;
        this.description = description;
        this.part_condition = part_condition;
        this.name = name;
        this.id = id;
        this.year = year;
        this.scrapyard_id = scrapyard_id;
        this.negotiable = negotiable;
        this.price = price;
    }



    public PartModel() {

    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPart_condition() {
        return part_condition;
    }

    public void setPart_condition(String part_condition) {
        this.part_condition = part_condition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getScrapyard_id() {
        return scrapyard_id;
    }

    public void setScrapyard_id(int scrapyard_id) {
        this.scrapyard_id = scrapyard_id;
    }

    public boolean isNegotiable() {
        return negotiable;
    }

    public void setNegotiable(boolean negotiable) {
        this.negotiable = negotiable;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
