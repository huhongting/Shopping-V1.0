package cn.edu.jnu.web.dao.impl;

import java.io.File;
import java.io.Serializable;

import org.springframework.stereotype.Repository;

import cn.edu.jnu.web.dao.base.DaoSupport;
import cn.edu.jnu.web.entity.Picture;

/**
 * ǰ̨����ͼƬʵ�����DAO���̳�DaoSupport
 * Ϊ����ͼƬ�ṩ��������ɾ�Ĳ����
 * ��дdelete��������ɾ��������дΪ���ݿ�ɾ���ͱ����ļ�ɾ��
 * @author HHT
 *
 */
@Repository
public class PictureDaoImpl extends DaoSupport {

	/**
	 * ��д�û�ɾ��������ɾ������ʱɾ�������ļ�
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
		return false;// �ļ�������
	}
}
