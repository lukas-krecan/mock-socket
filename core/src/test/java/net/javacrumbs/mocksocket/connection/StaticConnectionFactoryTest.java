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


import static net.javacrumbs.mocksocket.MockSocket.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import net.javacrumbs.mocksocket.connection.data.DefaultSocketData;
import net.javacrumbs.mocksocket.connection.data.SocketData;

import org.junit.After;
import org.junit.Test;
import org.springframework.util.FileCopyUtils;


public class StaticConnectionFactoryTest {

	private static final String ADDRESS1 = "localhost:1111";
	private static final String ADDRESS2 = "localhost:2222";
	private static final SocketData DATA1 = new DefaultSocketData(new byte[]{1,1,1,1});
	private static final SocketData DATA2 = new DefaultSocketData(new byte[]{2,2,2,2});
	private static final SocketData DATA3 = new DefaultSocketData(new byte[]{3,3,3,3});
	private static final SocketData DATA4 = new DefaultSocketData(new byte[]{4,4,4,4});
	private StaticConnectionFactory connectionFactory  = new StaticConnectionFactory();

	@After
	public void resetConnection()
	{
		StaticConnectionFactory.reset();
	}
	
	@Test
	public void testExpectOne() throws IOException
	{
		StaticConnectionFactory.expectCall().andReturn(DATA1);
		
		checkConnection(ADDRESS1,DATA1, DATA4);
		
		assertThat(StaticConnectionFactory.getConnection().requestData().get(0).getBytes(), is(DATA4.getBytes()));
		assertThat(StaticConnectionFactory.getConnection().requestData().size(), is(1));
//		assertTrue(StaticConnectionFactory.getConnection().containsRequestThat(is(DATA4)));
//		assertFalse(StaticConnectionFactory.getConnection().containsRequestThat(is(DATA3)));
	}
	
	@Test(expected=IllegalStateException.class)
	public void testUnknown() throws IOException
	{
		connectionFactory.createConnection(ADDRESS1);
	}
	@Test
	public void testExpectTwo() throws IOException
	{
		StaticConnectionFactory.expectCall().andReturn(DATA2).andReturn(DATA1);
				
		checkConnection(ADDRESS1,DATA2, DATA3);
		checkConnection(ADDRESS1,DATA1, DATA4);
		
		assertThat(StaticConnectionFactory.getConnection().requestData().get(0).getBytes(), is(DATA3.getBytes()));
		assertThat(StaticConnectionFactory.getConnection().requestData().get(1).getBytes(), is(DATA4.getBytes()));
	}
	@Test
	public void testExpectTwoUniversal() throws IOException
	{
		StaticConnectionFactory.expectCall().andReturn(DATA2).andReturn(DATA1);
		
		checkConnection(ADDRESS1,DATA2, DATA3);
		checkConnection(ADDRESS1,DATA1, DATA4);
		
		assertThat(StaticConnectionFactory.getConnection().requestData().get(0).getBytes(), is(DATA3.getBytes()));
		assertThat(StaticConnectionFactory.getConnection().requestData().get(1).getBytes(), is(DATA4.getBytes()));
	}
	@Test(expected=IllegalArgumentException.class)
	public void testExpectTwice() throws IOException
	{
		StaticConnectionFactory.expectCall().andReturn(DATA2);
		StaticConnectionFactory.expectCall().andReturn(DATA1);
	}

	@Test
	public void testMoreRequests() throws IOException
	{
		StaticConnectionFactory.expectCall().andReturn(DATA1);
		
		checkConnection(ADDRESS1,DATA1);
		Connection connection = connectionFactory.createConnection(ADDRESS1);
		try
		{
			connection.getInputStream();
			fail();
		}
		catch(AssertionError e)
		{
			assertThat(e.getMessage(), is("No more connections expected."));
		}
	}

	
	@Test
	public void testWithPayload() throws IOException
	{
		StaticConnectionFactory.expectCall()
			.andWhenRequest(data(is(DATA4.getBytes()))).thenReturn(DATA1)
			.andWhenRequest(data(is(DATA3.getBytes()))).thenReturn(DATA2);
		
		checkConnection(ADDRESS1,DATA1, DATA4);
		checkConnection(ADDRESS1,DATA2, DATA3);
		
		assertThat(StaticConnectionFactory.getConnection().requestData().get(0).getBytes(), is(DATA4.getBytes()));
	
	}
	@Test
	public void testWithAddress() throws IOException
	{
		StaticConnectionFactory.expectCall()
		.andWhenRequest(address(is(ADDRESS1))).thenReturn(DATA1)
		.andWhenRequest(address(is(ADDRESS2))).thenReturn(DATA2);
		
		checkConnection(ADDRESS1,DATA1, DATA4);
		checkConnection(ADDRESS2,DATA2, DATA3);
		
		assertThat(StaticConnectionFactory.getConnection().requestData().get(0).getBytes(), is(DATA4.getBytes()));
		
	}
	@Test
	public void testWithPayloadMultiple() throws IOException
	{
		StaticConnectionFactory.expectCall()
			.andWhenRequest(data(is(DATA4.getBytes()))).thenReturn(DATA1).thenReturn(DATA3)
			.andWhenRequest(data(is(DATA3.getBytes()))).thenReturn(DATA2);
		
		checkConnection(ADDRESS1,DATA1, DATA4);
		checkConnection(ADDRESS1,DATA2, DATA3);
		checkConnection(ADDRESS1,DATA3, DATA4);
		
		assertThat(StaticConnectionFactory.getConnection().requestData().get(0).getBytes(), is(DATA4.getBytes()));
		
	}
	@Test
	public void testUnexpected() throws IOException
	{
		StaticConnectionFactory.expectCall()
			.andWhenRequest(data(is(DATA4.getBytes()))).thenReturn(DATA1);
		checkConnection(ADDRESS1,DATA1, DATA4);
		Connection connection = connectionFactory.createConnection(ADDRESS1);
		connection.getOutputStream().write(DATA3.getBytes());
		try
		{
			connection.getInputStream().read();
			fail("Exception expected");
		}
		catch(AssertionError e)
		{
			assertEquals("No matcher matches request [3, 3, 3, 3]. Do not know which response to return.", e.getMessage());
		}
	}
	@Test
	public void testUnexpectedMultiple() throws IOException
	{
		StaticConnectionFactory.expectCall().andWhenRequest(data(is(DATA4.getBytes()))).thenReturn(DATA1);
		checkConnection(ADDRESS1,DATA1, DATA4);
		Connection connection = connectionFactory.createConnection(ADDRESS1);
		connection.getOutputStream().write(DATA4.getBytes());
		try
		{
			connection.getInputStream().read();
			fail("Exception expected");
		}
		catch(AssertionError e)
		{
			assertTrue(e.getMessage().startsWith("No more connections expected for request matching matcher: "));
		}
	}
	
	private void checkConnection(String address, SocketData outData) throws IOException {
		checkConnection(address, outData, null);
	}
	private void checkConnection(String address, SocketData outData, SocketData inData) throws IOException {
		Connection connection = connectionFactory.createConnection(address);
		assertNotNull(connection);
		if (inData!=null)
		{
			connection.getOutputStream().write(inData.getBytes());
		}
		byte[] actualOutData = FileCopyUtils.copyToByteArray(connection.getInputStream());
		assertThat(actualOutData, is(outData.getBytes()));	
	}
}
