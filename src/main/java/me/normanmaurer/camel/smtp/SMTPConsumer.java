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
package me.normanmaurer.camel.smtp;

import java.net.InetSocketAddress;

import me.normanmaurer.camel.smtp.authentication.AuthHookImpl;
import me.normanmaurer.camel.smtp.relay.AllowToRelayHandler;

import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.james.protocols.api.logger.ProtocolLoggerAdapter;
import org.apache.james.protocols.netty.NettyServer;
import org.apache.james.protocols.smtp.SMTPProtocol;
import org.apache.james.protocols.smtp.SMTPProtocolHandlerChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Auto-generated Javadoc
/**
 * Consumer which starts an SMTPServer and forward mails to the processor once
 * they are received.
 */
public class SMTPConsumer extends DefaultConsumer {

	/** The config. */
	final SMTPURIConfiguration config;
	
	/** The server. */
	private NettyServer server;
	
	/** The chain. */
	private SMTPProtocolHandlerChain chain;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory
			.getLogger(SMTPConsumer.class);

	/**
	 * Instantiates a new sMTP consumer.
	 *
	 * @param endpoint the endpoint
	 * @param processor the processor
	 * @param config the config
	 */
	public SMTPConsumer(Endpoint endpoint, Processor processor,
			SMTPURIConfiguration config) {
		super(endpoint, processor);
		this.config = config;

	}

	/**
	 * Startup the SMTP Server.
	 *
	 * @throws Exception the exception
	 */
	@Override
	protected void doStart() throws Exception {
		super.doStart();
		chain = new SMTPProtocolHandlerChain(true);
		chain.add(new AllowToRelayHandler(config.getLocalDomains()));
		if (config.getConsumerHook() != null) {
			chain.add(config.getConsumerHook());
		} else {
			chain.add(new DefaultConsumerHook(this));
		}
		/*
		 * if SMTP authentication has been activated - i.e., an authenticator
		 * bean was specified in the config, instantiate the AuthHookImpl with
		 * it
		 */
		if (config.getAuthenticator() != null) {
			chain.add(new AuthHookImpl(config.getAuthenticator()));
		}
		chain.wireExtensibleHandlers();
		server = new NettyServer(new SMTPProtocol(chain, config,
				new ProtocolLoggerAdapter(LOG)));
		server.setListenAddresses(new InetSocketAddress(config.getBindIP(),
				config.getBindPort()));
		server.bind();
	}

	/**
	 * Shutdown the SMTPServer.
	 *
	 * @throws Exception the exception
	 */
	@Override
	protected void doStop() throws Exception {
		super.doStop();
		server.unbind();
	}

}
