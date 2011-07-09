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

package net.javacrumbs.mocksocket.connection.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import net.javacrumbs.mocksocket.connection.StringUtils;

public class OutputSocketData implements RequestSocketData {
	
	private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	
	private final String address;
	
	public OutputSocketData(String address) {
		this.address = address;
	}

	public InputStream getData() {
		return new ByteArrayInputStream(getDataAsBytes());
	}
	
	protected byte[] getDataAsBytes()
	{
		return outputStream.toByteArray();
	}
	
	public OutputStream getOutputStream() {
		return outputStream;
	}

	public String getAddress() {
		return address;
	}
	
	@Override
	public String toString() {
		return StringUtils.convertDataToString(getDataAsBytes());
	}

}
