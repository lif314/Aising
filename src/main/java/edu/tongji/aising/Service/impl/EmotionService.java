package edu.tongji.aising.Service.impl;

import edu.tongji.aising.Service.Interface.IEmotionService;
import org.json.JSONArray;
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
public class EmotionService implements IEmotionService {

    /**
     * emotion相关的数据
     * @param faceBase64 人脸图像Base64
     * @return 返回字符串，实际是json
     */
    public String emotionRecognition(String faceBase64){

        String url = "https://api-cn.faceplusplus.com/facepp/v3/detect";
        byte[] buff = DatatypeConverter.parseBase64Binary(faceBase64);
        HashMap<String, String> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        map.put("api_key", API_KEY);
        map.put("api_secret", API_SECRET);
        map.put("return_landmark", "1");
//        map.put("return_attributes", "gender,age,smiling,headpose,facequality,blur,eyestatus,emotion,ethnicity,beauty,mouthstatus,eyegaze,skinstatus");
        map.put("return_attributes", "emotion"); // 只读取表情信息
        byteMap.put("image_file", buff);
        try{
            byte[] bacd = post(url, map, byteMap);
            return dataProcessing(new String(bacd));
//            return new String(bacd);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // EmotionService test: main test
    public static void main(String[] args) throws Exception{

        File file = new File("D:\\AllFile\\LearningFile\\Code\\SpringBoot\\Aising\\src\\main\\resources\\img\\test2.jpg");
        byte[] buff = getBytesFromFile(file);
        System.out.println(buff);
        String url = "https://api-cn.faceplusplus.com/facepp/v3/detect";
        HashMap<String, String> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        map.put("api_key", API_KEY);
        map.put("api_secret", API_SECRET);
        map.put("return_landmark", "1");
//        map.put("return_attributes", "gender,age,smiling,headpose,facequality,blur,eyestatus,emotion,ethnicity,beauty,mouthstatus,eyegaze,skinstatus");
        map.put("return_attributes", "emotion"); // 只读取表情信息
        byteMap.put("image_file", buff);
        try{
            byte[] bacd = post(url, map, byteMap);
//          String str = new String(bacd);
            String str =  dataProcessing(new String(bacd));
            System.out.println(str);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 接口官网：https://www.faceplusplus.com.cn/emotion-recognition/
     * example:https://console.faceplusplus.com.cn/documents/6329752
     * 解决方案：
     *      [1] 调用face++情绪识别，返回情绪类别
     */

    // 连接30s超时
    private final static int CONNECT_TIME_OUT = 30000;
    // 读取50s超时
    private final  static int READ_OUT_TIME = 50000;
    // API_KEY
    private final static String API_KEY = "m1tRsZnp0Z9nF7ul03oL7KIYBsvIJvzr";
    // SECRET
    private  final static String API_SECRET = "Lijixu0yqZY__7C__BEjeUe43wVIkcIn";

    private static final String boundaryString = getBoundary();

    /**
     * post请求
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
            String key = (String) ((Map.Entry) stringStringEntry).getKey();
            String value = (String) ((Map.Entry) stringStringEntry).getValue();
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
     *
     *
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


    /**
     * 数据预处理：由于Face++返回的json数据包含信息众多，我们只需要其中关于emotion的数据
     * 详细数据返回数据见：https://console.faceplusplus.com.cn/documents/4888373
     *
     * {
     *      "request_id",
     *      "time_used",
     *      "faces"[
     *          "face_token",
     *          "face_rectangle",
     *          "landmark",
     *          "attributes"{
     *              "emotion"{
     *                  "anger":0.004,
     *                  "disgust":0.004,
     *                  "fear":0.016,
     *                  "happiness":91.362,
     *                  "neutral":8.600,
     *                  "sadness":0.004,
     *                  "surprise":0.010
     *              }
     *          }
     *      ],
     *      "image_id",
     *      "face_num",
     * }
     *
     * @param str 传入的数据字符串
     * @return 经过提取的字符串
     */
    public static String dataProcessing(String str){
        // [1] 转成json数据
        JSONObject jsonObject = new JSONObject(str);
        // [2] 提取emotion相关数据
        //注意：family中的内容带有中括号[]，所以要转化为JSONArray类型的对象
        JSONArray faces = jsonObject.getJSONArray("faces");
//        System.out.println(faces);
        JSONObject Attributes = faces.getJSONObject(0);
//        System.out.println(Attributes);
        JSONObject attributes = (JSONObject) Attributes.get("attributes");
//        System.out.println(attributes);
        JSONObject emotion = attributes.getJSONObject("emotion");
        // [3] 返回数据
        return emotion.toString();
    }

}
