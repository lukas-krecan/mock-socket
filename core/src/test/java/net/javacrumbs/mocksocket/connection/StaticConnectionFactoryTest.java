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


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.After;
import org.junit.Test;
import org.springframework.util.FileCopyUtils;


public class StaticConnectionFactoryTest {

	private static final String ADDRESS1 = "localhost:1111";
	private static final String ADDRESS2 = "localhost:2222";
	private static final byte[] DATA1 = new byte[]{1,1,1,1};
	private static final byte[] DATA2 = new byte[]{2,2,2,2};
	private static final byte[] DATA3 = new byte[]{3,3,3,3};
	private static final byte[] DATA4 = new byte[]{4,4,4,4};
	private StaticConnectionFactory connectionFactory  = new StaticConnectionFactory();

	@After
	public void resetConnection()
	{
		StaticConnectionFactory.reset();
	}
	
	@Test
	public void testExpectOne() throws IOException
	{
		StaticConnectionFactory.expectCallTo(ADDRESS1).andReturn(DATA1);
		
		checkConnection(ADDRESS1,DATA1, DATA4);
		
		assertThat(StaticConnectionFactory.connection(ADDRESS1).requestData().get(0), is(DATA4));
		assertThat(StaticConnectionFactory.connection(ADDRESS1).requestData().size(), is(1));
//		assertTrue(StaticConnectionFactory.connection(ADDRESS1).containsRequestThat(is(DATA4)));
//		assertFalse(StaticConnectionFactory.connection(ADDRESS1).containsRequestThat(is(DATA3)));
	}
	
	@Test(expected=IllegalStateException.class)
	public void testUnknown() throws IOException
	{
		connectionFactory.createConnection(ADDRESS1);
	}
	@Test
	public void testExpectTwo() throws IOException
	{
		StaticConnectionFactory.expectCallTo(ADDRESS1).andReturn(DATA2).andReturn(DATA1);
				
		checkConnection(ADDRESS1,DATA2, DATA3);
		checkConnection(ADDRESS1,DATA1, DATA4);
		
		assertThat(StaticConnectionFactory.connection(ADDRESS1).requestData().get(0), is(DATA3));
		assertThat(StaticConnectionFactory.connection(ADDRESS1).requestData().get(1), is(DATA4));
	}
	@Test(expected=IllegalArgumentException.class)
	public void testExpectTwice() throws IOException
	{
		StaticConnectionFactory.expectCallTo(ADDRESS1).andReturn(DATA2);
		StaticConnectionFactory.expectCallTo(ADDRESS1).andReturn(DATA1);
	}
	@Test
	public void testExpectDifferentAddress() throws IOException
	{
		StaticConnectionFactory.expectCallTo(ADDRESS1).andReturn(DATA1).andReturn(DATA2);
		StaticConnectionFactory.expectCallTo(ADDRESS2).andReturn(DATA3).andReturn(DATA4);
		
		checkConnection(ADDRESS1,DATA1);
		checkConnection(ADDRESS2,DATA3);
		checkConnection(ADDRESS1,DATA2);
		checkConnection(ADDRESS2,DATA4);
	}
	@Test
	public void testMoreRequests() throws IOException
	{
		StaticConnectionFactory.expectCallTo(ADDRESS1).andReturn(DATA1);
		
		checkConnection(ADDRESS1,DATA1);
		Connection connection = connectionFactory.createConnection(ADDRESS1);
		try
		{
			connection.getInputStream();
			fail();
		}
		catch(AssertionError e)
		{
			assertThat(e.getMessage(), is("No more connections expected to \"localhost:1111\"."));
		}
	}

	
	@Test
	public void testWithPayload() throws IOException
	{
		StaticConnectionFactory.expectCallTo(ADDRESS1)
			.andWhenPayload(is(DATA4)).thenReturn(DATA1)
			.andWhenPayload(is(DATA3)).thenReturn(DATA2);
		
		checkConnection(ADDRESS1,DATA1, DATA4);
		checkConnection(ADDRESS1,DATA2, DATA3);
		
		assertThat(StaticConnectionFactory.connection(ADDRESS1).requestData().get(0), is(DATA4));
	
	}
	@Test
	public void testWithPayloadMultiple() throws IOException
	{
		StaticConnectionFactory.expectCallTo(ADDRESS1)
			.andWhenPayload(is(DATA4)).thenReturn(DATA1).thenReturn(DATA3)
			.andWhenPayload(is(DATA3)).thenReturn(DATA2);
		
		checkConnection(ADDRESS1,DATA1, DATA4);
		checkConnection(ADDRESS1,DATA2, DATA3);
		checkConnection(ADDRESS1,DATA3, DATA4);
		
		assertThat(StaticConnectionFactory.connection(ADDRESS1).requestData().get(0), is(DATA4));
		
	}
	@Test
	public void testUnexpected() throws IOException
	{
		StaticConnectionFactory.expectCallTo(ADDRESS1)
			.andWhenPayload(is(DATA4)).thenReturn(DATA1);
		checkConnection(ADDRESS1,DATA1, DATA4);
		Connection connection = connectionFactory.createConnection(ADDRESS1);
		connection.getOutputStream().write(DATA3);
		try
		{
			connection.getInputStream().read();
			fail("Exception expected");
		}
		catch(AssertionError e)
		{
			assertEquals("No matcher matches request [3, 3, 3, 3] for address \"localhost:1111\". Do not know which response to return.", e.getMessage());
		}
	}
	@Test
	public void testUnexpectedMultiple() throws IOException
	{
		StaticConnectionFactory.expectCallTo(ADDRESS1).andWhenPayload(is(DATA4)).thenReturn(DATA1);
		checkConnection(ADDRESS1,DATA1, DATA4);
		Connection connection = connectionFactory.createConnection(ADDRESS1);
		connection.getOutputStream().write(DATA4);
		try
		{
			connection.getInputStream().read();
			fail("Exception expected");
		}
		catch(AssertionError e)
		{
			assertTrue(e.getMessage().startsWith("No more connections expected for \"localhost:1111\" and request matching matcher: "));
		}
	}
	
	private void checkConnection(String address, byte[] outData) throws IOException {
		checkConnection(address, outData, null);
	}
	private void checkConnection(String address, byte[] outData, byte[] inData) throws IOException {
		Connection connection = connectionFactory.createConnection(address);
		assertNotNull(connection);
		if (inData!=null)
		{
			connection.getOutputStream().write(inData);
		}
		byte[] actualOutData = FileCopyUtils.copyToByteArray(connection.getInputStream());
		assertThat(actualOutData, is(outData));	
	}
}
