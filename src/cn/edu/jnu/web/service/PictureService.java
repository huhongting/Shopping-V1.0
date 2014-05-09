package cn.edu.jnu.web.service;

import cn.edu.jnu.web.entity.Picture;
import cn.edu.jnu.web.util.QueryResult;

public interface PictureService {
	/**
	 * 保存
	 * @param pic
	 */
	void savePicture(Picture pic);
	/**
	 * 删除
	 * @param id
	 */
	void delerePicture(int id);
	/**
	 * 查找
	 * @param id
	 * @return
	 */
	Picture findById(int id);
	/**
	 * 更新
	 * @param picture
	 */
	void updatePicture(Picture picture);
	/**
	 * 分页条件查询
	 * @param start
	 * @param limit
	 * @param ands
	 * @return
	 */
	QueryResult<Picture> listPictures(int start, int limit, String[] ands);
}
