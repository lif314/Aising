package edu.tongji.aising.Service.Interface;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface IMusicService {

    // 根据情绪类型返回对应歌曲
    String getEmotionMusic(String emotionType) throws IOException, Exception;
}
