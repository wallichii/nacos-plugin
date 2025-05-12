package com.alibaba.nacos.plugin.datasource.impl.postgresql;

import com.alibaba.nacos.plugin.datasource.constants.DatabaseTypeConstant;
import com.alibaba.nacos.plugin.datasource.impl.mysql.ConfigMigrateMapperByMysql;

/**
 * @author van
 * @date 2025/5/12 11:28
 * @description TODO
 */
public class ConfigMigrateMapperByPostgresql extends ConfigMigrateMapperByMysql {
    @Override
    public String getDataSource() {
        return DatabaseTypeConstant.POSTGRESQL;
    }
}
