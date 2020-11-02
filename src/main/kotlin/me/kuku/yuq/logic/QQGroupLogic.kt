package me.kuku.yuq.logic

import com.IceCreamQAQ.Yu.annotation.AutoBind
import me.kuku.yuq.pojo.CommonResult
import me.kuku.yuq.pojo.GroupMember

@AutoBind
interface QQGroupLogic {
    fun addGroupMember(qq: Long, group: Long): String
    fun setGroupAdmin(qq: Long, group: Long, isAdmin: Boolean): String
    fun setGroupCard(qq: Long, group: Long, name: String): String
    fun deleteGroupMember(qq: Long, group: Long, isFlag: Boolean): String
    fun addHomeWork(group: Long, courseName: String, title: String, content: String, needFeedback: Boolean): String
    fun groupCharin(group: Long, content: String, time: Long): String
    fun groupLevel(group: Long): CommonResult<List<Map<String, String>>>
    fun queryMemberInfo(group: Long, qq: Long): CommonResult<GroupMember>
    fun essenceMessage(group: Long): CommonResult<List<String>>
    fun queryGroup(): CommonResult<List<Long>>
    fun groupHonor(group: Long, type: String): List<Map<String, String>>
    fun groupSign(group: Long, place: String, text: String, name: String, picId: String?, picUrl: String?): CommonResult<String>
}