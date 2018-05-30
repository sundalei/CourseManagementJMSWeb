package packt.jee.eclipse.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;

import packt.jee.eclipse.jms.dto.CourseDTO;

public class CourseTopicSubscriber {
	private TopicConnection connection;
	private TopicSession session;
	private Topic topic;
	
	private String subcriberName;
	
	public CourseTopicSubscriber(String name) throws Exception {
		this.subcriberName = name;
		
		InitialContext initialContext = new InitialContext();
		TopicConnectionFactory connectionFactory = (TopicConnectionFactory) initialContext.lookup("jms/CourseManagementCF");
		connection = connectionFactory.createTopicConnection();
		connection.start();
		session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		topic = (Topic) initialContext.lookup("jms/courseManagementTopic");
		
		TopicSubscriber subscriber = session.createSubscriber(topic);
		subscriber.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				try {
					CourseDTO course = (CourseDTO) ((ObjectMessage) message).getObject();
					System.out.println("Received addCourse notification for course name - " + course.getName() + " in Subscriber " + subcriberName);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void stop() {
		if(connection != null) {
			try {
				connection.close();
			} catch(JMSException e) {
				e.printStackTrace();
			}
		}
	}
}
