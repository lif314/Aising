package edu.tongji.aising.XMLRepository.crud;

import com.google.common.annotations.VisibleForTesting;
import edu.tongji.aising.Repository.Person;

import java.util.List;


/**
 * Crud Test Demo5
 */


public class XmlCrudTest {
    public static void main(String[] args) {

        // save test
        Person person1 = new Person();
        person1.setId("00001");
        person1.setName("lilinfei");
        person1.setPassword("12312313");
        person1.setAddress("ShangHai Tongji University");

        Person person2 = new Person();
        person2.setId("00002");
        person2.setName("ZhaoZiYu");
        person2.setPassword("3j3opazcmxzaaa");
        person2.setAddress("ShangHai Tongji University 20 533");

        Person person3 = new Person();
        person3.setId("00003");
        person3.setName("ShaoGuoCheng");
        person3.setPassword("sasasa433545a");
        person3.setAddress("ShangHai Tongji University JiaDing");

        // 创建针对具体对象(Person)的XML数据库上下文
        XmlCrud<Person> xmlCrud = new XmlCrud<>(Person.class);

        // save test ---- add
        xmlCrud.save(person1, person2, person3);

        // init test
        // init接口 --- 初始化数据，读取文件中所有的数据
        List<Person> init = xmlCrud.init(Person.class);
        for (Person person : init) {
            System.out.println(person.toString());
            System.out.println("=======");
        }

        // deleteById test   参数规定为字符串
        xmlCrud.deleteById(person1.getId());


        // findById test
        Person byId = xmlCrud.findById(person2.getId());
        System.out.println(byId.toString());

    }
}
