package packt.jee.eclipse.jms.jsp.beans;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import packt.jee.eclipse.jms.CourseQueueSender;
import packt.jee.eclipse.jms.dto.CourseDTO;

public class CourseJSPBean {
	
	private CourseDTO course = new CourseDTO();
	
	public void setId(int id) {
		course.setId(id);
	}
	
	public String getName() {
		return course.getName();
	}
	
	public void setName(String name) {
		course.setName(name);
	}
	
	public int getCredits() {
		return course.getCredits();
	}
	
	public void setCredits() {
		course.setCredits(course.getCredits());
	}
	
	public void addCourse(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession(true);
		
		CourseQueueSender courseQueueSender = (CourseQueueSender) session.getAttribute("CourseQueueSender");
		if(courseQueueSender == null) {
			courseQueueSender = new CourseQueueSender();
			session.setAttribute("CourseQueueSender", courseQueueSender);
		}
		
		if(courseQueueSender != null) {
			courseQueueSender.sendAddCourseMessage(course);
		}
	}
}
