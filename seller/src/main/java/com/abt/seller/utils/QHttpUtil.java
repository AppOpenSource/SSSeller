package com.abt.seller.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QHttpUtil {
	/**
	 * Return the MIME type based on the specified file name.
	 */
	public static String getContentType(String fileName) {
		// return new MimetypesFileTypeMap().getContentType(fileName);
		return null;
	}

	/**
	 * Return the MIME type based on the specified file name.
	 */
	public static String getContentType(File file) {
		// return new MimetypesFileTypeMap().getContentType(file);
		return null;
	}

	/**
	 * Return the list of query parameters based on the specified query string.
	 */
	public static List<QParameter> getQueryParameters(String queryString) {
		if (queryString.startsWith("?")) {
			queryString = queryString.substring(1);
		}
		List<QParameter> result = new ArrayList<QParameter>();
		if (queryString != null && !queryString.equals("")) {
			String[] p = queryString.split("&");
			for (String s : p) {
				if (s != null && !s.equals("")) {
					if (s.indexOf('=') > -1) {
						String[] temp = s.split("=");
						result.add(new QParameter(temp[0], temp[1]));
					}
				}
			}
		}
		return result;
	}

	public static String formParamDecode(String value) {
		int nCount = 0;
		for (int i = 0; i < value.length(); i++) {
			if (value.charAt(i) == '%') {
				i += 2;
			}
			nCount++;
		}

		byte[] sb = new byte[nCount];
		for (int i = 0, index = 0; i < value.length(); i++) {
			if (value.charAt(i) != '%') {
				sb[index++] = (byte) value.charAt(i);
			} else {
				StringBuilder sChar = new StringBuilder();
				sChar.append(value.charAt(i + 1));
				sChar.append(value.charAt(i + 2));
				sb[index++] = Integer.valueOf(sChar.toString(), 16).byteValue();
				i += 2;
			}
		}
		String decode = "";
		try {
			decode = new String(sb, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return decode;
	}

	public static boolean isEmpty(String str) {
		if (null == str || str.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	public static Map<String, String> splitResponse(String response) {
		Map<String, String> map = new HashMap<String, String>();
		if (!QHttpUtil.isEmpty(response)) {
			String[] array = response.split("&");
			if (array.length > 2) {
				String tokenStr = array[0];
				String secretStr = array[1];
				String[] token = tokenStr.split("=");
				if (token.length == 2) {
					map.put("oauth_token", token[1]);
				}
				String[] secret = secretStr.split("=");
				if (secret.length == 2) {
					map.put("oauth_token_secret", secret[1]);
				}
			}
		}
		return map;
	}
}