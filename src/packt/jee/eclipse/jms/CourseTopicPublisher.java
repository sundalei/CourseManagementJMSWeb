package packt.jee.eclipse.jms;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.InitialContext;

import packt.jee.eclipse.jms.dto.CourseDTO;

public class CourseTopicPublisher {
	private TopicConnection connection;
	private TopicSession session;
	private Topic topic;
	
	public CourseTopicPublisher() throws Exception {
		InitialContext initialContext = new InitialContext();
		TopicConnectionFactory connectionFactory = (TopicConnectionFactory) initialContext.lookup("jms/CourseManagementCF");
		connection = connectionFactory.createTopicConnection();
		connection.start();
		session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		topic = (Topic) initialContext.lookup("jms/courseManagementTopic");
	}
	
	public void close() {
		if(connection != null) {
			try {
				connection.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void publishAddCourseMessage(CourseDTO course) throws Exception {
		TopicPublisher sender = session.createPublisher(topic);
		ObjectMessage objectMessage = session.createObjectMessage(course);
		sender.send(objectMessage);
	}
}
