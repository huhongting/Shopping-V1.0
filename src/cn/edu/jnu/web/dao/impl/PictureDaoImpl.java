package cn.edu.jnu.web.dao.impl;

import java.io.File;
import java.io.Serializable;

import org.springframework.stereotype.Repository;

import cn.edu.jnu.web.dao.base.DaoSupport;
import cn.edu.jnu.web.entity.Picture;

/**
 * 前台滚动图片实体类的DAO，继承DaoSupport
 * 为滚动图片提供基本的增删改查操作
 * 重写delete方法，将删除操作重写为数据库删除和本地文件删除
 * @author HHT
 *
 */
@Repository
public class PictureDaoImpl extends DaoSupport {

	/**
	 * 重写用户删除操作，删除数据时删除磁盘文件
	 */
	@Override
	public <T> T delete(Class<T> clazz, Serializable entityId) {
		Picture pic = find(Picture.class, entityId);
		String url = pic.getUrl();
		deleteFile(url);
		this.hibernateTemplate.delete(pic);
		return null;
	}

	private boolean deleteFile(String url) {
		File file = new File(url);
		if(file.exists()) {
			return file.delete();
		}
		return false;// 文件不存在
	}
}
