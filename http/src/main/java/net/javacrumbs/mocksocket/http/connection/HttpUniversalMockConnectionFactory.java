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

package net.javacrumbs.mocksocket.http.connection;

import net.javacrumbs.mocksocket.connection.UniversalMockConnectionFactory;
import net.javacrumbs.mocksocket.connection.data.RequestSocketData;
import net.javacrumbs.mocksocket.connection.data.SocketData;
import net.javacrumbs.mocksocket.connection.matcher.MatcherBasedMockConnectionFactory;
import net.javacrumbs.mocksocket.connection.sequential.SequentialMockConnectionFactory;
import net.javacrumbs.mocksocket.http.connection.matcher.HttpMatcherBasedMockConnectionFactory;
import net.javacrumbs.mocksocket.http.connection.matcher.MatcherBasedHttpMockResultRecorder;
import net.javacrumbs.mocksocket.http.connection.sequential.HttpSequentialMockConnectionFactory;
import net.javacrumbs.mocksocket.http.connection.sequential.SequentialHttpMockRecorder;

import org.hamcrest.Matcher;

public class HttpUniversalMockConnectionFactory extends UniversalMockConnectionFactory implements UniversalHttpMockRecorder{
	
	@Override
	protected MatcherBasedMockConnectionFactory createMatcherBasedConnection() {
		return new HttpMatcherBasedMockConnectionFactory();
	}
	
	@Override
	protected SequentialMockConnectionFactory createSequentialConnection() {
		return new HttpSequentialMockConnectionFactory();
	}
	
	public SequentialHttpMockRecorder andReturn(String data){
		return andReturn(new HttpData(data));
	}
	
	@Override
	public SequentialHttpMockRecorder andReturn(SocketData data) {
		return (SequentialHttpMockRecorder)super.andReturn(data);
	}
	
	@Override
	public MatcherBasedHttpMockResultRecorder andWhenRequest(Matcher<RequestSocketData> matcher) {
		return (MatcherBasedHttpMockResultRecorder)super.andWhenRequest(matcher);
	}
}
