package com.limitedtimeoffers.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BaseController {
    @RequestMapping("/")
    public String seckillGoods(){

        return "redirect:/seckill/list";
    }
}
