package packt.jee.eclipse.jms;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.InitialContext;

import packt.jee.eclipse.jms.dto.CourseDTO;

public class CourseQueueSender {
	private QueueConnection connection;
	private QueueSession session;
	private Queue queue;
	
	public CourseQueueSender() throws Exception {
		InitialContext initialContext = new InitialContext();
		QueueConnectionFactory connectionFactory = (QueueConnectionFactory) initialContext.lookup("jms/CourseManagementCF");
		connection = connectionFactory.createQueueConnection();
		connection.start();
		session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		queue = (Queue) initialContext.lookup("jms/courseManagementQueue");
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

	@Override
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}
	
	public void sendAddCourseMessage(CourseDTO course) throws Exception {
		QueueSender sender = session.createSender(queue);
		ObjectMessage objMessage = session.createObjectMessage(course);
		sender.send(objMessage);
	}
}
