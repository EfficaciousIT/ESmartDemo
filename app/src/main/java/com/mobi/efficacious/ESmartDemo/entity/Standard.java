package com.mobi.efficacious.ESmartDemo.entity;

/**
 * Created by EFF-4 on 9/3/2017.
 */

public class Standard {
    private String Division_id;
    private String Standard_id;
    private String Standard_name;
public String Standarad_div;
    public Standard()
    {

    }

    public String getStandard_id() {
        return Standard_id;
    }

    public void setStandard_id(String standard_id) {
        Standard_id = standard_id;
    }

    public String getStandard_name() {
        return Standard_name;
    }

    public void setStandard_name(String standard_name) {
        Standard_name = standard_name;
    }

    public String getStandarad_div() {
        return Standarad_div;
    }

    public void setStandarad_div(String standarad_div) {
        Standarad_div = standarad_div;
    }

    public String getDivision_id() {
        return Division_id;
    }

    public void setDivision_id(String division_id) {
        Division_id = division_id;
    }
}
