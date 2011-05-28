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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.javacrumbs.mocksocket.AbstractMockSocketImpl;

/**
 * MockSocket impl that uses a {@link ConnectionFactory}. 
 * @author Lukas Krecan
 *
 */
public class ConnectionFactoryMockSocketImpl extends AbstractMockSocketImpl {
	private final ConnectionFactory connectionFactory;
	private Connection connection;
	
	public ConnectionFactoryMockSocketImpl(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	@Override
	protected void onConnect(String address) {
		connection = connectionFactory.createConnection(address);
	}

	@Override
	protected InputStream getInputStream() throws IOException {
		assertConnection();
		return connection.getInputStream();
	}

	@Override
	protected OutputStream getOutputStream() throws IOException {
		assertConnection();
		return connection.getOutputStream();
	}

	private void assertConnection() {
		if (connection == null)
		{
			throw new IllegalStateException("Connection is not open");
		}
		
	}

}
