
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.axis.utils.IOUtils;

public class Connect{

	public static int retry = 0;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ArrayList<String> extractLocation_Testing(String proxyName,String portNo,String url,int fileIndex) {
		int connectTimeout = 240000,readTimeout = 60000;
		System.setProperty("http.proxyHost", proxyName);
		if(portNo != null)
			System.setProperty("http.proxyPort", portNo);

		InputStream in = null;
		URL conn = null;
		try {

			ArrayList result = new ArrayList();
			byte[] reply = new byte[4096];
			int bytesRead;
			String content = "";

			conn = new URL(url);
			URLConnection con = conn.openConnection();
			con.setConnectTimeout(connectTimeout);
			con.setReadTimeout(readTimeout);
			in = con.getInputStream();

			String inputStreamString = new Scanner(in,"UTF-8").useDelimiter("\\A").next();
			content += inputStreamString;

			int length = content.length();
			
			if(content.length() <= 0 && retry<3){
				
				try{
					Thread.sleep(1000);
				}catch (InterruptedException e) {
					e.printStackTrace();
				}
				retry++;
				System.out.println("Retrying..");
				extractLocation_Testing(proxyName,portNo,url,fileIndex);
				
				in.close();
				
			}else{
			String loc  = extractLocation(content);

			result.add(length);
			result.add(loc);

			in.close();
			}

			return result;

		}
		catch(Exception e){
			e.printStackTrace();
			try {
				in.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return null;

		}

	}


	public static String extractLocation(String page) {
		Matcher matcher = Pattern.compile("class=\"locality\">\\s*(.*?)\\s*</span>").matcher(page);
		if(matcher.find()){
			//		  System.out.println("\nLocation: " + matcher.group(1));
			return matcher.group(1);
		}
		else
			return null;
	}
	@SuppressWarnings("unused")
	public static void HitURLS(ArrayList<String> urls){
		System.out.println("Testing "+urls.size()+" URL's");
		int fileIndex=0;
		ArrayList<String> proxies = new ArrayList<String>();
		proxies.add("");
		proxies.add("us-il.proxymesh.com");
		proxies.add("uk.proxymesh.com");
		proxies.add("open.proxymesh.com");

		for(String proxyName : proxies){

			String portNo ="31280";
			
			if(proxyName == "");
				portNo="";
				
			int time = 10000;
			ArrayList<String> Location1 = new ArrayList<String>();
			ArrayList<String> Location2 = new ArrayList<String>();
			ArrayList<Integer> fetchSuccess1 = new ArrayList<Integer>();
			ArrayList<Integer> fetchSuccess2= new ArrayList<Integer>();

			for(String url : urls){

				List<?> result1 = new ArrayList();
				List<?> result2 = new ArrayList();
				String Loc1 = null,Loc2 = null;
				int length1 = 0 , length2 = 0;
				System.out.println(url);
				try{

					result1 = extractLocation_Testing(proxyName,portNo,url,fileIndex);
					length1 = (Integer) result1.get(0);

					if(length1 > 1)
						fetchSuccess1.add(length1);

					Location1.add((String) result1.get(1));
				}catch(NullPointerException e){
					continue;
				}

//				try{
//					Thread.sleep(time);
//					time = (int) (time+ Math.random());
//				}catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//
//				try{
//
//					result2 = extractLocation_Testing("","",url,fileIndex);
//					Location2.add(Loc2);
//					length2 = (Integer) result2.get(0);
//
//					if(length2 > 1)
//						fetchSuccess2.add(length2);
//
//				}catch(NullPointerException e){
//					continue;
//				}
//
				try{
					Thread.sleep(time);
					time = (int) (time+ Math.random());
				}catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

			int s1 = urls.size();
			int s2 = fetchSuccess1.size();
			int s3 = fetchSuccess2.size();

			System.out.println(proxyName);
			System.out.println("\t No of pages fetched : "+ fetchSuccess1.size());
			System.out.println("\t No of pages not fetched : "+ (s1 - s2));
			System.out.println("\t No of locations that are non null : "+ Location1.size());

//			System.out.println("No Proxy");
//			System.out.println("\t No of pages fetched : "+ fetchSuccess2.size());
//			System.out.println("\t No of pages not fetched : "+ (s1 - s3));
//			System.out.println("\t No of locations that are non null : "+ Location2.size());
		}

	}

	public static void main(String args[]){

		FetchURL fetch = new FetchURL();
		ArrayList<String> URLS = new ArrayList<String>();
		URLS = fetch.FetchURLs();
		HitURLS(URLS);

	}
}