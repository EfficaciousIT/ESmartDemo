package com.mobi.efficacious.ESmartDemo.entity;

public class dialog_boxMap {
    public String getVehicle_id() {
        return Vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        Vehicle_id = vehicle_id;
    }

    public String getVehicle_no() {
        return Vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        Vehicle_no = vehicle_no;
    }

    private String Vehicle_id;
    private String Vehicle_no;
public dialog_boxMap()
{

}

    public dialog_boxMap(String Vehicle_id, String Vehicle_no)
    {
        super();
        this.Vehicle_id = Vehicle_id;
        this.Vehicle_no = Vehicle_no;

    }


}

