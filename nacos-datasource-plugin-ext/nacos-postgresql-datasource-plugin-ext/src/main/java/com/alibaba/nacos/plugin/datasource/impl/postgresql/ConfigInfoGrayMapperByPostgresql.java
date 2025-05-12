package com.alibaba.nacos.plugin.datasource.impl.postgresql;

import com.alibaba.nacos.plugin.datasource.constants.DatabaseTypeConstant;
import com.alibaba.nacos.plugin.datasource.impl.mysql.ConfigInfoGrayMapperByMySql;

/**
 * @author van
 * @date 2025/5/12 11:26
 * @description TODO
 */
public class ConfigInfoGrayMapperByPostgresql extends ConfigInfoGrayMapperByMySql {
    @Override
    public String getDataSource() {
        return DatabaseTypeConstant.POSTGRESQL;
    }

}
