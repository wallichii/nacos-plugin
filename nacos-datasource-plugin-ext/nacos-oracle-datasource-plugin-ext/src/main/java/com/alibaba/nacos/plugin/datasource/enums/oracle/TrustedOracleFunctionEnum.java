package com.alibaba.nacos.plugin.datasource.enums.oracle;

import com.alibaba.nacos.plugin.datasource.enums.mysql.TrustedMysqlFunctionEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @author van
 * @date 2025/5/12 10:53
 * @description TODO
 */
public enum TrustedOracleFunctionEnum {
    NOW("sysdate", "SYSDATE");

    private static final Map<String, TrustedOracleFunctionEnum> LOOKUP_MAP = new HashMap();
    private final String functionName;
    private final String function;

    private TrustedOracleFunctionEnum(String functionName, String function) {
        this.functionName = functionName;
        this.function = function;
    }

    public static String getFunctionByName(String functionName) {
        TrustedOracleFunctionEnum entry = (TrustedOracleFunctionEnum)LOOKUP_MAP.get(functionName);
        if (entry != null) {
            return entry.function;
        } else {
            throw new IllegalArgumentException(String.format("Invalid function name: %s", functionName));
        }
    }

    static {
        for(TrustedOracleFunctionEnum entry : values()) {
            LOOKUP_MAP.put(entry.functionName, entry);
        }

    }
}
