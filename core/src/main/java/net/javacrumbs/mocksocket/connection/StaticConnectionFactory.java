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

import net.javacrumbs.mocksocket.socket.MockSocketImplFactory;


/**
 * Stores connections in a static field. It is NOT threads safe so you can not execute multiple tests in parallel. 
 * You also can not use it if there is a {@link SocketImplFactory} already set.
 * @author Lukas Krecan
 * @see Socket#setSocketImplFactory(SocketImplFactory)
 */
public class StaticConnectionFactory implements ConnectionFactory {
	private static UniversalMockConnectionFactory expectedConnection;

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
		UniversalMockConnectionFactory connectionFactory = expectedConnection;
		if (connectionFactory==null)
		{
			throw new IllegalStateException("Connection not expected. You have to call expectCall() first.");
		}
		return connectionFactory.create(address);
	}
	
	public synchronized static UniversalMockRecorder expectCall() {
		if (getConnection()==null)
		{
			UniversalMockConnectionFactory mockConnection = new UniversalMockConnectionFactory();
			setExpectedConnection(mockConnection);
			return mockConnection;
		}
		else
		{
			throw new IllegalArgumentException("Can not call expectCall twice. You have to call reset before each test. If you need simulate multiple requests, please call andReturn several times.");
		}
	}
	
	public synchronized static void reset()
	{
		expectedConnection = null;
	}


	protected static void setExpectedConnection(UniversalMockConnectionFactory mockConnection) {
		expectedConnection = mockConnection;
	}
	
	public synchronized static RequestRecorder getConnection() {
		return expectedConnection;
	}
}
