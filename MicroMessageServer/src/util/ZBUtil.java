package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


@SuppressWarnings("unchecked")
public class ZBUtil {

	private static String apiURL;
	private static String pictureURL;
	private static List<String> zbTypes;
	public static List<String> zbName;
	static{
		apiURL="http://itxyz.cn/zb?";
		pictureURL="http://itxyz.cn/images/";
		zbTypes=new ArrayList<>(0);
		zbName=new ArrayList<>(0);
		try {
			InputStream zbInputStream=ZBUtil.class.getClassLoader().getResourceAsStream("ZB.xml");
			SAXReader saxReader=new SAXReader();
			Document document=saxReader.read(zbInputStream);
			Element rootElement=document.getRootElement();
			List<Element> elements=rootElement.elements();
			for (Element element:elements) {
				zbTypes.add(element.element("code").getText());
				zbName.add(element.element("name").getText());
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	public static String getImageURL(int type,String str) throws IOException{
		String imageURL=null;
		String imageName=null;
		URL url=new URL(apiURL+"type="+URLEncoder.encode(zbTypes.get(type),"UTF-8")+"&name="+URLEncoder.encode(str,"UTF-8"));
		HttpURLConnection httpUrlConnection=(HttpURLConnection) url.openConnection();
		httpUrlConnection.connect();
		InputStream inputStream=httpUrlConnection.getInputStream();
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
		imageName=bufferedReader.readLine();
		imageURL=pictureURL+imageName;
		return imageURL;
	}
}
