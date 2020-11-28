package com.limitedtimeoffers.springboot.controller;

import org.springframework.stereotype.Controller;

@Controller
public class BaseController {
    public String seckillGoods(){
        return "redirect:/seckill/list";
    }
}
