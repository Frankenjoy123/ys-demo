package com.yunsoo.third.dto.wechat;

import com.yunsoo.common.util.HashUtils;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.TreeMap;

/**
 * Created by yan on 11/9/2016.
 */
public class WeChatData {
    //采用排序的Dictionary的好处是方便对数据包进行签名，不用再签名之前再做一次排序
    private TreeMap<String, Object> paramsMap = new TreeMap<String, Object>();

    /**
     * 设置某个字段的值
     * @param key 字段名
     * @param value 字段值
     */
    public void setValue(String key, Object value)
    {
        paramsMap.put(key, value);
    }

    /**
     * 根据字段名获取某个字段的值
     * @param key 字段名
     * @return key对应的字段值
     */
    public Object getValue(String key)
    {
        return paramsMap.get(key);
    }

    /**
     * @将Map转成xml
     * @return 经转换得到的xml串
     **/
    public String toXml() throws UnsupportedEncodingException {
        //数据为空时不能转化为xml格式
        if (0 == paramsMap.size())
            return "";

        StringBuilder xml = new StringBuilder("<xml>");
        paramsMap.forEach((key, value) ->{
            if(value!=null){
                if (value instanceof Integer){
                    xml.append("<" + key + ">" + value + "</" + key + ">");
                }
                else if(value instanceof String){
                    xml.append("<" + key + ">" + "<![CDATA[" + value + "]]></" + key + ">");
                }
            }
        });
        xml.append("</xml>");
        String xmlStr = xml.toString();
        return xmlStr;
    }

    /**
     * @TreeMap格式转化成url参数格式
     * @ return url格式串, 该串不包含sign字段值
     */
    public String toUrl()
    {
        StringBuilder result = new StringBuilder();

        paramsMap.forEach((key, value) ->{
            if(value != null && StringUtils.hasText(value.toString())){
                if (!key.equals("sign"))
                {
                    result.append(key);
                    result.append("=");
                    result.append(value);
                    result.append("&");
                }
            }
        });

        String returnValue = result.toString();
        return returnValue.substring(0, returnValue.length()-1);
    }

    public void addSign(String privateKey) throws UnsupportedEncodingException {
        String str = toUrl();
        //在string后加入API KEY
        str += "&key=" + privateKey;
        paramsMap.put("sign", HashUtils.md5HexString(str, "UTF-8").toUpperCase());
    }

    /**
     * @获取Map
     */
    public TreeMap<String, Object> getValues()
    {
        return paramsMap;
    }

}
