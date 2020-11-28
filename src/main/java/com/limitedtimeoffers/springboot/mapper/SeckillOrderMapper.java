package com.limitedtimeoffers.springboot.mapper;

import com.limitedtimeoffers.springboot.entity.SeckillOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Mapper
public interface SeckillOrderMapper {

    /**
     *
     *
     * @param seckillId
     * @param money     limited offer price
     * @param userPhone  ordered users
     * @return if >=1 means success
     */

    int insertOrder(@Param("seckillId") long seckillId, @Param("money") BigDecimal money,  @Param("userPhone") long userPhone);

    /**
     * check limit offer item order properties
     *
     * @param seckillId
     * @param userPhone
     * @return
     */
    SeckillOrder findById(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

}
