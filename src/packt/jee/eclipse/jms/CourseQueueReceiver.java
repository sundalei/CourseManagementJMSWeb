package packt.jee.eclipse.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.InitialContext;

import packt.jee.eclipse.jms.dto.CourseDTO;

public class CourseQueueReceiver {
	private QueueConnection connection;
	private QueueSession session;
	private Queue queue;
	
	private String receiverName;
	
	private CourseTopicPublisher topicPublisher;
	
	public CourseQueueReceiver(String name) throws Exception {
		this.receiverName = name;
		
		InitialContext initialContext = new InitialContext();
		QueueConnectionFactory connectionFactory = (QueueConnectionFactory) initialContext.lookup("jms/CourseManagementCF");
		connection = connectionFactory.createQueueConnection();
		connection.start();
		session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		queue = (Queue) initialContext.lookup("jms/courseManagementQueue");
		
		topicPublisher = new CourseTopicPublisher();
		
		QueueReceiver receiver = session.createReceiver(queue);
		receiver.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				try {
					CourseDTO course = (CourseDTO) ((ObjectMessage) message).getObject();
					System.out.println("Received addCourse message for course name - " + course.getName() + " in Receiver " + receiverName);
					if(topicPublisher != null) {
						topicPublisher.publishAddCourseMessage(course);
					}
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
