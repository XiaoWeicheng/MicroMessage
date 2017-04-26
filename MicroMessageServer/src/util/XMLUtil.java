package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;

import entity.Message;
import entity.TextMessage;

public class XMLUtil {

	@SuppressWarnings("unchecked")
	public static Map<String, String>getXMLContent(HttpServletRequest request) throws IOException{

		Map<String, String> xMLMap=new HashMap<>();
		InputStream inputStream=request.getInputStream();
		SAXReader saxReader=new SAXReader();
		try {
			Document document=saxReader.read(inputStream);
			Element rootElement=document.getRootElement();
			List<Element> elements=rootElement.elements();
			for(Element element:elements){
				xMLMap.put(element.getName(), element.getText());
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return xMLMap;
	}
	private static XStream xStream=new XStream();
	public static String toXMLString(Message message){
		String xMLString=null;
		if("text".equals(message.getMsgType())){
			TextMessage textMessage=(TextMessage)message;
			xStream.alias("xml", textMessage.getClass());
			xMLString=xStream.toXML(textMessage);
		}
		return xMLString;
	}
}
