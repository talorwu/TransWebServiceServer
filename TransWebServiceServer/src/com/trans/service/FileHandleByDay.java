package com.trans.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 * @author 作者 E-mail:
 * @date 创建时间：2016年9月6日 上午9:53:22
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class FileHandleByDay {

	private UtilMap util = new UtilMap();

	/**
	 * 
	 * @param date
	 *            2016-06-05
	 * @param fileParentPath
	 *            C:/demo/
	 * @param direction
	 *            611471004000-611471003000
	 * @param conf
	 * @throws IOException
	 * @throws ParseException
	 */
	
	private double max(double a,double b) {
		return a>=b?a:b;
	}
	
	@SuppressWarnings({ "unused", "static-access" })
	public void carCount(String fileParentPath, String fileOutput, String direction,
			Properties conf) throws IOException, ParseException {

		String date = fileParentPath.substring(
				fileParentPath.lastIndexOf("/") + 1, fileParentPath.length());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		// 文件名
		String[] fileName = direction.split("-", -1);
		// 起始地点需要计算过车量的方向
		String startAddressDirection = conf.getProperty(direction
				+ "-startDirection");
		// 终止地点需要计算过车量的方向
		String endAddressDirection = conf.getProperty(direction
				+ "-endDirection");
		
		if(startAddressDirection == null
				|| endAddressDirection == null){
//			System.out.println("无该路段配置信息");
			return;
		}
		// 路段距离
		double distanc = 0;
		if (util.getDistance().get(direction) != null) {
			distanc = util.getDistance().get(direction);
		} else {
			if (util.getDistance().get(
					direction.split("-", -1)[1] + "-"
							+ direction.split("-", -1)[0]) != null) {
				distanc = util.getDistance().get(
						direction.split("-", -1)[1] + "-"
								+ direction.split("-", -1)[0]);
			}
		}
		// 存放起始路口过车数
		Map<String, String> startMap = new HashMap<String, String>();
		Map<String, String> endMap = new HashMap<String, String>();

		String[] detn = startAddressDirection.split(" ", -1);

		String aData = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(fileParentPath + "/" + fileName[0]),
				"UTF-8"));

		while ((aData = br.readLine()) != null) {

			String[] data = aData.split(",", -1);

			if (data.length == 26) {

				if (startAddressDirection.indexOf(data[14]) > -1) {
					String[] line = conf
							.getProperty(direction + "-" + data[14]).split(" ",
									-1);
					Map<Integer, String> map = new HashMap<Integer, String>();
					for (int i = 0; i < line.length; i++) {
						map.put(Integer.parseInt(line[i]), "y");
					}

					if (map.get(Integer.parseInt(data[15])) != null) {

						startMap.put(data[8] + "," + data[18], data[18]);
					}
				}
			}
		}

		BufferedReader br0 = new BufferedReader(new InputStreamReader(
				new FileInputStream(fileParentPath + "/" + fileName[1]),
				"UTF-8"));
		String ADate = "";
		String carNumber = "^[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9\u4e00-\u9fa5]{1}$";

		while ((ADate = br0.readLine()) != null) {

			String[] Data = ADate.split(",", -1);
			if (endAddressDirection.equals(Data[14])
					&& Pattern.compile(carNumber).matcher(Data[8]).find()) {
				if (endMap.containsKey(Data[8])) {
					endMap.put(Data[8], endMap.get(Data[8])+","+Data[18]);
				}else {
					endMap.put(Data[8], Data[18]);
				}
			}
		}

		double count = 0;
		double MaxTime = 0;
		double MaxCount = 0;
		double allSpeed = 0;
		double allTime = 0;
		double defaultSpeed = 0;
		double defaultcount = 0;
		double flowCount = 0;
		
		Map<String,Integer> flowMap = new HashMap<String,Integer>(); 
		
		Set<String> keys = startMap.keySet();

		for (String key : keys) {
			// System.out.println(key.split(",", -1)[0]);
			if (endMap.get(key.split(",", -1)[0]) != null) {
				//多个车牌处理
				String[] endtimes =  endMap.get(key.split(",", -1)[0]).split(",",-1);
				int correctTimeIndex=-1;
				for (int i=0;i<endtimes.length;i++){
					if (timeCom(startMap.get(key), endtimes[i]) == -1) {
						if(correctTimeIndex == -1)
							correctTimeIndex = i;
						else{
							if (timeCom(endtimes[correctTimeIndex], endtimes[i]) == -1) {
								correctTimeIndex = i;
							}
							
						}
					}
				}
				if(correctTimeIndex ==-1)
					continue;
				String newEndTime = "";
				for(int i=0;i<endtimes.length;i++){
					if(i!=correctTimeIndex){
						if(i==0)
							newEndTime=newEndTime+endtimes[i];
						else {
							newEndTime=newEndTime+","+endtimes[i];
						}
					}
					
					
				}
				endMap.put(key.split(",", -1)[0], newEndTime);
				if (timeCompare(startMap.get(key),
						endtimes[correctTimeIndex])) {

					Date d0 = sdf.parse(startMap.get(key));
					Date d1 = sdf.parse(endtimes[correctTimeIndex]);
					long l = d1.getTime() - d0.getTime();

					if (l > 0) {
						
						if(timeHandle(startMap.get(key), date
								+ " 00:00:00", date + " 03:00:00")){
							defaultcount++;
							defaultSpeed += (distanc * 3600 * 1000) / l;
						}

						if (timeHandle(startMap.get(key), date
								+ " 08:00:00", date + " 09:00:00")
								|| timeHandle(startMap.get(key), date
										+ " 17:00:00", date + " 18:00:00")) {
							MaxCount++;
							MaxTime += l / 60000.0;
						}
						
						if(timeHandle(startMap.get(key), date
								+ " 06:00:00", date + " 22:00:00")){
							flowCount ++;
							
							if(timeHandle(startMap.get(key), date
									+ " 08:00:00", date + " 10:00:00")){
								String bosskey = splitTime(startMap.get(key),1);
								if(flowMap.get(bosskey) == null){
									flowMap.put(bosskey, 1);
								}else{
									flowMap.put(bosskey, (flowMap.get(bosskey)+1));
								}				
							}
						}
						count++;
						allSpeed += (distanc * 3600 * 1000) / l;
						allTime += l / 60000.0;
					}

				}
			}
		}
		//找8点到9点之间 每分钟最大的过车量
		double maxflow = 0;
		Set<String> ks = flowMap.keySet();
		for(String k : ks){
			if(maxflow < flowMap.get(k)){
				maxflow = flowMap.get(k);
			}
		}


		defaultSpeed /=defaultcount; 

		File file = new File(fileOutput+"/"+direction+".txt");
		boolean b = false;
		if (!file.exists()) {
			System.out.println("创建文件");
			b = true;
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
		if(b){
			bw.write("date\tCLT\tPHTT\tRS\tRAV\r\n");
		}
		DecimalFormat df = new DecimalFormat("0.0000");
		
		//权值为饱和度*长度
		bw.write(date.replace("-", "")
				+ "\t"
				+ (max((flowCount / (maxflow * 60))*distanc*((allTime / count) - (distanc / defaultSpeed) * 60),0.01))
				+ "\t" + (flowCount / (maxflow * 60))*distanc*(MaxTime / MaxCount) + "\t"
				+ (flowCount / (maxflow * 60))*distanc*(flowCount / (maxflow * 60)) + "\t" //1最大车流
				+ (flowCount / (maxflow * 60))*distanc*(100/(allSpeed / count)) + "\r\n");
		System.out.println(direction+"计算完毕！");
		/*System.out.println(date
				+ "\t"
				+ (((MaxTime / MaxCount*distanc)*distanc)*(allTime / count) - (distanc / defaultSpeed) * 60)
				+ "\t" + ((MaxTime / MaxCount*distanc)*distanc )*MaxTime / MaxCount + "\t"
				+ (((MaxTime / MaxCount*distanc)*distanc)*flowCount / (maxflow * 60)) + "\t" //1最大车流
				+ (((MaxTime / MaxCount*distanc)*distanc)*100/(allSpeed / count)) + "\r\n");
		*/
		bw.close();
		br.close();
		br0.close();
	}
	//time0小返回-1，相等返回0，time0大返回1
	public static int timeCom(String time0,String time1) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar startTime = Calendar.getInstance();
		Calendar endTime = Calendar.getInstance();

		startTime.setTime(sdf.parse(time0));
		endTime.setTime(sdf.parse(time0));
		if (startTime.after(endTime)) {
			return 1;
		}else if (startTime.before(endTime)) {
			return -1;
		}
		return 0;
		
	}

	public static boolean timeHandle(String time, String STime,
			String ETime) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c0 = Calendar.getInstance();
		Calendar c1 = Calendar.getInstance();
		Calendar t = Calendar.getInstance();

		c0.setTime(sdf.parse(STime));
		c1.setTime(sdf.parse(ETime));
		t.setTime(sdf.parse(time));

		if (t.before(c1) && t.after(c0)) {
			return true;
		}
		return false;
	}

	public static boolean timeCompare(String time0, String time1)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar startTime = Calendar.getInstance();
		Calendar endTime = Calendar.getInstance();
		Calendar compareTime = Calendar.getInstance();

		startTime.setTime(sdf.parse(time0));
		endTime.setTime(sdf.parse(time0));
		endTime.set(Calendar.MINUTE, startTime.get(Calendar.MINUTE) + 5);
		compareTime.setTime(sdf.parse(time1));
		if (compareTime.after(startTime) && compareTime.before(endTime)) {
			return true;
		}
		return false;
	}

	/**
	 * 拆分时间片段,生成每个时间段的key
	 * @param yyyy_MM_dd
	 * @param timePart
	 * @throws ParseException 
	 */
	public static String splitTime(String time,int timePart) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();  
		//生成key
		calendar.setTime(sdf.parse(time));
	    // 当前分钟数  
	    int MINUTE = calendar.get(Calendar.MINUTE);
	    //设置key开始时间的分钟为(MINUTE/timePart) * timePart
	    calendar.set(Calendar.MINUTE,(MINUTE/timePart) * timePart);    
	    calendar.set(Calendar.SECOND, 0);
	    String timePartStart = sdf.format(calendar.getTime());
	    
	    //设置key结束时间的分钟为(1+(MINUTE/timePart)) * timePart
	    calendar.set(Calendar.MINUTE,(1+(MINUTE/timePart)) * timePart);
	    calendar.set(Calendar.SECOND, 0);
	    String timePartend = sdf.format(calendar.getTime());
	    //返回map的key
		return timePartStart+"-"+timePartend.substring(11, 19);
	}
	
//	public static void main(String[] args) throws IOException, ParseException{
//		Properties p = new Properties();
//		p.load(new FileInputStream("Increment.properties"));
//		new FileHandle().carCount("C:/demo/2016-06-21","C:/demo3/",
//				"611471004000-611471003000", p);
// 	}
}
