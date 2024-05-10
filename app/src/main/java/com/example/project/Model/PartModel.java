package com.example.project.Model;

public class PartModel {
    String id,make,model,year,category,subcategory,description,part_condition,name,price,negotiable,scrapyard_id,images;

    public PartModel(String id, String make, String model, String year, String category, String subcategory, String description, String part_condition, String name, String price, String negotiable, String scrapyard_id, String images) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.year = year;
        this.category = category;
        this.subcategory = subcategory;
        this.description = description;
        this.part_condition = part_condition;
        this.name = name;
        this.price = price;
        this.negotiable = negotiable;
        this.scrapyard_id = scrapyard_id;
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNegotiable() {
        return negotiable;
    }

    public void setNegotiable(String negotiable) {
        this.negotiable = negotiable;
    }

    public String getScrapyard_id() {
        return scrapyard_id;
    }

    public void setScrapyard_id(String scrapyard_id) {
        this.scrapyard_id = scrapyard_id;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
