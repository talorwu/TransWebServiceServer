package com.trans.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TranIndexService {
	static double afa;
	static double S;
	static double C;

	//key:时间(天)+路段，value：一天的数据
	
	//先查询是否在内存，在查询是否在文件，都不在再计算
	public double[] calTranIndex(String time, String ...roads) throws IOException {
		String filename;
		if (roads.length!=0) {
			for (String road : roads) {
				if(FileManager.dateUsedMap.containsKey(time+road)){
					continue;
				}else{
					String[] dir = road.split("-");
					
					filename = "F:/resultByMinute5/"+UtilMap.getFileNameByCrossNum(dir[0])+"-"+UtilMap.getFileNameByCrossNum(dir[1])+".txt";
					FileHandler.readFile(filename,time,road);
					
					if(!FileManager.dateUsedMap.containsKey(time+road)){
						new EntranceMain("F:/NTTDATA/"+time.substring(0,10),"F:/resultByMinute5/").action();
						FileHandler.readFile(filename,time,road);
					}
				}
			}
			
			
		}else{
			File file = new File("F:/resultByMinute5/");
			File[] Names = file.listFiles();
			if(Names.length==0){
				new EntranceMain("F:/NTTDATA/"+time.substring(0,10),"F:/resultByMinute5/").action();
			} else {
				for (File name : Names) {
					String[] dir = new String[2];
					dir[0] = name.getName().substring(0, 12);
					dir[1] = name.getName().substring(13, 25);
					if (FileManager.dateUsedMap.containsKey(
							time + UtilMap.getCrossingNumber(dir[0]) + "-" + UtilMap.getCrossingNumber(dir[1]))) {
						continue;
					} else {
						FileHandler.readFile(name.getName(), time,
								UtilMap.getCrossingNumber(dir[0]) + "-" + UtilMap.getCrossingNumber(dir[1]));
						if (!FileManager.dateUsedMap.containsKey(
								time + UtilMap.getCrossingNumber(dir[0]) + "-" + UtilMap.getCrossingNumber(dir[1]))) {
							new EntranceMain("F:/NTTDATA/" + time.substring(0, 10), "F:/resultByMinute5/").action();
							FileHandler.readFile(name.getName(), time,
									UtilMap.getCrossingNumber(dir[0]) + "-" + UtilMap.getCrossingNumber(dir[1]));
						}
					}
				}
			}
		}
		
		return CalTranIndex.calTranIndex( time, roads);

	}
	
	public static void calParams(double C) {
		afa = 10.0 / (Math.log(C + 2) - Math.log(2));
		S = -afa * Math.log(2);
		TranIndexService.C = C + 2;
		System.out.println(afa + "\t" + S + "\t" + TranIndexService.C);
	}

}
