package me.kuku.yuq.service

import com.IceCreamQAQ.Yu.annotation.AutoBind
import me.kuku.yuq.entity.WeiboEntity

@AutoBind
interface WeiboService {
    fun findByQQ(qq: Long): WeiboEntity?

    fun save(weiboEntity: WeiboEntity)

    fun delByQQ(qq: Long): Int

    fun findByMonitor(monitor: Boolean): List<WeiboEntity>

    fun findAll(): List<WeiboEntity>

}