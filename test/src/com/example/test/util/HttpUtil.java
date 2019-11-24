package com.example.test.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
	
	public static void sendHttpRequest(final String address,
			final HttpCallbackListener listener) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection = null;//HttpURLConnection����Ҫ�����������˷���Http����
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");//��������ʽ
					connection.setConnectTimeout(8000);// 8�� ���������ĳ�ʱʱ�䣨��λ�����룩
					connection.setReadTimeout(8000);// 8�� ��������ȡ���ݵĳ�ʱʱ�䣨��λ�����룩
					InputStream in = connection.getInputStream();//�õ�һ��������
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					if (listener != null) {
						// �ص�onFinish()����
						listener.onFinish(response.toString());
					}
				} catch (Exception e) {
					if (listener != null) {
						// �ص�onError()����
						listener.onError(e);
					}
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();
	}

}