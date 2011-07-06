Mock Socket
===========

Mock Socket project is here to help you with testing of your Java Socket related code. It has two parts

* Core - code for mocking of general Java sockets
* HTTP - support for text based HTTP socket testing

Core API
========
The API was inspired by [Mockito](http://mockito.org/) framework. You have to start with static import:

	import static net.javacrumbs.mocksocket.MockSocket.*; 
	
Mock response
-------------

Then you can write the test. 

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
	

This test is not much useful since it tests that Java sockets work, but I hope you have the idea. 

Clean-up
--------
**It's necessary to call MockSocket.reset() after each test.**

	@After
	public void tearDown()
	{
		reset();
	}
	
Validate request data
--------------------

It's possible to compare the request data like this.

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
	
Condition based mock response
-----------------------------
Sometimes it is not feasible to return mock responses based on the order of requests. In such situation, it's possible to use condition based mocks.

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
	
or 

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
	
Matchers
--------
As you have seen, Mock socket contains several built-in matchers.

<table>
    <tr>
        <th>Method</th>
        <th>Description</th>
        <th>Example</th>
    </tr>
    <tr>
    	<td>data(Matcher<byte[]> dataMatcher)</td>
    	<td>Compares data in andWhenRequest method</td>
    	<td>expectCall().andWhenRequest(data(is(dataToWrite))).thenReturn(data(mockData))</td>
    </tr>
    <tr>
    	<td>withData(InputStream data)</td>
    	<td>Compares data in andWhenRequest method</td>
    	<td>expectCall().andWhenRequest(withData(inputStream)).thenReturn(DATA1)</td>
    </tr>
    <tr>
    	<td>address(Matcher<String> addressMatcher)</td>
    	<td>Compares address in andWhenRequest method</td>
    	<td>expectCall().andWhenRequest(address(is("example.org:1234"))).thenReturn(data(mockData));</td>
    </tr>
    <tr>
    	<td>data(Matcher<byte[]> dataMatcher)</td>
    	<td>Compares response data</td>
    	<td>assertThat(recordedConnections().get(0), data(is(dataToWrite)));</td>
    </tr>
</table>

	

	

	


	

	
	

	

	
