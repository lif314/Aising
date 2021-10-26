//package edu.tongji.aising.Repository;
//
//import com.fasterxml.jackson.dataformat.xml.XmlMapper;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * test model
// */
//public class JacksonTest {
//    public static void main(String[] args) throws IOException {
//
//
//        User user1 = new User();
//        user1.setUserId(2322323);
//        user1.setUserName("test");
//        user1.setUserPasswd("2sdfdw342");
//        user1.setUserPhoto("dsfsdd2sdfeyrry");
//
//        User user2 = new User();
//        user2.setUserId(000023);
//        user2.setUserName("test2");
//        user2.setUserPasswd("2sdfdw2");
//        user2.setUserPhoto("feyrry");
//
//        List<User> list = new ArrayList<>();
//        list.add(user1);
//        list.add(user2);
//
//        Person person = new Person();
//        person.setId("12121");
//        person.setName("llf");
//        person.setPassword("aasa32342432");
//        person.setAddress("Tongji University");
//        person.setUserList(list);
//
//
////        XmlMapper xmlMapper = new XmlMapper();
//        // 写入xml文件中
////        xmlMapper.writeValue(new File("D:\\AllFile\\LearningFile\\Code\\SpringBoot\\Aising\\src\\main\\resources\\XmlDB\\personJacken.xml"), person);
//
//    }
//}
