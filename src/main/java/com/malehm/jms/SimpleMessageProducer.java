package com.malehm.jms;

import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;

@Singleton
@Startup
public class SimpleMessageProducer {

	@Inject
	@JMSConnectionFactory("java:/jms/remoteCF")
	JMSContext context;

	@Resource(lookup = "java:/jms/queue/myQueue")
	Queue queue;

	@Schedule(hour = "*", minute = "*", second = "*/10")
	public void timeout() throws Exception {
		System.out.println("start timeout");
		JMSProducer producer = context.createProducer();
		Message message = context.createTextMessage("Hallo World!");
		producer.send(queue, message);
		System.out.println("stop timeout");
	}
}