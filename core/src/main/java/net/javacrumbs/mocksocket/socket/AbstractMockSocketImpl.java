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
package net.javacrumbs.mocksocket.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;

public abstract class AbstractMockSocketImpl extends SocketImpl {
	
	/**
	 * Onvoked on a connect method call.
	 * @param address host:port
	 */
	protected abstract void onConnect(String address);
	
	public void setOption(int optID, Object value) throws SocketException {
		
	}

	public Object getOption(int optID) throws SocketException {
		return null;
	}

	@Override
	protected void create(boolean stream) throws IOException {

	}

	@Override
	protected void connect(String host, int port) throws IOException {
		onConnect(host+":"+port);
	}

	

	@Override
	protected void connect(InetAddress address, int port) throws IOException {
		connect(address.getHostAddress(), port);
	}

	@Override
	protected void connect(SocketAddress address, int timeout) throws IOException {
		if (address instanceof InetSocketAddress)
		{
			InetSocketAddress inetSocketAddress = (InetSocketAddress) address;
			connect(inetSocketAddress.getHostName(), inetSocketAddress.getPort());
		}
		else
		{
			onConnect(address.toString());
		}
	}

	@Override
	protected void bind(InetAddress host, int port) throws IOException {
		
	}

	@Override
	protected void listen(int backlog) throws IOException {
	
	}

	@Override
	protected void accept(SocketImpl s) throws IOException {

	}

	
	@Override
	protected int available() throws IOException {
		return 0;
	}

	@Override
	protected void sendUrgentData(int data) throws IOException {
	
	}
	
	@Override
	protected void close() throws IOException {
				
	}

}
