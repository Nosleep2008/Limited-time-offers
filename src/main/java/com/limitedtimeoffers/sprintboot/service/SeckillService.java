package com.limitedtimeoffers.sprintboot.service;

import com.limitedtimeoffers.sprintboot.dto.Exposer;
import com.limitedtimeoffers.sprintboot.dto.SeckillExecution;
import com.limitedtimeoffers.sprintboot.entity.Seckill;
import com.limitedtimeoffers.sprintboot.exception.RepeatKillException;
import com.limitedtimeoffers.sprintboot.exception.SeckillCloseException;
import com.limitedtimeoffers.sprintboot.exception.SeckillException;

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
