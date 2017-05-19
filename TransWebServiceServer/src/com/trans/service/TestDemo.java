package com.trans.service;

import java.io.IOException;

public class TestDemo {
	public static void main(String[] args) throws IOException {
		TranIndexService tranIndexService = new TranIndexService();
		TranIndexService.calParams(35);
		double[] res = tranIndexService.calTranIndex("2016-09-24", "#7-#8");
		int i=0;
		for (double d : res) {
			System.out.println(i++ +" "+d);
		}
		
	}
}
