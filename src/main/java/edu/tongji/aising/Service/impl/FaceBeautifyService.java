package edu.tongji.aising.Service.impl;


import edu.tongji.aising.Service.Interface.IFaceBeautifyService;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLException;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class FaceBeautifyService implements IFaceBeautifyService {


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
     * 人脸美颜 ---- Face++ URL:https://api-cn.faceplusplus.com/facepp/v1/beautify
     * api文档：https://console.faceplusplus.com.cn/documents/34878217
     * @param faceBase64 人脸图像Base64编码
     * @return base64 图片数据
     */
    @Override
    public String faceBeautify(String faceBase64) {
        String url = "https://api-cn.faceplusplus.com/facepp/v1/beautify";

        /**
         * 参数设置
         */
        HashMap<String, String> map = new HashMap<>();
        map.put("api_key", API_KEY);
        map.put("api_secret", API_SECRET);
        map.put("image_base64", faceBase64);

        // ByteMap: 无用参数，置为空
        try{
            byte[] bacd = post(url, map, null);
            String str = new String(bacd);
            System.out.println("faceBeautify:" + str);

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
     * 编码
     * @return 编码结果串
     * @throws Exception URLEncoder为空
     */
    private static String encode() throws Exception{
        return URLEncoder.encode(" ", StandardCharsets.UTF_8);
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


}
