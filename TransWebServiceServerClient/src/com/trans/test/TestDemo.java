package com.trans.test;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

import com.trans.service.TransWebServiceCallbackHandler;
import com.trans.service.TransWebServiceIOExceptionException;
import com.trans.service.TransWebServiceStub;
import com.trans.service.TransWebServiceStub.CalParams;
import com.trans.service.TransWebServiceStub.CalTranIndex;
import com.trans.service.TransWebServiceStub.CalTranIndexResponse;

public class TestDemo {
	public static void main(String[] args) throws RemoteException, TransWebServiceIOExceptionException{
		
		TransWebServiceStub transWebServiceStub = new TransWebServiceStub();
		CalParams calParams = new CalParams();
		calParams.setC(35);
		transWebServiceStub.calParams(calParams);
		CalTranIndex calTranIndex = new CalTranIndex();
		calTranIndex.addRoads("#7-#8");
		calTranIndex.setTime("2016-09-24");
		CalTranIndexResponse calTranIndexResponse = transWebServiceStub.calTranIndex(calTranIndex);
		double[] res = calTranIndexResponse.get_return();
		int i=0;
		for (double d : res) {
			System.out.println(i++ +" "+d);
		}
	}
}
