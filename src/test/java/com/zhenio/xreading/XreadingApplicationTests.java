package com.zhenio.xreading;

import static org.junit.Assert.assertEquals;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONObject;
import com.zhenio.xreading.controller.UserController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class XreadingApplicationTests {

    @Test
    public void contextLoads() {
    }
    //測試單元
    @Test
    public void test() throws Exception {
    	UserController userController = new UserController();
      
        JSONObject json = userController.insertCurrencyData();
        String message = json.getString("message");     
        Assertions.assertThat(message).isEqualTo("insertSuccess");
    }
}
