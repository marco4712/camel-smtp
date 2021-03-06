/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/
package org.apache.camel.component.james.smtp;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;

// TODO: Auto-generated Javadoc
/**
 * Endpoint which create {@link SMTPConsumer} instances.
 */
public class SMTPEndpoint extends DefaultEndpoint {

	/** The config. */
	private final SMTPURIConfiguration config;

	/**
	 * Instantiates a new sMTP endpoint.
	 * 
	 * @param endPointUri
	 *            the end point uri
	 * @param component
	 *            the component
	 * @param config
	 *            the config
	 */
	public SMTPEndpoint(String endPointUri, Component component,
			SMTPURIConfiguration config) {
		super(endPointUri, component);
		this.config = config;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.camel.Endpoint#createConsumer(org.apache.camel.Processor)
	 */
	public Consumer createConsumer(Processor processor) throws Exception {
		return new SMTPConsumer(this, processor, config);
	}

	/**
	 * Producing is not supported.
	 * 
	 * @return the producer
	 * @throws Exception
	 *             the exception
	 */
	public Producer createProducer() throws Exception {
		return null;
	}

	/**
	 * Not a singleton.
	 * 
	 * @return true, if is singleton
	 */
	public boolean isSingleton() {
		return false;
	}

}
