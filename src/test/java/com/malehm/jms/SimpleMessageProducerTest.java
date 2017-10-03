package com.malehm.jms;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.Queue;

import org.junit.Rule;
import org.junit.Test;

public class SimpleMessageProducerTest {

	@Rule
	public JmsResource jmsResource = new JmsResource();

	@Test
	public void test() throws Exception {
		JMSContext jmsContext = jmsResource.getJmsContext();
		Queue queue = jmsContext.createQueue("TESTQUEUE");
		SimpleMessageProducer producer = new SimpleMessageProducer();
		producer.context = jmsContext;
		producer.queue = queue;
		producer.timeout();

		JMSConsumer consumer = jmsContext.createConsumer(queue);
		Message message = consumer.receive(1000);
		assertThat(message.getBody(String.class), equalTo("Hallo World!"));
	}
}