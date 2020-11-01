package com.limitedtimeoffers.sprintboot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SeckillOrder implements Serializable {
    private long seckillId;   // items id
    private BigDecimal money;  // payment amount

    private long userPhone;  // user phone number
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+0")
    private Date createTime;
    private boolean status; // order status

    private Seckill seckill;

}
