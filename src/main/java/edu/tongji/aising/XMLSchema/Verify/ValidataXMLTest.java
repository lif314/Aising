package edu.tongji.aising.XMLSchema.Verify;

import edu.tongji.aising.XMLRepository.crud.XmlCrud;
import edu.tongji.aising.XMLRepository.util.Assert;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXValidator;
import org.dom4j.io.XMLWriter;
import org.dom4j.util.XMLErrorHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;
import java.util.Properties;

/**
 * XML Schema校验XML文件
 */
public class ValidataXMLTest<T> {

    // XML 文件存放路径
    private static String XmlPath = null;

    // XML Schema文件存放路径
    private static String XsdPath = null;

    private static String DtdPath = null;

    public ValidataXMLTest(Class<T> tClass){

        /**
         * [1] 加载配置文件
         */
        // 创建对象
        Properties pro = new Properties();
        // 加载配置文件，转换为一个集合 --- 类加载器
        // 获取class目录下的配置文件
        ClassLoader classLoader = XmlCrud.class.getClassLoader();
        // 获取字节流 --- 找到配置文件并加载为字节流
        InputStream inputStream = classLoader.getResourceAsStream("Xmldb.properties");
        // 错误处理
        Assert.notNull(inputStream, "Properties file not found");

        try {
            pro.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         *  [2] 获取配置文件中定义的数据
         */
        // 获取对象的类名，默认对象名+s为文件名
        String className = tClass.getSimpleName();
        // XML 文件路径名
        XmlPath = pro.getProperty("XmlPath") + "\\" +  className + "s.xml";
//        System.out.println("XmlPath: "+ XmlPath);
        // XML Schema文件路径
        XsdPath = pro.getProperty("XsdPath") + "\\" + className + "s.xsd";
//        System.out.println("XsdPath: " + XsdPath);
        // DTD 文件路径
        DtdPath = pro.getProperty("DtdPath") + "\\" + className + "s.dtd";
//        System.out.println("DtdPath: " + DtdPath);
    }


    /**
     * 通过XML Schema文件校验XML文件
     * @return true:  校验成功
     *         false: 校验失败
     */
    public final boolean validateXMLByXSD(){

        try {
            // 创建默认的XML错误处理器
            XMLErrorHandler errorHandler = new XMLErrorHandler();
            // 获取基于 SAX 的解析器的实例
            SAXParserFactory factory = SAXParserFactory.newInstance();
            // 解析器在解析时验证 XML 内容。
            factory.setValidating(true);
            // 指定由此代码生成的解析器将提供对 XML 名称空间的支持。
            factory.setNamespaceAware(true);
            // 使用当前配置的工厂参数创建 SAXParser 的一个新实例。
            SAXParser parser = factory.newSAXParser();
            // 创建一个读取工具
            SAXReader xmlReader = new SAXReader();
            // 获取要校验xml文档实例
            Document xmlDocument = (Document) xmlReader.read(new File(XmlPath));
            // 设置 XMLReader 的基础实现中的特定属性。核心功能和属性列表可以在 [url]http://sax.sourceforge.net/?selected=get-set[/url] 中找到。
            parser.setProperty(
                    "http://java.sun.com/xml/jaxp/properties/schemaLanguage",
                    "http://www.w3.org/2001/XMLSchema");
            parser.setProperty(
                    "http://java.sun.com/xml/jaxp/properties/schemaSource",
                    "file:" + XsdPath);

            // 创建一个SAXValidator校验工具，并设置校验工具的属性
            SAXValidator validator = new SAXValidator(parser.getXMLReader());
            // 设置校验工具的错误处理器，当发生错误时，可以从处理器对象中得到错误信息。
            validator.setErrorHandler(errorHandler);
            // 校验
            validator.validate(xmlDocument);

            XMLWriter writer = new XMLWriter(OutputFormat.createPrettyPrint());
            // 如果错误信息不为空，说明校验失败，打印错误信息
            if (errorHandler.getErrors().hasContent()) {
                writer.write(errorHandler.getErrors());
                return false;
//                System.out.println("XML文件通过XSD文件校验失败！");
            } else {
                return true;
//                System.out.println("XML文件通过XSD文件校验成功！");
            }
        } catch (Exception ex) {
            System.out.println("XML文件: " + XmlPath + " 通过XSD文件:" + XsdPath + "检验失败。\n原因： " + ex.getMessage());
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 通过DTD文件校验XML文件
     * @return true: 成功
     *         false: 失败
     */
    public final boolean validateXMLByDTD(){

        return false;
    }

}
