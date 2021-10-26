package edu.tongji.aising.Controller;

import edu.tongji.aising.Service.Interface.IMusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("")
@CrossOrigin
public class MusicController {

    @Autowired
    public IMusicService musicService;

    @PostMapping("/music")
    public @ResponseBody
    String randomMusic(@RequestParam String type) throws Exception {
        return musicService.getEmotionMusic(type);
    }

}
