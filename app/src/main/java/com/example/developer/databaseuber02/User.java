package com.example.developer.databaseuber02;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Developer on 17/2/2017.
 */
public class User {
    private Long number;
    private String name;
    public String hola;
    private String defaultName="default";
    public User(Long number,String name){
        this.number=number;
        this.name=name;
    }
    public User(){
        //DefaultConstructor MacArt

    }



    public Long getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(Long number) {
        this.number = number;
    }
    public Map<String,Object> toMap(){
        HashMap<String,Object> result=new HashMap<>();
        result.put("name",name);
        result.put("number",number);
        return result;
    }

}
