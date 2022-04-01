package com.example.paper.entity.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@ApiModel("用户登录账户密码")
@Accessors(chain = true)
public class UserLoginVO {
    String name;
    String password;
}
