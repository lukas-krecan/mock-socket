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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.javacrumbs.mocksocket.connection.data.OutputSocketData;
import net.javacrumbs.mocksocket.connection.data.RequestSocketData;


/**
 * Common code for creating MockConnections.
 * @author Lukas Krecan
 *
 */
public abstract class AbstractMockConnectionFactory implements RequestRecorder, ConnectionFactory {

	private final List<OutputSocketData> requestData = new ArrayList<OutputSocketData>();
	protected int actualConnection = -1;
	
	public synchronized Connection createConnection(String address) {
		actualConnection++;
		requestData.add(createRequestSocket(address));
		
		return new DefaultConnection(createInputStream(), getRequestSocketData().getOutputStream());
	}

	protected OutputSocketData createRequestSocket(String address) {
		return new OutputSocketData(address);
	}

	protected abstract InputStream createInputStream();

	protected synchronized OutputSocketData getRequestSocketData() {
		return requestData.get(actualConnection);
	}
	
	public synchronized List<RequestSocketData> requestData() {
		return new ArrayList<RequestSocketData>(requestData);
	}
}