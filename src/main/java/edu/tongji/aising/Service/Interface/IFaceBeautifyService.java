package edu.tongji.aising.Service.Interface;

import org.springframework.stereotype.Service;

@Service
public interface IFaceBeautifyService {
    String faceBeautify(String faceBase64);
}
