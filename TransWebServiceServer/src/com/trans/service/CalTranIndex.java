package com.trans.service;

import java.io.File;
import java.util.Map;

import javax.naming.directory.DirContext;

import org.eclipse.jdt.internal.compiler.ast.ThisReference;

/**
 * @author admin
 *
 */
public class CalTranIndex {


	/**
	 * 
	 * @param time
	 * @param roads
	 * @return
	 */
	public static double[] calTranIndex(String time, String... roads) {
		double[] res = new double[288];
		// int maxRoadNum = 50;
		double[] carCount = new double[288];
		for (int i = 0; i < res.length; i++) {
			res[i] = 0;

		}
		for (int i = 0; i < carCount.length; i++) {
			carCount[i] = 0;
		}
		double dis = 0;
		double[] lumda = new double[288];
		DataStruct aDataStruct;
		// int roadindex=0;
		if (roads.length != 0) {

			for (String road : roads) {
				aDataStruct = FileManager.temporaryData.get(time + road);
				dis = 0;
				String[] dir = road.split("-");
				if (UtilMap.getDistance().get(
						UtilMap.getFileNameByCrossNum(dir[0]) + "-" + UtilMap.getFileNameByCrossNum(dir[1])) != null) {
					dis = UtilMap.getDistance()
							.get(UtilMap.getFileNameByCrossNum(dir[0]) + "-" + UtilMap.getFileNameByCrossNum(dir[1]));
				} else {
					System.out.println(
							UtilMap.getFileNameByCrossNum(dir[1]) + "-" + UtilMap.getFileNameByCrossNum(dir[0]));
					dis = UtilMap.getDistance()
							.get(UtilMap.getFileNameByCrossNum(dir[1]) + "-" + UtilMap.getFileNameByCrossNum(dir[0]));

				}
				for (int i = 0; i < 288; i++) {
					if (aDataStruct.flowCount[i] != 0) {
						lumda[i] = calIndex(aDataStruct.speed[i]);
						lumda[i] *= (aDataStruct.flowCount[i] / aDataStruct.speed[i] * dis);
						carCount[i] += (aDataStruct.flowCount[i] / aDataStruct.speed[i] * dis);
						res[i] += lumda[i];
					} else
						res[i] += 0;
				}
			}

		} else {
			File file = new File("F:/resultByMinute5/");
			File[] Names = file.listFiles();
			dis = 0;

			for (File name : Names) {
				String[] dir = new String[2];
				dir[0] = name.getName().substring(0, 12);
				dir[1] = name.getName().substring(13, 25);
				if (UtilMap.getDistance().get(dir[0] + "-" + dir[1]) != null) {
					dis = UtilMap.getDistance().get(dir[0] + "-" + dir[1]);

				} else {
					dis = UtilMap.getDistance().get(dir[1] + "-" + dir[0]);

				}
				aDataStruct = FileManager.temporaryData
						.get(time + UtilMap.getCrossingNumber(dir[0]) + "-" + UtilMap.getCrossingNumber(dir[1]));
				for (int i = 0; i < 288; i++) {
					if (aDataStruct.flowCount[i] != 0) {
						lumda[i] = calIndex(aDataStruct.speed[i]);
						lumda[i] *= (aDataStruct.flowCount[i] / aDataStruct.speed[i] * dis);
						res[i] += lumda[i];
						carCount[i] += (aDataStruct.flowCount[i] / aDataStruct.speed[i] * dis);
					} else
						res[i] += 0;
				}

			}

		}
		for (int i = 0; i < res.length; i++) {
			if(res[i]!=0)
				res[i] /= carCount[i];
		}

		return res;
	}

	// 公式 lumda = afa*log(C-v)+S
	

	private static double calIndex(double v) {
		if (v > TranIndexService.C - 2)
			return 0;
		else
			return TranIndexService.afa * Math.log(TranIndexService.C - v) + TranIndexService.S;
	}

}
