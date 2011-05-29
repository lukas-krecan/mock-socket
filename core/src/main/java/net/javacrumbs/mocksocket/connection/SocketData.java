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

package net.javacrumbs.mocksocket.connection;

import java.util.Arrays;

/**
 * Wraps socket data.
 * @author Lukas Krecan
 *
 */
public class SocketData {

	protected final byte[] data;

	public SocketData(byte[] data) {
		this.data = data;
	}

	public byte[] getBytes() {
		return data;
	}

	@Override
	public String toString() {
		return Arrays.toString(data);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(data);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SocketData other = (SocketData) obj;
		if (!Arrays.equals(data, other.data))
			return false;
		return true;
	}

}