/**
 * Copyright 2009-2011 the original author or authors.
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.javacrumbs.mocksocket.connection.data.OutputSocketData;
import net.javacrumbs.mocksocket.connection.data.RequestSocketData;
import net.javacrumbs.mocksocket.connection.data.SocketData;

import org.hamcrest.Matcher;


/**
 * Common code for MockConnections.
 * @author Lukas Krecan
 *
 */
public abstract class AbstractMockConnection implements MockConnection{

	private final List<OutputSocketData> requestData = new ArrayList<OutputSocketData>();
	protected int actualConnection = -1;
	private InputStream inputStream;
	
	public void onCreate(String address) {
		actualConnection++;
		requestData.add(createRequestSocket(address));
		inputStream = null;
	}

	protected OutputSocketData createRequestSocket(String address) {
		return new OutputSocketData(address);
	}

	public OutputStream getOutputStream() throws IOException {
		return getRequestSocketData().getOutputStream();
	}
	
	public final InputStream getInputStream() throws IOException
	{
		if (inputStream==null)
		{
			inputStream = createInputStream();
		}
		return inputStream;
	}

	protected abstract InputStream createInputStream() throws IOException;

	protected OutputSocketData getRequestSocketData() {
		return requestData.get(actualConnection);
	}
	
	public List<RequestSocketData> requestData() {
		return new ArrayList<RequestSocketData>(requestData);
	}

	protected int getActualConnection() {
		return actualConnection;
	}

	protected void setActualConnection(int actualConnection) {
		this.actualConnection = actualConnection;
	}
	
	public boolean containsRequestThat(Matcher<RequestSocketData> matcher){
		for (SocketData data : requestData) {
			if (matcher.matches(data))
			{
				return true;
			}
		}
		return false;
	}

}