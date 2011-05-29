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

public class HttpData {
	private static final String UTF8 = "UTF-8";

	private final byte[] data;
	
	private static boolean printAsString = true;

	public HttpData(byte[] data) {
		this.data = data;
	}
	

	public HttpData(String data) {
		this(data, UTF8);
	}

	public HttpData(String data, String charset) {
		this(stringToBytes(data, charset));
	}

	private static byte[] stringToBytes(String data, String charset)  {
		try {
			return data.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}
	

	public byte[] getBytes() {
		return data;
	}

	@Override
	public String toString() {
		if (printAsString)
		{
			return getAsString(UTF8);
		}
		else
		{
			return Arrays.toString(data);
		}
	}

	public String getAsString(String encoding) {
		try {
			return new String(data, UTF8);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(data);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HttpData other = (HttpData) obj;
		if (!Arrays.equals(data, other.data))
			return false;
		return true;
	}


	public static synchronized boolean isPrintAsString() {
		return printAsString;
	}


	public static synchronized void setPrintAsString(boolean printAsString) {
		HttpData.printAsString = printAsString;
	}

}
