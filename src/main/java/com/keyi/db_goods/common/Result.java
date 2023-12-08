package com.keyi.db_goods.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private String code;
    private String msg;
    private Object data;

    public static Result success(){
        return new Result(Constans.code_200,"",null);
    }

    public static Result error(){
        return new Result(Constans.code_500,"系统错误",null);
    }

    public static Result error(String code,String msg){
        return new Result(code,msg,null);
    }

    public static <T> Result success(ArrayList<T> newArrayList) {
        return new Result(Constans.code_200,"",newArrayList);
    }

    public static Result success(Map<String, Object> map) {
        return new Result(Constans.code_200,"",map);
    }
}
