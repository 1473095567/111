package com.test.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.test.Enum.ResultEnum;
import com.test.pojo.Result;
import com.test.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.NumberUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class TestController {
    @GetMapping(value="/gettest")
    public Result getTest(String canshu){
        if(!checkInt(canshu))
            return ResultUtil.error(ResultEnum.NOT_INT.getCode(),ResultEnum.NOT_INT.getMsg());
        else
            return ResultUtil.success(canshu);
    }

    @PostMapping(value="/posttest1")
    public Result postTest1(@RequestBody Map<String,String> map){
        if(!map.get("性别").equals("男")&&!map.get("性别").equals("女")){
            return ResultUtil.error(ResultEnum.SEX_ERROR.getCode(),ResultEnum.SEX_ERROR.getMsg());
        }
        if(!checkInt(map.get("学号")))
            return ResultUtil.error(ResultEnum.STUDENTID_ERROR1.getCode(),ResultEnum.STUDENTID_ERROR1.getMsg());
        if(map.get("学号").length()!=7)
            return ResultUtil.error(ResultEnum.STUDENTID_ERROR2.getCode(),ResultEnum.STUDENTID_ERROR2.getMsg());
        Map<String,Object> resultmap=new HashMap<>();
        for(String key: map.keySet()){
          resultmap.put("post_"+key,"post_"+map.get(key));
        }
        JSONObject json=new JSONObject(resultmap);
        String s=json.toString();
        return ResultUtil.success(s);
    }

    @PostMapping(value="/posttest2")
    public Result postTest2(String name,String sex,String studentId){
        if(!sex.equals("男")&&!sex.equals("女"))
            return ResultUtil.error(ResultEnum.SEX_ERROR.getCode(),ResultEnum.SEX_ERROR.getMsg());
        if(!checkInt(studentId))
            return ResultUtil.error(ResultEnum.STUDENTID_ERROR1.getCode(),ResultEnum.STUDENTID_ERROR1.getMsg());
        if(studentId.length()!=7)
            return ResultUtil.error(ResultEnum.STUDENTID_ERROR2.getCode(),ResultEnum.STUDENTID_ERROR2.getMsg());
        return ResultUtil.success("姓名:"+name+" 性别:"+sex+" 学号:"+studentId);
    }

    public boolean checkInt(String s){
        try{
            int a=Integer.parseInt(s);
            if(a<=0) return false;
            return true;
        }
        catch (NumberFormatException e){
            return false;
        }
    }
}
