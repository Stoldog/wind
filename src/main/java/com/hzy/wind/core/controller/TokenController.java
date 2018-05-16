package com.hzy.wind.core.controller;

import com.hzy.wind.core.pojo.TokenPOJO;
import com.hzy.wind.utils.JwtUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * Created by EduHzy-019 on 2018-05-16.
 */
@RestController
@RequestMapping("/api/v1/token")
public class TokenController {
    public static final String secretKey = "1&$OAlAsh!wJCIJU";

    @ApiOperation(value = "生成TOKEN",notes = "为客户端生成TOKEN")
    @CrossOrigin(origins = {"http://192.168.0.67:8000","http://localhost:8000"})
    @PostMapping("generate")
    public String generateToken(@RequestBody TokenPOJO pojo){
        long TTLMillis = (pojo.getTTLMillis()>0?pojo.getTTLMillis():1000L*60L*60L);
        return JwtUtil.createJWT(pojo.getUserId(),pojo.getUserName(),pojo.getRole(),pojo.getRoomId(),"WIND","TANGT",TTLMillis,secretKey);
    }
}
