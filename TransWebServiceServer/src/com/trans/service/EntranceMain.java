package com.trans.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @author  作�?? E-mail: 
 * @date 创建时间�?2016�?9�?5�? 下午4:45:38
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class EntranceMain {

	private String fileFolder;
	private String fileOutPut;
	private Properties conf;
	private ThreadPoolExecutor pool;
	
	public EntranceMain(String fileFolder,String fileOutPut) throws IOException {
		this.fileFolder = fileFolder;
		this.fileOutPut = fileOutPut;
		this.conf = new Properties();
		conf.load(new FileInputStream("Increment.properties"));
		this.pool = new ThreadPoolExecutor(10, 20, 2000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	}
	
	class MyThread implements Runnable{
		
		private String direction;
		
		public MyThread(String direction) {
			this.direction = direction;
		}
		@Override
		public void run() {
			try {
				//计算每天�?
				//new FileHandleByDay().carCount(fileFolder,fileOutPut,
				//		direction, conf);
				//计算小时�?
				new FileHandleByHour().carCount(fileFolder,fileOutPut,
						direction, conf);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	public void action(){
		List<String> list = getFilesName(fileFolder);
		for(int i = 0; i < list.size(); i++){
			pool.execute(new MyThread(list.get(i)));
		}
		pool.shutdown();
	}
	/**
	 * 将文件夹下所有文件（file）的文件名两两组�?
	 * @param FolderPath
	 * @return
	 */
	public static List<String> getFilesName(String FolderPath){
		List<String> list = new ArrayList<String>();
		File file = new File(FolderPath);
		File[] Names = file.listFiles();
		for(int i=0;i<Names.length;i++){
			for(int j=0;j<Names.length;j++){
				String fileCombination = Names[i].getName()+"-"+Names[j].getName();
				list.add(fileCombination);
			}
			
		}
		return list;
	}
	
	/*
	public static void main(String[] args) throws IOException {
		
		String s1="2016-09-11";

		String s2="2016-09-25";

		DateFormat df=new java.text.SimpleDateFormat("yyyy-MM-dd");

		Calendar startdate=Calendar.getInstance();

		Calendar enddate=Calendar.getInstance();
		try {
			startdate.setTime(df.parse(s1));
			enddate.setTime(df.parse(s2));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		int res = startdate.compareTo(enddate);
		System.out.println(res);
		while(res<=0){
			String curDate = df.format(startdate.getTime());
			new EntranceMain("F:/NTTDATA/"+curDate,"F:/resultByMinute5/").action();
			startdate.add(Calendar.DAY_OF_MONTH, 1);
			res = startdate.compareTo(enddate);
		}
		
		//new EntranceMain("F:/NTTDATA/2016-06-05","F:/result/").action();
	}
	*/
}


