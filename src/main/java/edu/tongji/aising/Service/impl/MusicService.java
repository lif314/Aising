package edu.tongji.aising.Service.impl;

//import edu.tongji.aising.Service.Interface.IMusicService;
import edu.tongji.aising.Service.Interface.IMusicService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;

@Service
public class MusicService implements IMusicService {


    /**
     * 腾讯云音乐接口：https://cloud.tencent.com/document/product/1155/40104#Java
     * example: https://github.com/TencentCloud/tencentcloud-sdk-java
     * 暂时不用了，自己写个简单的demo即可
     */

    @Override
    public String getEmotionMusic(String emotionType) throws Exception {

        File musicFile = new File("src/main/resources/music/" + emotionType + "/1.mp3");
        FileInputStream streamMusicFile = new FileInputStream(musicFile);
        byte[] buffer = new byte[(int) musicFile.length()];
        streamMusicFile.read(buffer);
        streamMusicFile.close();
        return Base64.getEncoder().encodeToString(buffer);
    }

    // main test
//    public static void main(String[] args) throws Exception {
//        System.out.println(getEmotionMusic("happiness"));
//    }

}
