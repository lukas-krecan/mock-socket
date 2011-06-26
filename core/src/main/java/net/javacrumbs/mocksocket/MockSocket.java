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

import net.javacrumbs.mocksocket.connection.MockConnection;
import net.javacrumbs.mocksocket.connection.StaticConnectionFactory;
import net.javacrumbs.mocksocket.connection.UniversalMockRecorder;
import net.javacrumbs.mocksocket.connection.data.RequestSocketData;
import net.javacrumbs.mocksocket.matchers.AddressMatcher;
import net.javacrumbs.mocksocket.matchers.DataMatcher;

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
	public static UniversalMockRecorder expectCallTo(String address) {
		return StaticConnectionFactory.expectCallTo(address);
	}
	
	/**
	 * To be called after each test.
	 */
	public static void reset()
	{
		StaticConnectionFactory.reset();
	}
	
	/**
	 * Returns {@link MockConnection}.
	 * @return
	 */
	public static MockConnection getConnectionTo(String address) {
		return StaticConnectionFactory.getConnectionTo(address);
	}

	/**
	 * Matcher that can compare data.
	 * @param dataMatcher
	 * @return
	 */
	public static Matcher<RequestSocketData> data(Matcher<byte[]> dataMatcher)
	{
		return new CombinableMatcher<RequestSocketData>(new DataMatcher(dataMatcher));
	}
	
	/**
	 * Matcher thac can compare address.
	 * @param addressMatcher
	 * @return
	 */
	public static Matcher<RequestSocketData> address(Matcher<String> addressMatcher)
	{
		return new CombinableMatcher<RequestSocketData>(new AddressMatcher(addressMatcher));
	}
}
