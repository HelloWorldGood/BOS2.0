package cn.bos.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.bos.base.action.BaseAction;
import cn.bos.model.TakeTime;
import cn.bos.service.TakeTimeService;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class TakeTimeAction extends BaseAction<TakeTime> {

	@Autowired
	private TakeTimeService takeTimeService;
	
	@Action(value = "taketime_findAll", results = { @Result(name = "success", type = "json") })
	public String findAll(){
		//调用业务层查询所有收派时间
		List<TakeTime> takeTimes = takeTimeService.findAll();
		ActionContext.getContext().getValueStack().push(takeTimes);
		return SUCCESS;
	}
}