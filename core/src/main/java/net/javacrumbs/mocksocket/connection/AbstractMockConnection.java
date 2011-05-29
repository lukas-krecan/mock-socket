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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;


/**
 * Common code for MockConnections.
 * @author Lukas Krecan
 *
 */
public abstract class AbstractMockConnection implements MockConnection{

	private final List<ByteArrayOutputStream> requestData = new ArrayList<ByteArrayOutputStream>();
	protected int actualConnection = -1;

	public void onCreate() {
		actualConnection++;
		requestData.add(new ByteArrayOutputStream());
	}

	public ByteArrayOutputStream getOutputStream() throws IOException {
		return requestData.get(actualConnection);
	}
	
	public List<SocketData> requestData() {
		List<SocketData> result = new ArrayList<SocketData>();
		for (ByteArrayOutputStream data : requestData) {
			result.add(createSocketData(data.toByteArray()));
		}
		return result;
	}

	protected SocketData createSocketData(byte[] data) {
		return new SocketData(data);
	}

	protected int getActualConnection() {
		return actualConnection;
	}

	protected void setActualConnection(int actualConnection) {
		this.actualConnection = actualConnection;
	}
	
	public boolean containsRequestThat(Matcher<? extends Object> matcher){
		for (ByteArrayOutputStream data : requestData) {
			if (matcher.matches(data.toByteArray()))
			{
				return true;
			}
		}
		return false;
	}

}