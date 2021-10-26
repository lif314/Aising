package edu.tongji.aising.Controller;


import edu.tongji.aising.Service.Interface.IEmotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Controller
@RequestMapping(path = "")
public class EmotionController {

    @Autowired
    public IEmotionService emotionService;

    @PostMapping(path = "/emotion")
    public @ResponseBody
    String emotionRecognition(@RequestParam String faceBase64){
         return emotionService.emotionRecognition(faceBase64);
    }
}