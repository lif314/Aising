package edu.tongji.aising.XMLRepository.crud;

import edu.tongji.aising.XMLRepository.util.Assert;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class XmlCrud<T> {

    /**
     * XML 数据库存储路径，由配置文件 Xmldb.properties指定
     * 用户只需要在资源文件目录下配置使用即可
     */
    private static String XmldbPath = null;

    private static String InstancePath = null;

    private static String ClassName = null;

    private static Class InstanceClass = null;

    /**
     * 构造函数：创建构造函数的同时加载配置文件，配置全局的数据库存储路径
     */
    public XmlCrud(Class<T> tClass) {
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
        Assert.notNull(inputStream, "Properties  file not found");

        try {
            pro.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         *  [2] 获取配置文件中定义的数据
         */
        // 配置文件中读取数据库路径
        XmldbPath = pro.getProperty("XmldbPath");
        // 类名 ---- 加s成为根元素
        InstanceClass = tClass;
        ClassName = tClass.getSimpleName();
        InstancePath = XmldbPath + "\\" + ClassName + "s.xml";
    }

    /**
     * 数据存储
     *
     * @param entity 实例类
     */

    @SafeVarargs
    public final void save(T... entity) {

        Assert.notNull(entity, "Entity Class must not be null");

        // 构建doc对象
        // [1] 获取对象类 --- 取第0个元素初始化
        Class<?> entityClass = entity[0].getClass();

        // [2] 通过DocumentHelper生成一个Document对象
        Document doc = DocumentHelper.createDocument();

        // [3] 添加根元素 ---- 类名
        String className = entityClass.getSimpleName();
//        System.out.println(className);  // 返回User[] 数组
        String rootName = className + "s";
        Element root = doc.addElement(rootName);  // 类名+s作为根元素，同时作为文件名


        // 迭代遍历参数表，为每个实例创建xml文件
        for (T t : entity) {
            // 获取该类
            Class<?> tClass = t.getClass();
            // 类名
            String simpleName = tClass.getSimpleName();
            // [4] 迭代添加根元素和元素值
            // 为根元素添加子元素
            Element element = root.addElement(simpleName);
            // 获取类的所有属性值
            Field[] declaredFields = tClass.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);  // 暴力反射
                try {
                    // 为子元素添加属性 --- 假设每个子元素都有id ,
                    // element.addAttribute("id", "01");
                    // System.out.println(declaredField.getName());
                    Element elem = element.addElement(declaredField.getName());
                    // 为属性设置值
                    // System.out.println(declaredField.get(t));
                    elem.addText(declaredField.get(t).toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }

        System.out.println("========== XML数据库路径 ============");
        System.out.println(XmlCrud.XmldbPath);


        // 将doc输入到文件中
        // Pretty print the document to System.out
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter prettyWriter = null;
        try {
            // 文件路径
            prettyWriter = new XMLWriter(new FileWriter(new File(XmldbPath + "\\\\" + rootName + ".xml")), format);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            // 写入文件
            assert prettyWriter != null;
            prettyWriter.write(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // 关闭文件
            prettyWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 解析XML文件：通过XML文件创建实例
     *
     * @param entity 传入类，eg:User.class
     * @return 返回该类的实例对象
     */
    public List<T> init(Class<T> entity) {

        // 解析XML文件的入口是一个Document对象
        // [1] 创建SAXReader对象，用于读取xml文件
        SAXReader reader = new SAXReader();

        // [2] 获取类对象的名称，加s后作为文件名
//        String className = entity.getSimpleName();
        // 文件读取路径
//        String filePath = XmldbPath  + "\\" + className + "s.xml";

        // [3] 读取xml文件，得到Document对象
        Document doc = null;
        try {
//            System.out.println(filePath);
            doc = reader.read(new File(InstancePath));
        } catch (DocumentException e) {
            e.printStackTrace();
        }

//        System.out.println(doc);

        // [3] 获取根元素
        Assert.notNull(doc, "File Path Not Found");
        Element root = doc.getRootElement();
//        System.out.println(root);  // Users

        // [4] 根据根元素找子元素 ---- 迭代器
        Iterator<Element> it = root.elementIterator(); // 返回对象强制装换

        /**
         * 获取类的属性集合来创建对象
         */
        Field[] declaredFields = entity.getDeclaredFields();

        // 获取类的方法集合初始化实例
        Method[] declaredMethods = entity.getDeclaredMethods();

        // 获取对象的构造函数
        Constructor<T> constructor = null;
        try {
            constructor = entity.getConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Assert.notNull(constructor, "Class's Constructor Not Found");


        List<T> list = new ArrayList<>();
        while (it.hasNext()) {
            // 取出元素对象 ---- 存有一个实例类的数据
            Element e = it.next();
//            System.out.println("e:" + e);  // User

            // 通过构造函数创建实例对象
            T newInstance = null;
            try {
                newInstance = constructor.newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                ex.printStackTrace();
            }
            Assert.notNull(newInstance, "Can't get Object's Instance");

            /**
             * 遍历方法，初始化对象
             */
            for (Field declaredField : declaredFields) {
                String attrName = declaredField.getName(); // 属性名
                // 属性名的首字母转换为大写后再加上set，即为方法名
                char[] chars = attrName.toCharArray();
                if (97 <= chars[0] && chars[0] <= 122) {
                    // 进行字母的ascii编码前移，效率最高
                    chars[0] ^= 32;
                }

//                System.out.println(String.valueOf(chars));

                // 拼接set方法名
                String methodName = "set" + String.valueOf(chars);      // set 方法名
                // 通过方法名获取方法
                Method method = null;
                try {
//                    System.out.println(methodName);       // setUserId
                    method = entity.getDeclaredMethod(methodName, String.class);
                } catch (NoSuchMethodException ex) {
                    ex.printStackTrace();
                }

                // 执行方法
                try {
                    assert method != null;
//                    System.out.println(e.element(attrName).getText());
                    method.invoke(newInstance, e.element(attrName).getText());
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    ex.printStackTrace();
                }
            }

            // 添加实例对象
            list.add(newInstance);
        }

        return list;
    }


    /**
     * 根据ID删除XML数据库中的数据
     *
     * @param id 需要删除元素的id
     */
    public final void deleteById(String id) {

        // 创建读取对象
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(new File(InstancePath));
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        // 获取根元素
        assert document != null;
        Element root = document.getRootElement();

        /**
         * 迭代器查找要删除的元素
         */
        Iterator<Element> it = root.elementIterator(); // 返回对象强制装换

        while (it.hasNext()) {
            // 获取元素
            Element next = it.next();
            String id1 = next.element("Id").getText();
            if (id.equals(id1)) {
                next.getParent().remove(next);
                break;  // 只删除第一个id相等的元素
            }
        }

        // 格式化输出流，同时指定编码格式。也可以在FileOutputStream中指定。
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");

        XMLWriter writer = null;
        try {
            writer = new XMLWriter(new FileOutputStream(InstancePath), format);
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            assert writer != null;
            writer.write(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据ID寻找对象
     *
     * @param id 元素ID
     * @return 返回相同ID组成的对象
     */
    public T findById(String id) {

        // 创建读取对象
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(new File(InstancePath));
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        // 获取根元素
        assert document != null;
        Element root = document.getRootElement();

        /**
         * 迭代器查找要删除的元素
         */
        Iterator<Element> it = root.elementIterator(); // 返回对象强制装换

        /**
         * 获取类的属性集合来创建对象
         */
        Field[] declaredFields = InstanceClass.getDeclaredFields();

        // 获取类的方法集合初始化实例
        Method[] declaredMethods = InstanceClass.getDeclaredMethods();

        // 获取对象的构造函数
        Constructor<T> constructor = null;
        try {
            constructor = InstanceClass.getConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Assert.notNull(constructor, "Class's Constructor Not Found");


//        List<T> list = new ArrayList<>();
        while (it.hasNext()) {
            // 获取元素
            Element next = it.next();
            String id1 = next.element("Id").getText();
            if (id.equals(id1)) {

                // 反序列化为class对象

                // 通过构造函数创建实例对象
                T newInstance = null;
                try {
                    newInstance = constructor.newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                    ex.printStackTrace();
                }
                Assert.notNull(newInstance, "Can't get Object's Instance");

                /**
                 * 遍历方法，初始化对象
                 */
                for (Field declaredField : declaredFields) {
                    String attrName = declaredField.getName(); // 属性名
                    // 属性名的首字母转换为大写后再加上set，即为方法名
                    char[] chars = attrName.toCharArray();
                    if (97 <= chars[0] && chars[0] <= 122) {
                        // 进行字母的ascii编码前移，效率最高
                        chars[0] ^= 32;
                    }

                    // 拼接set方法名
                    String methodName = "set" + String.valueOf(chars);      // set 方法名
                    // 通过方法名获取方法
                    Method method = null;
                    try {
//                    System.out.println(methodName);       // setUserId
                        method = InstanceClass.getDeclaredMethod(methodName, String.class);
                    } catch (NoSuchMethodException ex) {
                        ex.printStackTrace();
                    }

                    // 执行方法
                    try {
                        assert method != null;
//                    System.out.println(e.element(attrName).getText());
                        method.invoke(newInstance, next.element(attrName).getText());
                    } catch (IllegalAccessException | InvocationTargetException ex) {
                        ex.printStackTrace();
                    }

                }

                return newInstance;
            }
        }
        return null;
    }


    public T updateById(String oldValue, String newValue){



        return null;
    }

}