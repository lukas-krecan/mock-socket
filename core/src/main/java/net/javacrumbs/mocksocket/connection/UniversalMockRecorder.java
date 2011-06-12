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

import net.javacrumbs.mocksocket.connection.matcher.MatcherBasedMockResultRecorder;
import net.javacrumbs.mocksocket.connection.sequential.SequentialMockRecorder;

import org.hamcrest.Matcher;

/**
 * Can record both sequential and matcher based mocks.
 * @author Lukas Krecan
 *
 */
public interface UniversalMockRecorder {
	public SequentialMockRecorder andReturn(SocketData data);
	
	public MatcherBasedMockResultRecorder andWhenPayload(Matcher<SocketData> matcher);
}
