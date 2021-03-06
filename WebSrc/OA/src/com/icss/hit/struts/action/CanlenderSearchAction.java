/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.icss.hit.struts.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.icss.hit.bean.SchduletimeBean;
import com.icss.hit.bean.interfaces.ScheduletimeDao;
import com.icss.hit.component.PageBean;
import com.icss.hit.hibernate.vo.Schedule;

/** 
 * 用于在日历部分的时候，用于日历的每日操作和每月操作
 * Creation date: 08-04-2009
 * @author 赵颖申
 * XDoclet definition:
 * @struts.action validate="true"
 * @struts.action-forward name="canlenderSearch.successd" path="/work/configWork.jsp"
 * @struts.action-forward name="canlenderSearch.fail" path="/work/canlenderSearch.jsp"
 */
public class CanlenderSearchAction extends Action {
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
	@SuppressWarnings("deprecation")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		//在地址栏里面获得日期
		String date = request.getParameter("date");
		//如果没有或者日期格式错误的话，转向失败
		if(date == null||date.length()<8||date.length()>10)return mapping.findForward("canlenderSearch.fail");
		//读取从date中截取的字符串
		String year = null;
		String month = null;
		String day = null;
		Date beginTime = new Date();
		Date endTime = new Date();
		//将读出来的数据进行转化成int型的来用的变量
		int Year = 2009;
		int Month = 0;
		int Day = 0;
		Date searchDate = new Date();
		
		//date长度为8，格式为2009n2y8
			if(date.length() == 8)
			{
				year = date.substring(0, 4);
				month = date.substring(5, 6);
				day = date.substring(7, 8);
				Year = Integer.parseInt(year);
				Month = Integer.parseInt(month);
				Day = Integer.parseInt(day);
				
			}
			//date长度为9，格式为2009n12y8
			else if(date.length() == 9&&date.charAt(6) != 'y')
			{
				year = date.substring(0, 4);
				month = date.substring(5, 7);
				day = date.substring(8, 9);
				Year = Integer.parseInt(year);
				Month = Integer.parseInt(month);
				Day = Integer.parseInt(day);
				
			}
			//date长度为9，格式为2009n2y18
			else if(date.length() == 9&&date.charAt(6) == 'y')
			{
				year = date.substring(0, 4);
				month = date.substring(5, 6);
				day = date.substring(7, 9);
				Year = Integer.parseInt(year);
				Month = Integer.parseInt(month);
				Day = Integer.parseInt(day);
				
			}
			//date长度为10，格式为2009n12y18
			else if(date.length() == 10)
			{
				year = date.substring(0, 4);
				month = date.substring(5, 7);
				day = date.substring(8, 10);
				Year = Integer.parseInt(year);
				Month = Integer.parseInt(month);
				Day = Integer.parseInt(day);
				
			}
			//构建date类型的时间以便查询
			GregorianCalendar calendar = new GregorianCalendar(Year,Month-1,Day,0,0,0);
			beginTime = calendar.getTime();                 //一天开始时间
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			endTime = calendar.getTime();					//一天结束时间
			//获得用户的ID
			long userId = 1L;
			// 得到用户ID
			if( request.getSession().getAttribute("UserId") != null ){
				userId = Long.parseLong(request.getSession().getAttribute("UserId").toString());
			}else{
				return mapping.findForward("NullLogin");
			}
			
			ScheduletimeDao schduleWork = new SchduletimeBean();
			//获得查询的数目
			int count = schduleWork.getSchduleWorkCount(userId, beginTime, endTime);
			//进行分页
			int pageCount = 0;
			
			if( count != 0 ){
					// 得到总页数
					pageCount = schduleWork.getPageCount(count, SchduletimeBean.PAGE_SIZE);
				
				
				// 当前页数
				int pageNo = 1;
				if( request.getParameter("page") != null ){
					try{
						pageNo = Integer.parseInt(request.getParameter("page"));
					}catch( Exception e){
						pageNo = 1;
					}
				}
				
				// 得到当前页数的内容
				if( pageCount != 0){
					List<Schedule> list = schduleWork.getSchduleWorkByPage(userId, pageNo, beginTime, endTime);
					//将查询出来的数据添加到request里面去，在前台显示
					request.setAttribute("schList", list);
					
					// 输出分页的信息
					PageBean bean = new PageBean();
					bean.setTotal(pageCount);
					bean.setLink("/OA/canlenderSearch.do");
					bean.addParam("date="+date);
					bean.setThispage(pageNo);
					
					request.setAttribute("page", bean.getPageString());
				}
				request.setAttribute("ExistArrange", 1);
				//构造一个List，以便在前台显示日期，以便知道是什么时候的查询内容。
				List<Date> dateList = new ArrayList<Date>();
				dateList.add(beginTime);
				//加载到request里面去
				request.setAttribute("DateOfSearch", dateList);
				return mapping.findForward("canlenderSearch.successd");
			}
			else{
				//如果没有的时候，设置"ExistArrange"的东西，以便在前台判断且弹出提示.
				request.setAttribute("ExistArrange", 0);
				return mapping.findForward("canlenderSearch.fail");
			}
	}
	
}