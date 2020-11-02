package me.kuku.yuq.utils

import com.IceCreamQAQ.Yu.util.OkHttpWebImpl
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.icecreamqaq.yuq.message.*
import com.icecreamqaq.yuq.message.Message.Companion.toMessage
import com.icecreamqaq.yuq.mif
import com.icecreamqaq.yuq.mirai.MiraiBot
import com.icecreamqaq.yuq.mirai.message.ImageReceive
import com.icecreamqaq.yuq.yuq
import me.kuku.yuq.entity.QQLoginEntity
import java.net.URLEncoder
import kotlin.random.Random

object BotUtils {

    fun shortUrl(url: String): String{
        return OkHttpClientUtils.getStr("http://api.suowo.cn/api.htm?key=5e9a6ed83a005a12b5e62d70@f6bbd71fde6d40e27ecf3a592e13f9ff&url=${URLEncoder.encode(url, "utf-8")}")
    }

    fun randomStr(len: Int): String{
        val str = "abcdefghijklmnopqrstuvwxyz0123456789"
        val sb = StringBuilder()
        for (i in (0 until len))
            sb.append(str[Random.nextInt(0, str.length)])
        return sb.toString()
    }

    fun randomNum(len: Int): String{
        val sb = StringBuilder()
        for (i in (0 until len)){
            sb.append(Random.nextInt(10))
        }
        return sb.toString()
    }

    fun regex(regex: String, text: String): String? {
        val r = Regex(regex)
        val find = r.find(text)
        return find?.value
    }

    fun regex(first: String, last: String , text: String): String? {
        val regex = "(?<=$first).*?(?=$last)"
        return this.regex(regex, text)
    }

    fun messageToJsonArray(rm: Message): JSONArray{
        val body = rm.body
        val aJsonArray = JSONArray()
        loop@ for (messageItem in body){
            val aJsonObject = JSONObject()
            when (messageItem) {
                is Text -> {
                    aJsonObject["type"] = "text"
                    aJsonObject["content"] = messageItem.text
                }
                is ImageReceive -> {
                    aJsonObject["type"] = "image"
                    aJsonObject["content"] = messageItem.url
                }
                is Face -> {
                    aJsonObject["type"] = "face"
                    aJsonObject["content"] = messageItem.faceId
                }
                is At -> {
                    aJsonObject["type"] = "at"
                    aJsonObject["content"] = messageItem.user
                }
                is XmlEx -> {
                    aJsonObject["type"] = "xml"
                    aJsonObject["content"] = messageItem.value
                    aJsonObject["serviceId"] = messageItem.serviceId
                }
                is JsonEx -> {
                    aJsonObject["type"] = "json"
                    aJsonObject["content"] = messageItem.value
                }
                else -> continue@loop
            }
            aJsonArray.add(aJsonObject)
        }
        return aJsonArray
    }

    fun jsonArrayToMessage(jsonArray: JSONArray): Message{
        val msg = "".toMessage()
        for (j in jsonArray.indices){
            val aJsonObject = jsonArray.getJSONObject(j)
            when (aJsonObject.getString("type")){
                "text" -> msg.plus(aJsonObject.getString("content"))
                "image" -> msg.plus(mif.imageByUrl(aJsonObject.getString("content")))
                "face" -> msg.plus(mif.face(aJsonObject.getInteger("content")))
                "at" -> msg.plus(mif.at(aJsonObject.getLong("content")))
                "xml" -> msg.plus(mif.xmlEx(aJsonObject.getInteger("serviceId"), aJsonObject.getString("content")))
                "json" -> msg.plus(mif.jsonEx(aJsonObject.getString("content")))
            }
        }
        return msg
    }

    fun delMonitorList(jsonArray: JSONArray, username: String): JSONArray{
        val list = mutableListOf<JSONObject>()
        jsonArray.forEach {
            val jsonObject = it as JSONObject
            if (jsonObject.getString("name") == username) list.add(jsonObject)
        }
        list.forEach { jsonArray.remove(it) }
        return jsonArray
    }

    fun toQQEntity(web: OkHttpWebImpl, miraiBot: MiraiBot): QQLoginEntity{
        val concurrentHashMap = web.domainMap
        val qunMap = concurrentHashMap.getValue("qun.qq.com")
        val groupPsKey = qunMap.getValue("p_skey").value
        val qqMap = concurrentHashMap.getValue("qq.com")
        val sKey = qqMap.getValue("skey").value
        val qZoneMap = concurrentHashMap.getValue("qzone.qq.com")
        val psKey = qZoneMap.getValue("p_skey").value
        return QQLoginEntity(null, yuq.botId, 0L, "", sKey, psKey, groupPsKey, miraiBot.superKey, QQUtils.getToken(miraiBot.superKey).toString())
    }

    fun delManager(jsonArray: JSONArray, content: String): JSONArray{
        for (i in jsonArray.indices){
            val str = jsonArray.getString(i)
            if (str == content) {
                jsonArray.remove(str)
                break
            }
        }
        return jsonArray
    }
}