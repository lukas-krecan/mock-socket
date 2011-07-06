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

package net.javacrumbs.mocksocket;

import static net.javacrumbs.mocksocket.MockSocket.address;
import static net.javacrumbs.mocksocket.MockSocket.data;
import static net.javacrumbs.mocksocket.MockSocket.emptyResponse;
import static net.javacrumbs.mocksocket.MockSocket.expectCall;
import static net.javacrumbs.mocksocket.MockSocket.recordedConnections;
import static net.javacrumbs.mocksocket.MockSocket.reset;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.net.Socket;

import javax.net.SocketFactory;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Test;

public class SampleTest {

	@Test
	public void testResponse() throws Exception
	{
		byte[] mockData = new byte[]{1,2,3,4};
		expectCall().andReturn(data(mockData));
		
		Socket socket = SocketFactory.getDefault().createSocket("example.org", 1234);
		byte[] data = IOUtils.toByteArray(socket.getInputStream());
		socket.close();
		assertThat(data, is(mockData));
	}
	
	@Test
	public void testMultiple() throws Exception
	{
		byte[] mockData1 = new byte[]{1,2,3,4};
		byte[] mockData2 = new byte[]{1,2,3,4};
		expectCall().andReturn(data(mockData1)).andReturn(data(mockData2));
		
		Socket socket1 = SocketFactory.getDefault().createSocket("example.org", 1234);
		byte[] data1 = IOUtils.toByteArray(socket1.getInputStream());
		socket1.close();
		assertThat(data1, is(mockData1));

		Socket socket2 = SocketFactory.getDefault().createSocket("example.org", 1234);
		byte[] data2 = IOUtils.toByteArray(socket2.getInputStream());
		socket2.close();
		assertThat(data2, is(mockData2));
	}
	
	@Test
	public void testConditionalAddress() throws Exception
	{
		byte[] mockData = new byte[]{1,2,3,4};
		expectCall().andWhenRequest(address(is("example.org:1234"))).thenReturn(data(mockData));
		
		Socket socket = SocketFactory.getDefault().createSocket("example.org", 1234);
		byte[] data = IOUtils.toByteArray(socket.getInputStream());
		socket.close();
		assertThat(data, is(mockData));
	}
	
	@Test
	public void testConditionalData() throws Exception
	{
		byte[] dataToWrite = new byte[]{5,4,3,2};
		byte[] mockData = new byte[]{1,2,3,4};
		expectCall().andWhenRequest(data(is(dataToWrite))).thenReturn(data(mockData));
		
		Socket socket = SocketFactory.getDefault().createSocket("example.org", 1234);
		IOUtils.write(dataToWrite, socket.getOutputStream());
		byte[] data = IOUtils.toByteArray(socket.getInputStream());
		socket.close();
		assertThat(data, is(mockData));
	}
	
	@Test
	public void testRequest() throws Exception
	{
		byte[] dataToWrite = new byte[]{5,4,3,2};
		expectCall().andReturn(emptyResponse());
		
		Socket socket = SocketFactory.getDefault().createSocket("example.org", 1234);
		IOUtils.write(dataToWrite, socket.getOutputStream());
		socket.close();
		assertThat(recordedConnections().get(0), data(is(dataToWrite)));
		assertThat(recordedConnections().get(0), address(is("example.org:1234")));
	}

	
	@After
	public void tearDown()
	{
		reset();
	}
}
