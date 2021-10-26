package edu.tongji.aising.Service.impl;

import edu.tongji.aising.Service.Interface.IMakeupService;
import edu.tongji.aising.Service.impl.FaceService.GsonUtils;
import org.hibernate.sql.OracleJoinFragment;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLException;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class MakeupService implements IMakeupService {

    @Override
    public String makeup(String faceBeauty, String type) {
//        String result = faceBeautify(faceBase64);
//        JSONObject json = new JSONObject(result);
//        String face = json.getString("result");
        System.out.println("face:" + faceBeauty);
        System.out.println("type: " + typeDecode(type));
        return faceMakeup(faceBeauty, typeDecode(type));
    }


    // main test
    public static void main(String[] args) throws Exception{

        File file = new File("D:\\AllFile\\LearningFile\\Code\\SpringBoot\\Aising\\src\\main\\resources\\img\\test2.jpg");
        byte[] buff = getBytesFromFile(file);
//        System.out.println(buff);
        String url = "https://api-facestyle.megvii.com/facestyle/v1/makeup";
        HashMap<String, String> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        map.put("api_key", API_KEY);
        map.put("api_secret", API_SECRET);
        byteMap.put("image_file", buff);
        map.put("makeup_config_lipstick", "{\"product_id\": \"21c15a02-4fab-4700-8267-717064149009\", \"product_name\": \"Aising\",\"thickness\": 0.5}");

        try{
            byte[] bacd = post(url, map, byteMap);
            String str = new String(bacd);
            System.out.println(str);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 口红型号解析
     * @param type 口红类别 --- 方便区别，直接传输颜色编码
     * @return 返回product_id
     */
    private String typeDecode(String type){
        switch (type) {
            case "#CF2CB5":
                return "52fe0817-a212-4366-a479-5ebec310a316";
            case "#C93C66":
                return "c2e3c761-a62e-4c70-ad51-bc667e5b77bc";
            case "#C10532":
                return "744d8601-6f8e-490d-af26-8f384f2da9f4";
            case "#72122F":
                return "b7148577-a25e-4d10-80b2-a18524fbf222";
            case "#135F89":
                return "21c15a02-4fab-4700-8267-717064149009";
            case "#E06959":
                return "2f327d28-80c6-444e-a549-8e28f7dbd8ce";
        }
        return null;
    }


    /**
     * 接口官网：https://facestyle-console.megvii.com/documents/259816110
     * 解决方案：
     *      [1] 调用facestyle接口，实现对人物脸部进行化妆
     */

    // 连接30s超时
    private final static int CONNECT_TIME_OUT = 30000;
    // 读取50s超时
    private final  static int READ_OUT_TIME = 50000;
    // API_KEY
    private final static String API_KEY = "M70ODNXzAAk8pN68j22gkZiYdpAqiQIz";
    // SECRET
    private  final static String API_SECRET = "g1yVTDdseKeCF-T87mrIPkwk4v9f4YnP";

    private static final String boundaryString = getBoundary();


    /**
     * 虚拟化妆
     * @param faceBase64 人脸base64
     * @param type 口红型号
     * @return 化妆后的人脸base64值
     */
    private static String faceMakeup(String faceBase64, String type){

        String url = "https://api-facestyle.megvii.com/facestyle/v1/makeup";

        byte[] buff = DatatypeConverter.parseBase64Binary(faceBase64);

        HashMap<String, String> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        map.put("api_key", API_KEY);
        map.put("api_secret", API_SECRET);
        byteMap.put("image_file", buff);
        System.out.println("{\"product_id\": \" " + type + "\", \"product_name\": \"Aising\",\"thickness\": 0.5}");
        map.put("makeup_config_lipstick", "{\"product_id\": \"" + type + "\", \"product_name\": \"Aising\",\"thickness\": 0.5}");

        try{
            byte[] bacd = post(url, map, byteMap);
            String str = new String(bacd);
            System.out.println("faceMakeup:" + str);
            return  str;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 人脸美颜 ---- Face++ URL:https://api-cn.faceplusplus.com/facepp/v1/beautify
     * api文档：https://console.faceplusplus.com.cn/documents/34878217
     * @param faceBase64 人脸图像Base64编码
     * @return base64 图片数据
     */
    private static String faceBeautify(String faceBase64){

        String url = "https://api-cn.faceplusplus.com/facepp/v1/beautify";

        /**
         * 参数设置
         */
        HashMap<String, String> map = new HashMap<>();
        map.put("api_key", "m1tRsZnp0Z9nF7ul03oL7KIYBsvIJvzr");
        map.put("api_secret", "Lijixu0yqZY__7C__BEjeUe43wVIkcIn");
        map.put("image_base64", faceBase64);

        // ByteMap: 无用参数，置为空
        try{
            byte[] bacd = post(url, map, null);
            String str = new String(bacd);
            System.out.println("faceBeautiify:" + str);

            return str;  // 注意Base64的格式
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    /**
     * post请求配置
     * @param url 请求地址url
     * @param map map
     * @param fileMap 文件
     * @return 二进制文件
     * @throws Exception error
     */
    protected static byte[] post(String url, HashMap<String, String> map, HashMap<String, byte[]> fileMap) throws Exception {

        // 请求配置
        HttpURLConnection conne;
        URL url1 = new URL(url);
        conne = (HttpURLConnection) url1.openConnection();
        conne.setDoOutput(true);
        conne.setUseCaches(false);
        conne.setRequestMethod("POST");
        conne.setConnectTimeout(CONNECT_TIME_OUT);
        conne.setReadTimeout(READ_OUT_TIME);
        conne.setRequestProperty("accept", "*/*");
        conne.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundaryString);
        conne.setRequestProperty("connection", "Keep-Alive");
        conne.setRequestProperty("user-agent", "Mozilla/4.0 (compatible;MSIE 6.0;Windows NT 5.1;SV1)");
        DataOutputStream obos = new DataOutputStream(conne.getOutputStream());


        for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
            String key = stringStringEntry.getKey();
            Object value =  stringStringEntry.getValue();
            obos.writeBytes("--" + boundaryString + "\r\n");
            obos.writeBytes("Content-Disposition: form-data; name=\"" + key
                    + "\"\r\n");
            obos.writeBytes("\r\n");
            obos.writeBytes(value + "\r\n");
        }

        if(fileMap != null && fileMap.size() > 0){
            for (Map.Entry<String, byte[]> fileEntry : fileMap.entrySet()) {
                obos.writeBytes("--" + boundaryString + "\r\n");
                obos.writeBytes("Content-Disposition: form-data; name=\"" + fileEntry.getKey()
                        + "\"; filename=\"" + encode() + "\"\r\n");
                obos.writeBytes("\r\n");
                obos.write(fileEntry.getValue());
                obos.writeBytes("\r\n");
            }
        }

        obos.writeBytes("--" + boundaryString + "--" + "\r\n");
        obos.writeBytes("\r\n");
        obos.flush();
        obos.close();
        InputStream ins = null;
        int code = conne.getResponseCode();
        try{
            if(code == 200){
                ins = conne.getInputStream();
            }else{
                ins = conne.getErrorStream();
            }
        }catch (SSLException e){
            e.printStackTrace();
            return new byte[0];
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[4096];
        int len;
        while((len = ins.read(buff)) != -1){
            baos.write(buff, 0, len);
        }
        byte[] bytes = baos.toByteArray();
        ins.close();
        return bytes;
    }



    /**
     * 获取边界 ---- 接口文档需求
     * @return boundaryString
     */
    private static String getBoundary() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < 32; ++i) {
            sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-".charAt(random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_".length())));
        }
        return sb.toString();
    }

    /**
     * 编码
     * @return 编码结果串
     * @throws Exception URLEncoder为空
     */
    private static String encode() throws Exception{
        return URLEncoder.encode(" ", StandardCharsets.UTF_8);
    }

    /**
     * 将图像文件转换为二进制文件
     * @param f 文件
     * @return 二进制字节
     */
    public static byte[] getBytesFromFile(File f) {
        if (f == null) {
            return null;
        }
        try {
            FileInputStream stream = new FileInputStream(f);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = stream.read(b)) != -1)
                out.write(b, 0, n);
            stream.close();
            out.close();
            return out.toByteArray();
        } catch (IOException ignored) {
        }
        return null;
    }
}
