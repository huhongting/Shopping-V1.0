package cn.edu.jnu.web.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.edu.jnu.web.dao.impl.PictureDaoImpl;
import cn.edu.jnu.web.entity.Picture;
import cn.edu.jnu.web.service.PictureService;
import cn.edu.jnu.web.util.QueryResult;

@Service
public class PictureServiceImpl implements PictureService {

	private PictureDaoImpl pictureDao;
	
	@Override
	public void savePicture(Picture pic) {
		pictureDao.save(pic);
	}

	@Override
	public void delerePicture(int id) {
		pictureDao.delete(Picture.class, id);
	}

	@Override
	public Picture findById(int id) {
		return pictureDao.find(Picture.class, id);
	}

	@Override
	public QueryResult<Picture> listPictures(int start, int limit, String[] ands) {
		List<Picture> pics = pictureDao.pagingQuery(Picture.class, start, limit, ands, null);
		QueryResult<Picture> res = new QueryResult<Picture>();
		res.setResults(pics);
		res.setTotal(pictureDao.list(Picture.class, ands).size());
		return res;
	}

	public PictureDaoImpl getPictureDao() {
		return pictureDao;
	}
	@Resource(name="pictureDaoImpl")
	public void setPictureDao(PictureDaoImpl pictureDao) {
		this.pictureDao = pictureDao;
	}

	@Override
	public void updatePicture(Picture picture) {
		pictureDao.update(picture);
	}

}
