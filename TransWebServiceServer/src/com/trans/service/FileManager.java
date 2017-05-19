package com.trans.service;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



public class FileManager implements Runnable{
	//key：时间(天)+路段，value:查询的时间
	static Map<String, Date> dateUsedMap = new HashMap<String , Date>();
	static Map<String, DataStruct> temporaryData = new HashMap<String , DataStruct>();
	
	/*���ڼ��map�е��Ƿ����ʹ�ã�û�о�ɾ��
	 * ��һ������Ϊָ����/Сʱ/����/��
	 * �ڶ�������ָ�����
	 * */
	private String DHMS;
	private int interval;
	
	public FileManager(Map<String, Date> dateUsedMap, String dHMS, int interval) {
		super();
		this.dateUsedMap = dateUsedMap;
		DHMS = dHMS;
		this.interval = interval;
	}
	public String getDHMS() {
		return DHMS;
	}
	public void setDHMS(String dHMS) {
		DHMS = dHMS;
	}
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}
	
	private void checkMap(String DHMS,int interval) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date now = new Date();
		Calendar tmp = Calendar.getInstance();
		for (Map.Entry<String, Date> entry : dateUsedMap.entrySet()) {  
			 Date datetmp = dateUsedMap.get(entry.getKey());
			 tmp.setTime(datetmp);
			 switch (DHMS) {
			case "DAY":
				tmp.add(Calendar.DAY_OF_MONTH, interval);
				break;
			case "HOUR":
				tmp.add(Calendar.HOUR_OF_DAY, interval);
				break;
			case "MINUTE":
				tmp.add(Calendar.MINUTE, interval);
				break;
			case "SECOND":
				tmp.add(Calendar.SECOND, interval);
				break;
			default:
				break;
			}
			Date datetmp2 = tmp.getTime();
			if (now.getTime() > datetmp2.getTime()) {
				dateUsedMap.remove(entry.getKey());
				FileManager.temporaryData.remove(entry.getKey());
			}
		  
		}  
	}
	public void run() {
		while(true){
			checkMap(DHMS,interval);
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
