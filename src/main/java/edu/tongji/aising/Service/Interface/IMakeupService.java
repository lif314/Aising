package edu.tongji.aising.Service.Interface;

import org.springframework.stereotype.Service;

@Service
public interface IMakeupService {

    /**
     * 虚拟化妆
     * @param faceBase64 人脸base64格式
     * @param type 口红型号
     * @return 美颜+化妆后人脸base64数据
     */
    public String makeup(String faceBase64, String type);
}
