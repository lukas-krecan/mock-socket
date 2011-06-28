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

package net.javacrumbs.mocksocket.util;

import java.io.IOException;
import java.io.InputStream;

import net.javacrumbs.mocksocket.MockSocketException;

import org.apache.commons.io.IOUtils;

public class Utils {
	private Utils()
	{
		//empty
	}
	
	public static final byte[] toByteArray(InputStream stream)
	{
		try 
		{
			return IOUtils.toByteArray(stream);
		} 
		catch (IOException e)
		{
			throw new MockSocketException("Can not read data.",e);
		}
		finally
		{
			IOUtils.closeQuietly(stream);
		}
	}
}
