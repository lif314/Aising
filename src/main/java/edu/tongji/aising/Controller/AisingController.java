package edu.tongji.aising.Controller;

import edu.tongji.aising.Service.Interface.IEmotionService;
import edu.tongji.aising.Service.Interface.IFaceBeautifyService;
import edu.tongji.aising.Service.Interface.IMakeupService;
import edu.tongji.aising.Service.Interface.IMusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 实现情绪检测+音乐播放+虚拟化妆
 */

@RequestMapping("")
@CrossOrigin
@Controller
public class AisingController {

    @Autowired
    public IMakeupService makeupService;
    @Autowired
    public IMusicService musicService;
    @Autowired
    public IEmotionService emotionService;
    @Autowired
    public IFaceBeautifyService faceBeautifyService;

    @RequestMapping("/Aising")
    public @ResponseBody
    String  Aising(@RequestParam String faceBase64, @RequestParam String type) throws Exception {
        /**
         * [1] 根据人脸信息识别人脸情绪
         */
        String emotionResult = emotionService.emotionRecognition(faceBase64);

        String emotionType = "happiness";
        /**
         * [2] 由情绪最高值调用音乐播放接口，获取歌曲
         */
        String musicResult = musicService.getEmotionMusic(emotionType);

        /**
         * [3] 对人脸进行美颜
         */
        String faceBeautifyResult = faceBeautifyService.faceBeautify(faceBase64);

        /**
         * [4] 虚拟化妆
         */
        String makeup = makeupService.makeup(faceBeautifyResult, type);

        return "{" + musicResult + ","  +  emotionResult + "," + makeup + "}";
    }

}
