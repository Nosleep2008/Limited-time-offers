package com.limitedtimeoffers.sprintboot.controller;

import com.limitedtimeoffers.sprintboot.dto.SeckillExecution;
import com.limitedtimeoffers.sprintboot.dto.SeckillResult;
import com.limitedtimeoffers.sprintboot.entity.Seckill;
import com.limitedtimeoffers.sprintboot.enums.SeckillStatEnum;
import com.limitedtimeoffers.sprintboot.exception.RepeatKillException;
import com.limitedtimeoffers.sprintboot.exception.SeckillCloseException;
import com.limitedtimeoffers.sprintboot.exception.SeckillException;
import com.limitedtimeoffers.sprintboot.service.SeckillService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Controller
public class SeckillController {
    @Autowired
    private SeckillService seckillService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/list")
    public String findSeckillList(Model model){
        List<Seckill> list = seckillService.findAll();
        model.addAttribute("list",list);
        return "page/seckill";
    }

    @ResponseBody
    @RequestMapping("/findById")
    public Seckill findById(@RequestParam("id") Long id){
        return seckillService.findById(id);
    }
    @RequestMapping("/{seckillId}/detail")
    public String detail(@PathVariable("seckillId") Long seckillId, Model model){
        if(seckillId == null){
            return "page/seckill";
        }
        Seckill seckill = seckillService.findById(seckillId);
        model.addAttribute("seckill",seckill);
        if(seckill == null){
            return "page/seckill";
        }
        return "page/seckill_detail";

    }

    @ResponseBody
    @RequestMapping(value = "/{seckillId}/exposer",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @RequestParam("money") BigDecimal money,
                                                   @CookieValue(value = "killPhone", required = false) Long userPhone){
        if(userPhone == null){
            return new SeckillResult<SeckillExecution>(false,"please sign up");
        }
        try{
            SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId,money,userPhone,md5);
            return new SeckillResult<>(true,seckillExecution);

        }catch(RepeatKillException e) {
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
            return new SeckillResult<>(true, seckillExecution);
        }catch(SeckillCloseException e){
            SeckillExecution seckillExecution = new SeckillExecution(seckillId,SeckillStatEnum.END);
            return new SeckillResult<>(true,seckillExecution);
        }catch(SeckillException e){
            SeckillExecution seckillExecution = new SeckillExecution(seckillId,SeckillStatEnum.INNER_ERROR);
            return new SeckillResult<>(true,seckillExecution);
        }


    }
    @ResponseBody
    @GetMapping(value = "/time/now")
    public SeckillResult<Long> time() {
        Date now = new Date();
        return new SeckillResult(true, now.getTime());
    }



}
