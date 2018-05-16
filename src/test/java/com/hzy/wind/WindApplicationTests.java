package com.hzy.wind;

import com.hzy.wind.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WindApplicationTests {

    @Test
    public void parseJWT(){
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9BRE1JTiIsInVuaXF1ZV9uYW1lIjoiYWRtaW4wMDAwMDAiLCJ1c2VyX2lkIjoiMSIsImlzcyI6IlRBTkdUIiwiYXVkIjoiV0lORCIsImV4cCI6MTUyNjQ1Njk4NCwibmJmIjoxNTI2NDUzMzg0fQ.8l3Le4_G56Nrp4iL6qamhCPeeN6TM6xgPuIcbxsaimQ";

    }
}
