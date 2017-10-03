package com.malehm.jms;

import java.util.Arrays;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;

import org.apache.activemq.artemis.core.config.Configuration;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.jms.server.config.ConnectionFactoryConfiguration;
import org.apache.activemq.artemis.jms.server.config.JMSConfiguration;
import org.apache.activemq.artemis.jms.server.config.JMSQueueConfiguration;
import org.apache.activemq.artemis.jms.server.config.impl.ConnectionFactoryConfigurationImpl;
import org.apache.activemq.artemis.jms.server.config.impl.JMSConfigurationImpl;
import org.apache.activemq.artemis.jms.server.config.impl.JMSQueueConfigurationImpl;
import org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS;
import org.junit.rules.ExternalResource;

public class JmsResource extends ExternalResource {

	private JMSContext jmsContext;
	private EmbeddedJMS jmsServer;

	@Override
	protected void before() throws Throwable {
		// Step 1. Create ActiveMQ Artemis core configuration, and set the properties accordingly
		Configuration configuration = new ConfigurationImpl().setPersistenceEnabled(false)
				.setJournalDirectory("target/data/journal").setSecurityEnabled(false)
				.addAcceptorConfiguration("tcp", "tcp://localhost:61616")
				.addConnectorConfiguration("connector", "tcp://localhost:61616");

		// Step 2. Create the JMS configuration
		JMSConfiguration jmsConfig = new JMSConfigurationImpl();

		// Step 3. Configure the JMS ConnectionFactory
		ConnectionFactoryConfiguration cfConfig = new ConnectionFactoryConfigurationImpl().setName("cf")
				.setConnectorNames(Arrays.asList("connector")).setBindings("cf");
		jmsConfig.getConnectionFactoryConfigurations().add(cfConfig);

		// Step 4. Configure the JMS Queue
		JMSQueueConfiguration queueConfig = new JMSQueueConfigurationImpl().setName("queue1").setDurable(false)
				.setBindings("queue/queue1");
		jmsConfig.getQueueConfigurations().add(queueConfig);

		// Step 5. Start the JMS Server using the ActiveMQ Artemis core server
		// and the JMS configuration
		jmsServer = new EmbeddedJMS();
		jmsServer.setConfiguration(configuration);
		jmsServer.setJmsConfiguration(jmsConfig);
		jmsServer.start();
		System.out.println("Started Embedded JMS Server");

		// Step 6. Lookup JMS resources defined in the configuration
		ConnectionFactory cf = (ConnectionFactory) jmsServer.lookup("cf");
		jmsContext = cf.createContext();
	}

	@Override
	protected void after() {
		try {
			jmsServer.stop();
			System.out.println("Stopped Embedded JMS Server");
		} catch (final Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Could not shut down server.", ex);
		}
	}

	public JMSContext getJmsContext() {
		return jmsContext;
	}
}