package util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entity.LinkRobotMessage;
import entity.ManuRobotMessage;
import entity.News;
import entity.NewsRobotMessage;
import entity.RobotMessage;
import util.tuling.Aes;
import util.tuling.Md5;

public class JsonUtil {

	private static String APIKey="a79e3b82535240b690bef284666afa99";
	private static String Secret="8f034242e002d476";
	public static String getJsonString(String message,String userID){
		String data ="{\"key\":\""+APIKey+"\",\"info\":\""+message+"\",\"userid\":\""+userID+"\"}";
		String timeStamp=String.valueOf(System.currentTimeMillis());
		String beforeMD5=Secret+timeStamp+APIKey;
		String afterMD5=Md5.MD5(beforeMD5);
		Aes aes=new Aes(afterMD5);
		data=aes.encrypt(data);
		JSONObject jsonObject=new JSONObject();
		try {
			jsonObject.put("key", APIKey).put("timestamp", timeStamp).put("data", data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String jsonString=jsonObject.toString();
		return jsonString;
	}
	public static RobotMessage getRobotMessage(String jsonStr){
		RobotMessage robotMessage=new RobotMessage();
		JSONObject jSONObject;
		try {
			jSONObject = new JSONObject(jsonStr);
			robotMessage.setCode(jSONObject.getInt("code"));
			robotMessage.setText(jSONObject.getString("text"));
			switch (robotMessage.getCode()) {
				case 200000:
					((LinkRobotMessage)robotMessage).setUrl(jSONObject.getString("url"));;
					break;
				case 302000:
					List<News> newsList=new ArrayList<>(0);
					JSONArray jsonArray=jSONObject.getJSONArray("list");
					for(int i=0;i<jsonArray.length();i++){
						JSONObject jObject=jsonArray.getJSONObject(i);
						News news=new News();
						news.setArticle(jObject.getString("aticle"));
						news.setSource(jObject.getString("source"));
						news.setDetailurl(jObject.getString("detailurl"));
						newsList.add(news);
					}
					((NewsRobotMessage)robotMessage).setList(newsList);
					break;
				case 308000:
					((ManuRobotMessage)robotMessage).setName(jSONObject.getString("name"));
					((ManuRobotMessage)robotMessage).setInfo(jSONObject.getString("info"));
					((ManuRobotMessage)robotMessage).setDetailurl(jSONObject.getString("detailurl"));
					break;
				default:
					break;
			}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		return robotMessage;
	}
}
