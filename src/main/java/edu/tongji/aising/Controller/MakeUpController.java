package edu.tongji.aising.Controller;


import edu.tongji.aising.Service.Interface.IFaceBeautifyService;
import edu.tongji.aising.Service.Interface.IMakeupService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Controller
@RequestMapping("")
public class MakeUpController {

    @Autowired
    private IMakeupService makeupService;

    @Autowired
    private IFaceBeautifyService faceBeautifyService;

    /**
     * 虚拟化妆
     * @param faceBase64 人脸base64编码
     * @param type 口红型号
     * @return 返回化好妆的人脸base64(经过美颜)
     */
    @PostMapping("/makeup")
    public @ResponseBody
    String makeup(@RequestParam String faceBase64,@RequestParam String type){
        String result = faceBeautifyService.faceBeautify(faceBase64);
        JSONObject json = new JSONObject(result);
        String face = json.getString("result");
        return makeupService.makeup(face, type);
    }
}
