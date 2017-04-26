package entity;

import java.util.List;

public class NewsRobotMessage extends RobotMessage {

	public static int code_=302000;
	private List<News> list;
	public List<News> getList() {
		return list;
	}
	public void setList(List<News> list) {
		this.list = list;
	}
	
	
}
