package com.example.alphadrawer.ui.preferences;

public class Preferences {

    private Integer Results;
    private Integer Rad;
    private Double Lat;
    private Double Lng;

    public Preferences(){

    }

    public Integer getResults(){
        return Results;
    }

    public void setResults(Integer results){
        Results = results;
    }

    public Integer getrad(){
        return Rad;
    }

    public void setRad(Integer rad){
        Rad = rad;
    }

    public Double getlat(){
        return Lat;
    }

    public void setlat(Double lat){
        Lat = lat;
    }

    public Double getlng(){
        return Lng;
    }

    public void setlng(Double lng){
        Lng = lng;
    }
}
