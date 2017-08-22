package cn.bos.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.print.attribute.standard.Media;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
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

import com.opensymphony.xwork2.ActionContext;

import cn.bos.base.action.BaseAction;
import cn.bos.model.Area;
import cn.bos.model.FixedArea;
import cn.bos.model.Standard;
import cn.bos.service.FixedAreaService;
import cn.itcast.crm.domain.Customer;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class FixAreaAction extends BaseAction<FixedArea> {

	@Autowired
	private FixedAreaService fixAreaService;

	@Action(value = "fixed_areaPageQuery", results = { @Result(name = "success", type = "json") })
	public String fixed_areaPageQuery() {
		// 封装Pageable对象
		// 构造Pageable
				Pageable pageable = new PageRequest(page - 1, rows);
				// 构造条件查询对象
				Specification<FixedArea> specification = new Specification<FixedArea>() {
					@Override
					public Predicate toPredicate(Root<FixedArea> root,
							CriteriaQuery<?> query, CriteriaBuilder cb) {
						List<Predicate> list = new ArrayList<Predicate>();
						// 构造查询条件
						if (StringUtils.isNotBlank(model.getId())) {
							// 根据 定区编号查询 等值
							Predicate p1 = cb.equal(root.get("id").as(String.class),
									model.getId());
							list.add(p1);
						}
						if (StringUtils.isNotBlank(model.getCompany())) {
							// 根据公司查询 模糊
							Predicate p2 = cb.like(
									root.get("company").as(String.class),
									"%" + model.getCompany() + "%");
							list.add(p2);
						}

						return cb.and(list.toArray(new Predicate[0]));
					}
				};
				// 调用业务层，查询数据
				Page<FixedArea> pageData = fixAreaService.findPageData(specification,
						pageable);

				// 压入值栈
				pushPageDataToValueStack(pageData);
				return SUCCESS;
		// 调用业务层 ，查询数据结果
			/*	Pageable pageable = new PageRequest(page - 1, rows);		
				Page<FixedArea> pageData = fixAreaService.findPageData(pageable);
				// 返回客户端数据 需要 total 和 rows
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("total", pageData.getNumberOfElements());
				result.put("rows", pageData.getContent());

				// 将map转换为json数据返回 ，使用struts2-json-plugin 插件
				ActionContext.getContext().getValueStack().push(result);
				return SUCCESS;*/
	}

	// 增加区域
	@Action(value = "saveFixedArea", results = { @Result(name = "success", type = "redirect", location = "./pages/base/fixed_area.html") })
	public String saveFixedArea() {
		//model.setId(UUID.randomUUID().toString());
		fixAreaService.save(model);
		return SUCCESS;
	}

	// 批量删除
	private String ids;

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	@Action(value = "fixed_batch_del", results = { @Result(name = "success", type = "redirect", location = "./pages/base/fixed_area.html") })
	public String batch_del() {
		// 接受id的拼接数组
		String[] idsarray = ids.split(",");
		fixAreaService.delBatch(idsarray);
		return SUCCESS;
	}
	//查询所有未关联定区的列表
	@Action(value = "fixedArea_findNoAssociationCustomers", results = { @Result(name = "success", type = "json") })
	
	public String findNoAssociationCustomers(){
		// 使用webClient调用 webService接口
				Collection<? extends Customer> collection = WebClient
						.create("http://localhost:9002/crm_management/services/customerService/noassociationcustomers")
						.accept(MediaType.APPLICATION_JSON)
						.getCollection(Customer.class);
				ActionContext.getContext().getValueStack().push(collection);
				return SUCCESS;
	}
	
	//查询所有已关联定去的列表
	@Action(value = "fixedArea_findHasAssociationFixedAreaCustomers", results = { @Result(name = "success", type = "json") })
	public String findHasAssociationCustomers(){
		// 使用webClient调用 webService接口
				Collection<? extends Customer> collection = WebClient
						.create("http://localhost:9002/crm_management/services/customerService/associationfixedareacustomers"
								+model.getId())
						.accept(MediaType.APPLICATION_JSON)
						.getCollection(Customer.class);
				ActionContext.getContext().getValueStack().push(collection);
				return SUCCESS;
	}
	
	//关联客户到定区
	private String[] customerIds;
	public void setCustomerIds(String[] customerIds) {
		this.customerIds = customerIds;
	}
	@Action(value = "fixedArea_associationCustomersToFixedArea", results = { @Result(name = "success", type = "redirect", location = "./pages/base/fixed_area.html") })
	public String associationCustomersToFixedArea(){
		String customerIdStr = StringUtils.join(customerIds, ",");
		// 使用webClient调用 webService接口
		WebClient.create("http://localhost:9002/crm_management/services/customerService/associationcustomerstofixedarea").query("customerIdStr", customerIdStr)
																			.query("fixedAreaId", model.getId()).put(null);
		return SUCCESS;	
	}
	// 属性驱动
		private Integer courierId;
		private Integer takeTimeId;

		public void setCourierId(Integer courierId) {
			this.courierId = courierId;
		}

		public void setTakeTimeId(Integer takeTimeId) {
			this.takeTimeId = takeTimeId;
		}
		// 关联快递员 到定区
		@Action(value = "fixedArea_associationCourierToFixedArea", 
				results = { @Result(name = "success", type = "redirect", 
				location = "./pages/base/fixed_area.html") })
		public String associationCourierToFixedArea() {
			// 调用业务层， 定区关联快递员
			fixAreaService.associationCourierToFixedArea(model, courierId,
					takeTimeId);
			return SUCCESS;
		}
}
