package com.example.paper.entity.vo;

import com.example.paper.entity.po.UserPO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
@ApiModel("用户详情VO")
public class UserInfoVO {
    private int id;
    private String name;

    public UserInfoVO(UserPO userPO){
        BeanUtils.copyProperties(userPO,this);
    }
}
