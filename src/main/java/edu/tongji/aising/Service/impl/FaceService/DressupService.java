//package edu.tongji.aising.Service.impl.FaceService;
//
//import edu.tongji.aising.Service.Interface.IDressupService;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class DressupService implements IDressupService {
//
//    // AppID
//    private static final String AppID = "24970557";
//    // API KEY
//    private static final String AK = "KtUMfpceHyNGHr1QXj7qIU4Y";
//    // Secret Key
//    private static final String SK = "5mVnxvPkIbAkdk3bPSEoabIklDWTc6LC";
//
//
//    @Override
//    public String dressUp(String sourceBase64, String refBase64) {
//
//        return virtualDressUp(sourceBase64, refBase64);
//    }
//
//
//    public static void main(String[] args) {
//        String str = "";
//        String str1 = "";
//        System.out.println(virtualDressUp(str, str1));
//    }
//
//
//
//    private static String virtualDressUp(String sourceBase64, String refBase64){
//
//        // 请求url
//        String url = "https://aip.baidubce.com/rest/2.0/face/v1/transfer";
//        try {
//            Map<String, Object> map = new HashMap<>();
//            map.put("appid", AppID);
//            Map<String, String> sourceMap = new HashMap<>();
//            sourceMap.put("image", sourceBase64);
//            sourceMap.put("image_type", "BASE64");
//            map.put("source_image", sourceMap); // 默认只返回face_token
//            Map<String, String> refMap =  new HashMap<>();
//            refMap.put("image", refBase64);
//            refMap.put("image_type", "BASE64");
//            map.put("reference_images", refMap);
//
//            String param = GsonUtils.toJson(map);
//
//            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
//            String accessToken = getAuth();
////            System.out.println("token: " + accessToken);
//
//            return HttpUtil.post(url, accessToken, "application/json", param);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//
//
//    /**
//     * 获取API访问token
//     * 该token有一定的有效期，需要自行管理，当失效时需重新获取.
//     * ak - 百度云官网获取的 API Key
//     * sk - 百度云官网获取的 Securet Key
//     * @return assess_token 示例：
//     * "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567"
//     */
//    private static String getAuth() {
//        // 获取token地址
//        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
//        String getAccessTokenUrl = authHost
//                // 1. grant_type为固定参数
//                + "grant_type=client_credentials"
//                // 2. 官网获取的 API Key
//                + "&client_id=" + AK
//                // 3. 官网获取的 Secret Key
//                + "&client_secret=" + SK;
//        try {
//            URL realUrl = new URL(getAccessTokenUrl);
//            // 打开和URL之间的连接
//            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
//            connection.setRequestMethod("GET");
//            connection.connect();
//            // 获取所有响应头字段
//            Map<String, List<String>> map = connection.getHeaderFields();
//            // 遍历所有的响应头字段
//            for (Object key : ((Map<?, ?>) map).keySet()) {
//                System.err.println(key + "--->" + map.get(key));
//            }
//            // 定义 BufferedReader输入流来读取URL的响应
//            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            StringBuilder result = new StringBuilder();
//            String line;
//            while ((line = in.readLine()) != null) {
//                result.append(line);
//            }
//
//            /**
//             * 返回结果示例
//             */
//            System.err.println("result:" + result);
//            JSONObject jsonObject = new JSONObject(result.toString());
//            return jsonObject.getString("access_token");
//
//        } catch (Exception e) {
//            System.err.print("获取token失败！");
//            e.printStackTrace(System.err);
//        }
//        return null;
//    }
//
//
//}
