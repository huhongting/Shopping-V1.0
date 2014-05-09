package cn.edu.jnu.web.controller;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import cn.edu.jnu.web.entity.Picture;
import cn.edu.jnu.web.service.PictureService;
import cn.edu.jnu.web.util.QueryResult;
import cn.edu.jnu.web.util.UploadUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 首页滚动广告管理控制器
 * @author HHT
 *
 */
@Controller
public class PictureController {
	private ObjectMapper mapper = new ObjectMapper();
	
	private PictureService pictureService;
	
	/**
	 * 显示广告列表
	 * @param start
	 * @param limit
	 * @return
	 */
	@RequestMapping(value="/control/admin/picture.do", params="method=list")
	public @ResponseBody ObjectNode handlerPicList(@RequestParam int start, 
			@RequestParam int limit) {
		QueryResult<Picture> res = pictureService.listPictures(start, limit, null);
		
		return encodeJSON(res);
	}
	
	/**
	 * 设置广告显示或者不显示
	 * @param ids
	 * @param show
	 * @return
	 */
	@RequestMapping(value="/control/admin/picture.do", params="method=updateshow")
	public @ResponseBody ObjectNode handlerPicShow(@RequestParam int[] ids,
			@RequestParam boolean show) {
		for(int id : ids) {
			Picture pic = pictureService.findById(id);
			if(pic != null) {
				pic.setIsshow(show);
				pictureService.updatePicture(pic);
			}
		}
		return mapper.createObjectNode().put("success", true);
	}
	
	/**
	 * 删除广告
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/control/admin/picture.do", params="method=del")
	public @ResponseBody ObjectNode handlerDelete(@RequestParam int[] ids) {
		for(int id : ids) {
			Picture pic = pictureService.findById(id);
			UploadUtil.deletePicture(pic.getPath());
			pictureService.delerePicture(id);
		}
		return mapper.createObjectNode().put("success", true);
	}
	
	/**
	 * 上传新广告
	 * @param name
	 * @param note
	 * @param pic
	 * @param action
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/picture.do", params="method=add")
	public @ResponseBody ObjectNode handlerPicAdd(@RequestParam String name,
			@RequestParam String note, @RequestParam CommonsMultipartFile pic,
			@RequestParam String action, HttpServletRequest request) {
		
		try {
			String[] urls = UploadUtil.uploadPicture(request, pic, 
					"/WEB-INF/resources/images/show/", 
					"/resources/images/show/", "show_");
			
			if(urls == null) {
				return mapper.createObjectNode().put("success", false);
			}
			
			Picture picture = new Picture();
			picture.setName(name);
			picture.setNote(note);
			picture.setUptime(new Date());
			picture.setUrl(urls[0]);
			picture.setPath(urls[1]);
			if(action != null && !action.trim().equals("")) {
				picture.setAction(action);
			}
			pictureService.savePicture(picture);
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
			return mapper.createObjectNode().put("success", false);
		}
		return mapper.createObjectNode().put("success", true);
	}
	
	

	public ObjectNode encodeJSON(QueryResult<Picture> list) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ObjectNode json = mapper.createObjectNode();
		ArrayNode records = mapper.createArrayNode();
		json.put("count", list.getTotal());
		Iterator<Picture> it = list.getIterator();
		while(it.hasNext()) {
			ObjectNode node = mapper.createObjectNode();
			Picture pic = it.next();
			node.put("id", pic.getId());
			node.put("name", pic.getName());
			node.put("action", pic.getAction());
			node.put("isshow", pic.getIsshow());
			node.put("uptime", sdf.format(pic.getUptime()));
			node.put("url", pic.getUrl());
			node.put("note", pic.getNote());
			records.add(node);
		}
		json.put("records", records);
		return json;
	}

	public PictureService getPictureService() {
		return pictureService;
	}
	@Resource(name="pictureServiceImpl")
	public void setPictureService(PictureService pictureService) {
		this.pictureService = pictureService;
	}
}
