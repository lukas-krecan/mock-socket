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

import static org.hamcrest.CoreMatchers.is;

import java.io.InputStream;
import java.util.List;

import net.javacrumbs.mocksocket.connection.ConnectionFactory;
import net.javacrumbs.mocksocket.connection.StaticConnectionFactory;
import net.javacrumbs.mocksocket.connection.StringUtils;
import net.javacrumbs.mocksocket.connection.UniversalMockRecorder;
import net.javacrumbs.mocksocket.connection.data.DefaultSocketData;
import net.javacrumbs.mocksocket.connection.data.RequestSocketData;
import net.javacrumbs.mocksocket.connection.data.SocketData;
import net.javacrumbs.mocksocket.matchers.AddressMatcher;
import net.javacrumbs.mocksocket.matchers.DataMatcher;
import net.javacrumbs.mocksocket.util.Utils;

import org.hamcrest.Matcher;
import org.hamcrest.core.CombinableMatcher;


/**
 * Main class of mock socket to be statically imported tou your test.
 * @author Lukas Krecan
 *
 */
public class MockSocket {
	protected MockSocket()
	{
		
	}
	
	/**
	 * Prepares mock socket to be trained.
	 * @return
	 */
	public static UniversalMockRecorder expectCall() {
		return StaticConnectionFactory.expectCall();
	}
	
	/**
	 * To be called after each test.
	 */
	public static void reset()
	{
		StaticConnectionFactory.reset();
	}
	
	/**
	 * Returns request data.
	 * @return
	 */
	public static List<RequestSocketData> recordedConnections()
	{
		return StaticConnectionFactory.getRequestRecorder().requestData();
	}
	
	
	
	/**
	 * Empty response.
	 * @return
	 */
	public static SocketData emptyResponse()
	{
		return new DefaultSocketData(new byte[0]);
	}
	
	/**
	 * Response with given data.
	 * @return
	 */
	public static SocketData data(byte[] data)
	{
		return new DefaultSocketData(data);
	}

	/**
	 * Matcher that can compare data. To be used in {@link UniversalMockRecorder#andWhenRequest} method.
	 * @param dataMatcher
	 * @return
	 */
	public static Matcher<RequestSocketData> data(Matcher<byte[]> dataMatcher)
	{
		return new CombinableMatcher<RequestSocketData>(new DataMatcher(dataMatcher));
	}

	/**
	 * Matcher that can compare data. To be used in {@link UniversalMockRecorder#andWhenRequest} method.
	 * @param dataMatcher
	 * @return
	 */
	public static Matcher<RequestSocketData> withData(InputStream data)
	{
		return data(is(Utils.toByteArray(data)));
	}
		
	/**
	 * Matcher that can compare address. To be used in {@link UniversalMockRecorder#andWhenRequest} method.
	 * @param addressMatcher
	 * @return
	 */
	public static Matcher<RequestSocketData> address(Matcher<String> addressMatcher)
	{
		return new CombinableMatcher<RequestSocketData>(new AddressMatcher(addressMatcher));
	}
	
	/**
	 * Matcher that can compare data. To be used in request assertions.
	 * @param dataMatcher
	 * @return
	 */
	public static Matcher<SocketData> dataAre(InputStream data)
	{
		return new CombinableMatcher<SocketData>(new DataMatcher(is(Utils.toByteArray(data))));
	}
	/**
	 * Matcher that can compare data. To be used in request assertions.
	 * @param dataMatcher
	 * @return
	 */
	public static Matcher<SocketData> dataAre(SocketData data)
	{
		return dataAre(data.getData());
	}

	
	/**
	 * Sets custom connection factory.
	 * @param connectionFactory
	 */
	public static void useConnectionFactory(ConnectionFactory connectionFactory) {
		StaticConnectionFactory.useConnectionFactory(connectionFactory);
	}
	
	public static boolean isPrintDataAsString() {
		return StringUtils.isPrintDataAsString();
	}

	/**
	 * Should be data printed as string or as byte array.
	 * @param printDataAsString
	 */
	public static void setPrintDataAsString(boolean printDataAsString) {
		StringUtils.setPrintDataAsString(printDataAsString);
	}

	public static String getDefaultEncoding() {
		return StringUtils.getDefaultEncoding();
	}

	/**
	 * Default encoding for byte[] -> String conversion.
	 * @param defaultEncoding
	 */
	public static void setDefaultEncoding(String defaultEncoding) {
		StringUtils.setDefaultEncoding(defaultEncoding);
	}
}
