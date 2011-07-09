/*
 * Copyright 2005-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.javacrumbs.mocksocket.connection;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class StringUtils {
	private static boolean printDataAsString = true;
	
	private static String defaultEncoding = "UTF-8";
	
	public static synchronized boolean isPrintDataAsString() {
		return printDataAsString;
	}

	/**
	 * Should be data printed as string or as byte array.
	 * @param printDataAsString
	 */
	public static synchronized void setPrintDataAsString(boolean printDataAsString) {
		StringUtils.printDataAsString = printDataAsString;
	}

	public static synchronized String getDefaultEncoding() {
		return defaultEncoding;
	}

	public static synchronized void setDefaultEncoding(String defaultEncoding) {
		StringUtils.defaultEncoding = defaultEncoding;
	}

	public static String convertDataToString(byte[] data) {
		if (printDataAsString)
		{
			try {
				return new String(data, defaultEncoding);
			} catch (UnsupportedEncodingException e) {
				return new String(data);
			}
		}
		else
		{
			return Arrays.toString(data);
		}
	}
}
