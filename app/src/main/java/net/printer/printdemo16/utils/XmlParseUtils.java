package net.printer.printdemo16.utils;

import android.util.Xml;

import net.printer.printdemo16.pojo.XmlPojo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlParseUtils {

    /**
     * 获取xml内容
     * @param xml
     * @return
     * @throws Exception
     */
    public static ArrayList<XmlPojo> getXmlData(InputStream xml)throws Exception
    {
        //XmlPullParserFactory pullPaser = XmlPullParserFactory.newInstance();
        ArrayList<XmlPojo> persons = null;
        XmlPojo xmlPojo = null;
        // 创建一个xml解析的工厂
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        // 获得xml解析类的引用
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(xml, "UTF-8");
        // 获得事件的类型
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    persons = new ArrayList<XmlPojo>();
                    break;
                case XmlPullParser.START_TAG:
                    if ("XmlPojo".equals(parser.getName())) {
                        xmlPojo = new XmlPojo();
                        // 取出属性值
                        int id = Integer.parseInt(parser.getAttributeValue(0));
                        xmlPojo.setAreaNo(String.valueOf(id));
                    } else if ("name".equals(parser.getName())) {
                        String name = parser.nextText();// 获取该节点的内容
                        xmlPojo.setCarNo(name);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if ("xmlPojo".equals(parser.getName())) {
                        persons.add(xmlPojo);
                        xmlPojo = null;
                    }
                    break;
            }
            eventType = parser.next();
        }
        return persons;
    }

    /**
     * 保存xml
     * @param xmlPojos
     * @param out
     * @throws Exception
     */
    public static void saveXmlData(List<XmlPojo> xmlPojos, OutputStream out) throws Exception {
        XmlSerializer serializer = Xml.newSerializer();
        serializer.setOutput(out, "UTF-8");
        serializer.startDocument("UTF-8", true);
        serializer.startTag(null, "persons");
        for (XmlPojo p : xmlPojos) {
            serializer.startTag(null, "person");
            serializer.attribute(null, "id", p.getAreaNo() + "");
            serializer.startTag(null, "name");
            serializer.text(p.getCarNo());
            serializer.endTag(null, "name");
            serializer.endTag(null, "person");
        }

        serializer.endTag(null, "persons");
        serializer.endDocument();
        out.flush();
        out.close();
    }
}
