package com.mobi.efficacious.ESmartDemo.webservices;

public class Responce 
{
	int Result_Type;
	Object ResponceData;
	
	public Object getResponceData() {
		return ResponceData;
	}
	
	public int getResult_Type() {
		return Result_Type;
	}
	
	public void setResponceData(Object responceData) {
		ResponceData = responceData;
	}
	
	public void setResult_Type(int result_Type) {
		Result_Type = result_Type;
	}
}
