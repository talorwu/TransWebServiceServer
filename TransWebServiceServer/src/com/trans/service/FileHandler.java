package com.trans.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FileHandler {
	//Map<String, Boolean> dataUsed = new HashMap<String , Boolean>();
	//static Map<String, DataStruct> temporaryData = new HashMap<String , DataStruct>();
	//time:2016-05-04
	/*
	 * 返回路口标示
	 */
	/**读文件
	 * @param filename
	 * @param time
	 * @param road
	 * @throws IOException
	 */
	public static void readFile(String filename,String time,String road) throws IOException {
		String aData = "";
		String startTime = time.substring(5,10).replace("-", "")+"0000";
		//String endTime = time.substring(5,10).replace("-", "")+"2355";
		File filetmp = new File(filename);
		if (!filetmp.exists()) {
			filetmp.createNewFile();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(filename)));
		DataStruct value = new DataStruct();
		while ((aData = br.readLine()) != null){
			String[] data = aData.split("\t");
			if (startTime.equals(data[0])) {
				value.time[0] = data[0];
				value.flowCount[0] = data[1]=="NaN"||data[1]=="Infinite"?0:Integer.parseInt(data[1]);
				value.travelTime[0] = data[2]=="NaN"||data[2]=="Infinite"?0:Double.parseDouble(data[2]);
				value.speed[0] = data[3]=="NaN"||data[3]=="Infinite"?0:Double.parseDouble(data[3]);
				value.defaultTraveltime = data[4]=="NaN"||data[4]=="Infinite"?0:Double.parseDouble(data[4]);
				for (int i = 1; i < 288; i++) {
					if((aData = br.readLine()) != null){
						data = aData.split("\t");
						value.time[i] = data[0];
						value.flowCount[i] = data[1]=="NaN"||data[1]=="Infinite"?0:Integer.parseInt(data[1]);
						value.travelTime[i] = data[2]=="NaN"||data[2]=="Infinite"?0:Double.parseDouble(data[2]);
						value.speed[i] = data[3]=="NaN"||data[3]=="Infinite"?0:Double.parseDouble(data[3]);
					}
				}
				FileManager.temporaryData.put(time+road, value);
				FileManager.dateUsedMap.put(time+road, new Date());
				break;
			}
			
		}
	}
}
