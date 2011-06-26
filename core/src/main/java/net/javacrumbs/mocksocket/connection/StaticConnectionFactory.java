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
import java.net.Socket;
import java.net.SocketImplFactory;
import java.util.HashMap;
import java.util.Map;

import net.javacrumbs.mocksocket.socket.MockSocketImplFactory;


/**
 * Stores connections in a static field. It is NOT threads safe so you can not execute multiple tests in parallel. 
 * You also can not use it if there is a {@link SocketImplFactory} already set.
 * @author Lukas Krecan
 * @see Socket#setSocketImplFactory(SocketImplFactory)
 */
public class StaticConnectionFactory implements ConnectionFactory {
	private static Map<String, UniversalMockConnection> expectedConnections = new HashMap<String, UniversalMockConnection>();

	static 
	{
		bootstrap();
	}
	
	static void bootstrap()
	{
		try {
			Socket.setSocketImplFactory(new MockSocketImplFactory(new StaticConnectionFactory()));
		} catch (IOException e) {
			throw new IllegalStateException("Can not bootstrap the connection factory",e);
		}
	}
	
	public synchronized Connection createConnection(String address) {
		UniversalMockConnection connection = expectedConnections.get(address);
		if (connection==null)
		{
			throw new IllegalStateException("Connection to "+address+" not expected. You have to call expectCallTo(\""+address+"\") first.");
		}
		connection.onCreate(address);
		return connection;
	}
	
	public synchronized static UniversalMockRecorder expectCallTo(String address) {
		if (getConnectionTo(address)==null)
		{
			UniversalMockConnection mockConnection = new UniversalMockConnection(address);
			addExpectedConnection(address, mockConnection);
			return mockConnection;
		}
		else
		{
			throw new IllegalArgumentException("Can not call expectCall twice. You have to call reset before each test. If you need simulate multiple requests, please call andReturn several times.");
		}
	}

	protected synchronized static void addExpectedConnection(String address, UniversalMockConnection mockConnection) {
		expectedConnections.put(address, mockConnection);
	}
	
	public synchronized static void reset()
	{
		expectedConnections.clear();
	}

	
	public synchronized static MockConnection getConnectionTo(String address) {
		return expectedConnections.get(address);
	}
}
