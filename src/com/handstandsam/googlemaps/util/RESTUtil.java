package com.handstandsam.googlemaps.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class RESTUtil {

	private static final String UTF_8 = "UTF-8";

	public static String createParamString(Map<String, String> params) {
		StringBuilder sb = new StringBuilder();
		if (params != null) {
			List<String> keys = new ArrayList<String>(params.keySet());
			for (int i = 0; i < keys.size(); i++) {
				String key = keys.get(i);
				if (i > 0) {
					sb.append("&");
				}
				String value = params.get(key);
				String keyValuePair = key + "=" + value;
				sb.append(keyValuePair);
			}
		}
		return sb.toString();
	}

	public static String createFullUrl(String baseUrl,
			Map<String, String> params) {
		String fullUrl = baseUrl;
		if (params != null) {
			String paramsString = createParamString(params);
			if (paramsString.length() > 0) {
				fullUrl = baseUrl + "?" + paramsString;
			}
		}
		return fullUrl;
	}

	public static String getBaseUrl(HttpServletRequest request) {
		String uri = request.getRequestURI();
		String url = request.getRequestURL().toString();

		String baseUrl = url.replace(uri, "");
		return baseUrl;
	}

	public static String encode(String string) {
		try {
			return URLEncoder.encode(string, UTF_8);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static String getRequestPostBody(HttpServletRequest req)
			throws IOException {
		InputStream is = req.getInputStream();

		StringBuffer body = new StringBuffer();
		String line = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(is, UTF_8));
		while ((line = br.readLine()) != null) {
			body.append(line + "\n");
		}
		return body.toString();
	}
}
