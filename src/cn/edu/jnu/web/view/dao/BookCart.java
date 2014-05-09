package cn.edu.jnu.web.view.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BookCart implements Serializable {
	private static final long serialVersionUID = -8869060191477205802L;
	
	private List<CarBook> carBooks = new ArrayList<CarBook>();

	public List<CarBook> getCarBooks() {
		return carBooks;
	}

	public void setCarBooks(List<CarBook> carBooks) {
		this.carBooks = carBooks;
	}
	
	public int getCartNumber() {
		//return carBooks.size();
		int num = 0;
		for(CarBook cb : carBooks) {
			num += cb.getNum();
		}
		return num;
	}
	
	public void addBook(CarBook cb) {
		if(this.carBooks.contains(cb)) {
			int i = carBooks.indexOf(cb);
			carBooks.get(i).setNum(carBooks.get(i).getNum() + cb.getNum());
		} else {
			this.carBooks.add(cb);
		}
	}
	
	public void updateBook(CarBook cb) {
		if(this.carBooks.contains(cb)) {
			int i = carBooks.indexOf(cb);
			carBooks.get(i).setNum(cb.getNum());
		} else {
			this.carBooks.add(cb);
		}
	}
	
	public void removeBook(CarBook cb) {
		if(this.carBooks.contains(cb)) {
			carBooks.remove(cb);
		}
	}
	
	public void removeAll() {
		this.carBooks.removeAll(carBooks);
	}
	
}
