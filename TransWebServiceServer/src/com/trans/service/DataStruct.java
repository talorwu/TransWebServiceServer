package com.trans.service;



public class DataStruct {
	String[] time =new String[288];
	int[] flowCount =new int[288];
	double[] travelTime=new double[288];
	double[] speed=new double[288];
	double defaultTraveltime;
	
	public String[] getTime() {
		return time;
	}
	public void setTime(String[] time) {
		this.time = time;
	}
	public int[] getFlowCount() {
		return flowCount;
	}
	public void setFlowCount(int[] flowCount) {
		this.flowCount = flowCount;
	}
	public double[] getTravelTime() {
		return travelTime;
	}
	public void setTravelTime(double[] travelTime) {
		this.travelTime = travelTime;
	}
	public double[] getSpeed() {
		return speed;
	}
	public void setSpeed(double[] speed) {
		this.speed = speed;
	}
	public double getDefaultTraveltime() {
		return defaultTraveltime;
	}
	public void setDefaultTraveltime(double defaultTraveltime) {
		this.defaultTraveltime = defaultTraveltime;
	}
	
	public DataStruct(String[] time, int[] flowCount, double[] travelTime, double[] speed, double defaultTraveltime) {
		super();
		this.time = time;
		this.flowCount = flowCount;
		this.travelTime = travelTime;
		this.speed = speed;
		this.defaultTraveltime = defaultTraveltime;
	}
	public DataStruct() {
		// TODO Auto-generated constructor stub
	}
}
