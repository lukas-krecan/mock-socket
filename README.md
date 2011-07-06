Mock Socket
===========

Mock Socket project is here to help you with testing of your Java Socket related code. It has two parts

* Core - code for mocking of general Java sockets
* HTTP - support for text based HTTP socket testing

Core API
--------
The API was inspired by [Mockito](http://mockito.org/) framework. You have to start with static import:

	import static net.javacrumbs.mocksocket.MockSocket.*; 
	
Mock response
-------------

Then you can write the test. 

	@Test
	public void testResponse() throws Exception
	{
		byte[] mockData = new byte[]{1,2,3,4};
		DefaultSocketData mockResponse = new DefaultSocketData(mockData);
		expectCall().andReturn(mockResponse);
		
		Socket socket = SocketFactory.getDefault().createSocket("example.org", 1234);
		byte[] data = IOUtils.toByteArray(socket.getInputStream());
		socket.close();
		assertThat(data, is(mockData));
	}
	

This test is not much useful since it tests that Java sockets work, but I hope you have the idea. 

Clean-up
--------
*It's necessary to call MockSocket.reset() after each test.*

	@After
	public void tearDown()
	{
		reset();
	}
	
Compare request data
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
	


	

	
	

	

	
