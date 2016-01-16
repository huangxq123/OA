/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.icss.hit.struts.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.icss.hit.bean.MeetingAttendBean;
import com.icss.hit.bean.interfaces.MeetingAttendDao;
import com.icss.hit.component.PageBean;
import com.icss.hit.hibernate.vo.Meeting;

/** 
 * �õ��û���������б�
 * Creation date: 08-09-2009
 * @author ����
 * XDoclet definition:
 * @struts.action validate="true"
 * @struts.action-forward name="meetingAttend.succeed" path="/meeting/meetingAttend.jsp"
 * @struts.action-forward name="meetingAttend.failed" path="/meeting/meetingAttend.jsp"
 */
public class MeetingAttendAction extends Action {
	/*
	 * Generated Methods
	 */

	/** 
	 * Method execute
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// ��ǰ��½�û�ID
		long userId = -1;
		if( request.getSession().getAttribute("UserId") != null ){
			userId = Long.parseLong(request.getSession().getAttribute("UserId").toString());
		}else{
			return mapping.findForward("NullLogin");
		}
		// ��ѯҳ��
		int pageNo = 1;
		
		// ������Ա������
		int count = 0;
		// ��ҳ������
		int pageCount = 0;
		// �õ�����������
		List<Meeting> list = null;
		try{
			pageNo = Integer.parseInt(request.getParameter("page"));
		}
		catch(Exception e){
			pageNo =1;
		}
		
		if( userId != -1){
			MeetingAttendDao meeting = new MeetingAttendBean();
			
			// �õ������ͷ�ҳ
			count = meeting.getAllAssociateMeetingCount(userId);
			pageCount = meeting.getPageCount(count, MeetingAttendBean.PAGE_SIZE);
			
			// ����ҳ��ķ�Χ
			pageNo = pageNo < 1? 1:pageNo;
			pageNo = pageNo > pageCount? pageCount: pageNo;
			
			list = meeting.getAllAssociateMeeting(pageNo, userId);
			request.setAttribute("AssociateMeetingList", list);
						
			// ����ǰ���ҳ����ʾ
			PageBean pagebean = new PageBean();
			pagebean.setLink("meetingAttend.do");
			pagebean.setTotal(pageCount);
			pagebean.setThispage(pageNo);
			request.setAttribute("pageString", pagebean.getPageString());
			request.setAttribute("pageNo",pageNo);
		}
		return mapping.findForward("meetingAttend.succeed");
	}
}