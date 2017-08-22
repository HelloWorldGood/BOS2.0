package cn.bos.base.action;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;


public class BaseAction<T> extends ActionSupport implements ModelDriven<T> {

	private static final long serialVersionUID = 8797881082206295805L;
	protected T model;

	@Override
	public T getModel() {
		return model;
	}

	public void setModel(T model) {
		this.model = model;
	}

	// 构造完成Mode的实例化
	public BaseAction() {
		//
		Type genericSuperclass = this.getClass().getGenericSuperclass();
		ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
		Class<T> modelClass = (Class<T>) parameterizedType
				.getActualTypeArguments()[0];
		try {
			model = modelClass.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("模型构造失败....");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	// 接收分页查询参数
	protected int page;
	protected int rows;

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	// 将分页查询结果数据，压入值栈的方法
	protected void pushPageDataToValueStack(Page<T> pageData) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", pageData.getTotalElements());
		result.put("rows", pageData.getContent());

		ActionContext.getContext().getValueStack().push(result);
	}
}
