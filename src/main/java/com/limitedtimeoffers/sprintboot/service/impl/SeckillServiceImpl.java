package com.limitedtimeoffers.sprintboot.service.impl;

import com.limitedtimeoffers.sprintboot.dto.Exposer;
import com.limitedtimeoffers.sprintboot.dto.SeckillExecution;
import com.limitedtimeoffers.sprintboot.entity.Seckill;
import com.limitedtimeoffers.sprintboot.entity.SeckillOrder;
import com.limitedtimeoffers.sprintboot.enums.SeckillStatEnum;
import com.limitedtimeoffers.sprintboot.exception.RepeatKillException;
import com.limitedtimeoffers.sprintboot.exception.SeckillCloseException;
import com.limitedtimeoffers.sprintboot.exception.SeckillException;
import com.limitedtimeoffers.sprintboot.mapper.SeckillMapper;
import com.limitedtimeoffers.sprintboot.mapper.SeckillOrderMapper;
import com.limitedtimeoffers.sprintboot.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class SeckillServiceImpl implements SeckillService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    // salt words for enhance security of password
    private final String salt = "j$gijw#2mdk2=85";
    private final String key = "seckill";

    @Autowired
    private SeckillMapper seckillMapper;

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<Seckill> findAll() {
        List<Seckill> seckillList = redisTemplate.boundHashOps("seckill").values();
        if(seckillList == null || seckillList.size() == 0){
            // query redis fails
            seckillList = seckillMapper.findAll();
            for(Seckill seckill: seckillList){
                redisTemplate.boundHashOps(key).put(seckill.getSeckillId(),seckill);
                logger.info("findAll-> reading data from database to redis cache");
            }
        }
        else{
            logger.info("findAll -> read data from redis");
        }
        return seckillList;
    }

    @Override
    public Seckill findById(long seckillId) {
        return seckillMapper.findById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = (Seckill) redisTemplate.boundHashOps(key).get(seckillId);
        if(seckill == null){
            // read from redis failed
            //query database
            seckill = seckillMapper.findById(seckillId);
            if(seckill == null){
                return new Exposer(false,seckillId);
            }
            else{
                redisTemplate.boundHashOps(key).put(seckill.getSeckillId(),seckill);
                logger.info("RedisTemplate -> read from database");
            }
        }
        else{
            logger.info("RedisTemplate -> read from cache");
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if(nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()){
            return new Exposer(false,seckillId);
        }
        //convert string
        String md5 = getMD5(seckillId);
        return new Exposer(true,md5,seckillId);

    }
    private String getMD5(long seckillId){
        String base = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
    @Transactional
    @Override
    public SeckillExecution executeSeckill(long seckillId, BigDecimal money, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if(md5 == null || !md5.equals(getMD5(seckillId))){
            throw new SeckillCloseException("seckill data rewrite");

        }
        // execute order 1,reduce quantity of items 2, store order
        Date nowTime = new Date();
        try{
            int insertCount = seckillOrderMapper.insertOrder(seckillId,money,userPhone);
            if(insertCount<=0){
                // repeat make order
                throw new RepeatKillException("repeat make order");
            }else{
                //success
                int updateCount = seckillMapper.reduceStock(seckillId, nowTime);
                if (updateCount <= 0) {
                    //update failed
                    throw new SeckillCloseException("seckill is closed");
                } else {
                    //success ordered
                    SeckillOrder seckillOrder = seckillOrderMapper.findById(seckillId, userPhone);
                    //update redis
                    Seckill seckill = (Seckill) redisTemplate.boundHashOps(key).get(seckillId);
                    seckill.setStockCount(seckill.getSeckillId() - 1);
                    redisTemplate.boundHashOps(key).put(seckillId, seckill);

                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, seckillOrder);
                }

            }

        }catch(SeckillCloseException e){
            throw e;
        }catch (RepeatKillException e){
            throw e;
        }catch (Exception e){
            logger.info(e.getMessage(),e);
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }

    }
}
