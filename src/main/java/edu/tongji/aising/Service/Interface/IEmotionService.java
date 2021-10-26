package edu.tongji.aising.Service.Interface;

import org.springframework.stereotype.Service;

@Service
public interface IEmotionService {

    String emotionRecognition(String faceBase64);
}
