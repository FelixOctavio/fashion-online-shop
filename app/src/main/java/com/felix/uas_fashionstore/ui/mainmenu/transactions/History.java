package com.felix.uas_fashionstore.ui.mainmenu.transactions;

import java.io.Serializable;

public class History implements Serializable {
    private int id;
    private String name;
    private Integer idDB, price, stockS, stockM, stockL, idStock, status;
    private String description, date, kode;

    public History(Integer idDB, String name, Integer price, String description, Integer stockS, Integer stockM, Integer stockL, Integer idStock, String date, Integer status, String kode) {
        this.idDB = idDB;
        this.name = name;
        this.price = price;
        this.description = description;
        this.stockS = stockS;
        this.stockM = stockM;
        this.stockL = stockL;
        this.idStock = idStock;
        this.date = date;
        this.status = status;
        this.kode = kode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStockS() {
        return stockS;
    }

    public void setStockS(Integer stockS) {
        this.stockS = stockS;
    }

    public Integer getStockM() {
        return stockM;
    }

    public void setStockM(Integer stockM) {
        this.stockM = stockM;
    }

    public Integer getStockL() {
        return stockL;
    }

    public void setStockL(Integer stockL) {
        this.stockL = stockL;
    }

    public int getIdDB() {
        return idDB;
    }

    public int getIdStock() {
        return idStock;
    }
    public String getdate() {
        return date;
    }
    public int getStatus() {
        return status;
    }
    public String getKode() {
        return kode;
    }

}
