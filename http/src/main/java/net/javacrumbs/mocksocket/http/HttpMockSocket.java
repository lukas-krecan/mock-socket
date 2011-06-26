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
package net.javacrumbs.mocksocket.http;

import net.javacrumbs.mocksocket.MockSocket;
import net.javacrumbs.mocksocket.connection.data.RequestSocketData;
import net.javacrumbs.mocksocket.http.connection.HttpStaticConnectionFactory;
import net.javacrumbs.mocksocket.http.connection.UniversalHttpMockRecorder;
import net.javacrumbs.mocksocket.http.matchers.ContentMatcher;
import net.javacrumbs.mocksocket.http.matchers.HeaderMatcher;
import net.javacrumbs.mocksocket.http.matchers.MethodMatcher;
import net.javacrumbs.mocksocket.http.matchers.StatusMatcher;

import org.hamcrest.Matcher;
import org.hamcrest.core.CombinableMatcher;

public class HttpMockSocket extends MockSocket{
	
	protected HttpMockSocket()
	{
	}
	
	public static HttpResponseGenerator response()
	{
		return new HttpResponseGenerator();
	}
	
	public static HttpRequest requestTo(String address, int index)
	{
		return new HttpParser(getConnectionTo(address).requestData().get(index));
	}
	
	public static UniversalHttpMockRecorder expectCallTo(String address) {
		return HttpStaticConnectionFactory.expectCallTo(address);
	}
	
	public static CombinableMatcher<RequestSocketData> status(Matcher<Integer> statusMatcher) {
		return new CombinableMatcher<RequestSocketData>(new StatusMatcher(statusMatcher));
	}

	public static CombinableMatcher<RequestSocketData> header(String header, Matcher<String> headerMatcher) {
		return new CombinableMatcher<RequestSocketData>(new HeaderMatcher(header, headerMatcher));
	}
	
	public static CombinableMatcher<RequestSocketData> content(Matcher<String> contentMatcher) {
		return new CombinableMatcher<RequestSocketData>(new ContentMatcher(contentMatcher));
	}

	public static CombinableMatcher<RequestSocketData> method(Matcher<String> methodMatcher) {
		return new CombinableMatcher<RequestSocketData>(new MethodMatcher(methodMatcher));
	}


}
