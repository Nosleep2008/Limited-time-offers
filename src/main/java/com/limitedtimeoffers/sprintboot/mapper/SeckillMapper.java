package com.limitedtimeoffers.sprintboot.mapper;

import com.limitedtimeoffers.sprintboot.entity.Seckill;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

//import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Mapper
public interface SeckillMapper {
    /*
    @return return sql updated records number, if >= 1 means success update database
     */
    List<Seckill> findAll();


    //Seckill findId(long id);

    int reduceStock(@Param ("seckillId") long seckillId, @Param("killTime") Date killTime);


    Seckill findById(long id);
}
