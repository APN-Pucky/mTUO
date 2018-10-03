package de.neuwirthinformatik.Alexander.mTUO;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Wget {
	public static enum Status {
		Success, MalformedUrl, IoException, UnableToCloseOutputStream;
	}
	

	public static String sendGet(String url) {
		String ret = null;
		BufferedReader in = null;
		try {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header

		String USER_AGENT = "Mozilla/5.0";
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		//System.out.println("\nSending 'GET' request to URL : " + url);
		//System.out.println("Response Code : " + responseCode);

		in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}

		//print result
		ret = response.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				return null;
			}
		}
		return ret;
	}


	public static String wGet(String urlOfFile) {
		String ret = null;
		InputStream httpIn = null;
		OutputStream fileOutput = null;
		OutputStream bufferedOut = null;
		try {
			// check the http connection before we do anything to the fs
			httpIn = new BufferedInputStream(new URL(urlOfFile).openStream());
			// prep saving the file
			ret = StreamUtil.readFullyAsString(httpIn, "UTF-8");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (bufferedOut != null) {
					bufferedOut.close();
				}
				if (fileOutput != null) {
					fileOutput.close();
				}
				if (httpIn != null) {
					httpIn.close();
				}
			} catch (IOException e) {
				return null;
			}
		}
		return ret;
	}

	//public static void wGetAsync(final String saveAsFile, final String urlOfFile) {wGet(saveAsFile,urlOfFile);}});}
	public static Wget.Status wGet(String saveAsFile, String urlOfFile) {
		File yourFile = new File(saveAsFile);
		InputStream httpIn = null;
		OutputStream fileOutput = null;
		OutputStream bufferedOut = null;
		Log.d("TUO_WEB",saveAsFile);
		try {
			yourFile.getParentFile().mkdirs();
			yourFile.createNewFile(); // if file already exists will do nothing
			// check the http connection before we do anything to the fs
			httpIn = new BufferedInputStream(new URL(urlOfFile).openStream());
			// prep saving the file
			fileOutput = new FileOutputStream(saveAsFile);
			bufferedOut = new BufferedOutputStream(fileOutput, 1024);
			byte data[] = new byte[1024];
			boolean fileComplete = false;
			int count = 0;
			while (!fileComplete) {
				count = httpIn.read(data, 0, 1024);
				if (count <= 0) {
					fileComplete = true;
				} else {
					bufferedOut.write(data, 0, count);
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return Wget.Status.MalformedUrl;
		} catch (IOException e) {
			e.printStackTrace();
			return Wget.Status.IoException;
		} finally {
			try {
				if (bufferedOut != null) {
					bufferedOut.close();
				}
				if (fileOutput != null) {
					fileOutput.close();
				}
				if (httpIn != null) {
					httpIn.close();
				}
			} catch (IOException e) {
				return Wget.Status.UnableToCloseOutputStream;
			}
		}
		return Wget.Status.Success;
	}
}
