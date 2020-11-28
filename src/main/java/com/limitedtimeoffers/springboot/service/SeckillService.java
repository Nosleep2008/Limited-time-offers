package com.limitedtimeoffers.springboot.service;

import com.limitedtimeoffers.springboot.dto.Exposer;
import com.limitedtimeoffers.springboot.dto.SeckillExecution;
import com.limitedtimeoffers.springboot.entity.Seckill;
import com.limitedtimeoffers.springboot.exception.RepeatKillException;
import com.limitedtimeoffers.springboot.exception.SeckillCloseException;
import com.limitedtimeoffers.springboot.exception.SeckillException;

import java.math.BigDecimal;
import java.util.List;

public interface SeckillService {
    // return all items
    List<Seckill> findAll();

    // return the item by id
    Seckill findById(long seckillId);

    // get url of limited offer items
    Exposer exportSeckillUrl(long seckillId);

    //
    SeckillExecution executeSeckill(long seckillId, BigDecimal money, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException;

}
