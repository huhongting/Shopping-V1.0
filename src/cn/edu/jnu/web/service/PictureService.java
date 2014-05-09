package cn.edu.jnu.web.service;

import cn.edu.jnu.web.entity.Picture;
import cn.edu.jnu.web.util.QueryResult;

public interface PictureService {
	/**
	 * ����
	 * @param pic
	 */
	void savePicture(Picture pic);
	/**
	 * ɾ��
	 * @param id
	 */
	void delerePicture(int id);
	/**
	 * ����
	 * @param id
	 * @return
	 */
	Picture findById(int id);
	/**
	 * ����
	 * @param picture
	 */
	void updatePicture(Picture picture);
	/**
	 * ��ҳ������ѯ
	 * @param start
	 * @param limit
	 * @param ands
	 * @return
	 */
	QueryResult<Picture> listPictures(int start, int limit, String[] ands);
}
