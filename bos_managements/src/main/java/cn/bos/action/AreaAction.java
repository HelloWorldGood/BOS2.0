package cn.bos.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
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

import cn.bos.base.action.BaseAction;
import cn.bos.model.Area;
import cn.bos.model.Courier;
import cn.bos.model.Standard;
import cn.bos.service.AreaService;
import cn.bos.utils.PinYin4jUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class AreaAction extends BaseAction<Area> {
	// 注入业务层对象
	@Autowired
	private AreaService areaService;
	// 接收上传文件
	private File file;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	// 批量信息导入
	@Action(value = "area_batchImport")
	public String area_Import() throws IOException, IOException {
		List<Area> areas = new ArrayList<Area>();
		// 编写解析代码逻辑
		// 基于.xls 格式解析 HSSF
		// 1、 加载Excel文件对象
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
		// 2、 读取一个sheet
		HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
		// 3、 读取sheet中每一行
		for (Row row : sheet) {
			// 一行数据 对应 一个区域对象
			if (row.getRowNum() == 0) {
				// 第一行 跳过
				continue;
			}
			// 跳过空行
			if (row.getCell(0) == null
					|| StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
				continue;
			}
			Area area = new Area();
			area.setId(row.getCell(0).getStringCellValue());
			area.setProvince(row.getCell(1).getStringCellValue());
			area.setCity(row.getCell(2).getStringCellValue());
			area.setDistrict(row.getCell(3).getStringCellValue());
			area.setPostcode(row.getCell(4).getStringCellValue());
			// 基于pinyin4j生成城市编码和简码
			String province = area.getProvince();
			String city = area.getCity();
			String district = area.getDistrict();
			province = province.substring(0, province.length() - 1);
			city = city.substring(0, city.length() - 1);
			district = district.substring(0, district.length() - 1);
			// 简码
			String[] headArray = PinYin4jUtils.getHeadByString(province + city
					+ district);
			StringBuffer buffer = new StringBuffer();
			for (String headStr : headArray) {
				buffer.append(headStr);
			}
			String shortcode = buffer.toString();
			area.setShortcode(shortcode);
			// 城市编码
			String citycode = PinYin4jUtils.hanziToPinyin(city, "");
			area.setCitycode(citycode);

			areas.add(area);
		}
		// 调用业务层
		areaService.saveBatch(areas);

		return NONE;
	}

	
	@Action(value = "area_pageQuery", results = { @Result(name = "success", type = "json") })
	public String area_pageQuery() {
		// 封装Pageable对象
		Pageable pageable = new PageRequest(page - 1, rows);
		// 封装条件查询对象 Specification
		Specification<Area> specification = new Specification<Area>() {
			// Root 用于获取属性字段，
			// CriteriaQuery可以用于简单条件查询，
			// CriteriaBuilder 用于构造复杂条件查询
			@Override
			public Predicate toPredicate(Root<Area> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				// 创建保存条件的集合对象
				List<Predicate> list = new ArrayList<>();
				// 添加条件
				if (StringUtils.isNotBlank(model.getProvince())) {
					// 根据省份模糊查询
					Predicate p1 = cb.like(root.get("province")
							.as(String.class), "%" + model.getProvince() + "%");
					list.add(p1);
				}
				if (StringUtils.isNotBlank(model.getCity())) {

					Predicate p2 = cb.like(root.get("city").as(String.class),
							"%" + model.getCity() + "%");
					list.add(p2);
				}
				if (StringUtils.isNotBlank(model.getDistrict())) {
					Predicate p3 = cb.like(root.get("district")
							.as(String.class), "%" + model.getDistrict() + "%");
					list.add(p3);
				}

				return cb.and(list.toArray(new Predicate[0]));
			}
		};

		// 调用业务层 ，返回 Page
		Page<Area> pageData = areaService.findPageData(specification, pageable);
		// 压入值栈
		pushPageDataToValueStack(pageData);
		
		return SUCCESS;
	}
	
	
	//保存快区域基本信息
	@Action(value = "area_save", results = { @Result(name = "success", type = "redirect",location="./pages/base/area.html") })
	public String area_save(){
		model.setId(UUID.randomUUID().toString());
		areaService.save(model);
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
	@Action(value = "fixed_batch_del", results = { @Result(name = "success", type = "redirect",location="./pages/base/area.html") })
	public String batch_del() {
		// 接受id的拼接数组
		String[] idsarray = ids.split(",");
		areaService.delBatch(idsarray);
		return SUCCESS;
	}
}
