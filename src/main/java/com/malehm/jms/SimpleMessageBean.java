package com.malehm.jms;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(activationConfig = {
//		See jboss-ejb3.xml for the resource-adapter definition, because EAP 7 ignores the 
//		'connectionFactoryLookup' property and uses the default resource adapter if no resource adapter is specified.
		@ActivationConfigProperty(propertyName = "connectionFactoryLookup", propertyValue = "java:/jms/remoteCF"),
		@ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/queue/myQueue")
})
public class SimpleMessageBean implements MessageListener {
	
	@Override
	public void onMessage(Message message) {
		try {
			if (message instanceof TextMessage) {
				System.out.println("Message: " + message.getBody(String.class));
			} else {
				System.out.println("unknown");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}