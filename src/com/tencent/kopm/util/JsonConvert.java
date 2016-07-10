package com.tencent.kopm.util;
//Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
//Jad home page: http://www.kpdus.com/jad.html
//Decompiler options: packimports(3) fieldsfirst ansi 
//Source File Name:   JsonConvert.java


import java.util.*;
import net.sf.json.*;

public class JsonConvert
{

    public JsonConvert()
    {
    }

    public static JSONObject message(String message, boolean success)
    {
        Map map = new HashMap();
        map.put("success", Boolean.valueOf(success));
        map.put("message", message);
        return JSONObject.fromObject(map);
    }

    public static JSONObject generate(List list)
    {
        Map map = new HashMap();
        map.put("totalProperty", Integer.valueOf(list.size()));
        map.put("root", list);
        return JSONObject.fromObject(map);
    }

    public static JSONObject javabean2json(Object object, String message, boolean success)
    {
        Map map = new HashMap();
        map.put("success", Boolean.valueOf(success));
        map.put("message", message);
        map.put("data", object);
        return JSONObject.fromObject(map);
    }

    public static JSONObject objectcollect2json(List list, String total)
    {
        Map map = new HashMap();
        map.put("totalProperty", total);
        map.put("root", list);
        return JSONObject.fromObject(map);
    }

    public static JSONArray getJSONArrayFormString(String str)
    {
        if(str == null || str.trim().length() == 0)
            return null;
        JSONArray jsonArray = null;
        try
        {
            jsonArray = JSONArray.fromObject(str);
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public static JSONObject StringToJSONOBject(String str)
    {
        if(str == null || str.trim().length() == 0)
            return null;
        JSONObject jsonObject = null;
        try
        {
            jsonObject = JSONObject.fromObject(str);
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
