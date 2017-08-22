package cn.bos.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import cn.bos.model.Courier;
import cn.bos.model.Standard;
import cn.bos.service.CourierService;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class CourierAction extends ActionSupport implements ModelDriven<Courier>{

	private Courier courier = new Courier();
	@Autowired
	private CourierService courierService;
	@Override
	public Courier getModel() {
		return courier;
	}
	//保存快递员基本信息
	@Action(value = "courier_save", results = { @Result(name = "success", type = "redirect",location="./pages/base/courier.html") })
	public String courier_save(){
		courierService.save(courier);
		return SUCCESS;
	}
	private int page;
	private int rows;
	
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	//分页条件查询列表快递员信息
	@Action(value = "courier_pageQuery", results = { @Result(name="success",type="json") })
	public String pageQuery(){
				// 封装Pageable对象
				Pageable pageable = new PageRequest(page - 1, rows);		
				// 封装条件查询对象 Specification
				Specification<Courier> specification = new Specification<Courier>(){
					// Root 用于获取属性字段，
					//CriteriaQuery可以用于简单条件查询，
					//CriteriaBuilder 用于构造复杂条件查询
					@Override
					public Predicate toPredicate(Root<Courier> root,CriteriaQuery<?> query, CriteriaBuilder cb) {
								List<Predicate> list = new ArrayList<>();
								//简单表查询
								if (StringUtils.isNotBlank(courier.getCourierNum())) {
									Predicate p1 = cb.equal(
											root.get("courierNum").as(String.class),courier.getCourierNum());
									list.add(p1);
								}
								if (StringUtils.isNotBlank(courier.getCompany())) {
									Predicate p2 = cb.like(
											root.get("company").as(String.class),
											"%" + courier.getCompany() + "%");
									list.add(p2);
								}
								if (StringUtils.isNotBlank(courier.getType())) {
									Predicate p3 = cb.equal(root.get("type").as(String.class),
											courier.getType());
									list.add(p3);
								}
								// 多表查询
								Join<Courier, Standard> standardJoin = root.join("standard",
										JoinType.INNER);
								if (courier.getStandard() != null
										&& StringUtils.isNotBlank(courier.getStandard()
												.getName())) {
									Predicate p4 = cb.like(
											standardJoin.get("name").as(String.class), "%"
													+ courier.getStandard().getName() + "%");
									list.add(p4);
								}
								return cb.and(list.toArray(new Predicate[0]));
							}
						};

						// 调用业务层 ，返回 Page
						Page<Courier> pageData = courierService.findPageData(specification,
								pageable);
						// 将返回page对象 转换datagrid需要格式
						Map<String, Object> result = new HashMap<String, Object>();
						result.put("total", pageData.getTotalElements());
						result.put("rows", pageData.getContent());
						// 将结果对象 压入值栈顶部
						ActionContext.getContext().getValueStack().push(result);

						return SUCCESS;
					}
	
	private String ids;
	
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	@Action(value="courier_delBatch",results = { @Result(name="success",type="redirect",location="./pages/base/courier.html")})
	public String delBatch(){
		String [] idsarray = ids.split(",");
		courierService.delBatch(idsarray);
		return SUCCESS;
	}
	//显示快递员
	@Action(value = "courier_findnoassociation",
			results = { @Result(name="success",type="json") })
	public String findnoassociation(){
		//调用业务层查询未关联定区的快递员
		List<Courier> couriers = courierService.findNoAssociation();
		// 将结果对象 压入值栈顶部
		ActionContext.getContext().getValueStack().push(couriers);
		return SUCCESS;
	}
}
