package com.cs.crm.commons.utils;

import cn.hutool.core.lang.UUID;

public class UUIDUtils {
    public static String getUUID(){
        String uuid = UUID.randomUUID().toString().replace("-","");
        return uuid;
    }
}
