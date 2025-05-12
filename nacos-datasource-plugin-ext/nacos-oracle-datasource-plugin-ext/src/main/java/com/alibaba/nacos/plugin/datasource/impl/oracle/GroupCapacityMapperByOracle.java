/*
 * Copyright 1999-2023 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.nacos.plugin.datasource.impl.oracle;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.common.utils.NamespaceUtil;
import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.mapper.GroupCapacityMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/***
 * @author onewe
 */
public class GroupCapacityMapperByOracle extends AbstractOracleMapper
        implements GroupCapacityMapper {

    @Override
    public MapperResult selectGroupInfoBySize(MapperContext context) {
        String sql = getDatabaseDialect().getLimitTopSqlWithMark(
                "SELECT id, group_id FROM group_capacity WHERE id > ?");
        return new MapperResult(sql, CollectionUtils.list(
                context.getWhereParameter(FieldConstant.ID), context.getPageSize()));
    }

    @Override
    public MapperResult select(MapperContext context) {
        String sql = "SELECT id, quota, `usage`, max_size, max_aggr_count, max_aggr_size, group_id FROM " +
                "group_capacity WHERE group_id = ?";
        return new MapperResult(sql, Collections.singletonList(context.getWhereParameter("groupId")));
    }

    public MapperResult insertIntoSelect(MapperContext context) {
        List<Object> paramList = new ArrayList<>();
        paramList.add(context.getUpdateParameter("groupId"));
        paramList.add(context.getUpdateParameter("quota"));
        paramList.add(context.getUpdateParameter("maxSize"));
        paramList.add(context.getUpdateParameter("maxAggrCount"));
        paramList.add(context.getUpdateParameter("maxAggrSize"));
        paramList.add(context.getUpdateParameter("gmtCreate"));
        paramList.add(context.getUpdateParameter("gmtModified"));
        String sql = "INSERT INTO group_capacity (group_id, quota, `usage`, max_size, max_aggr_count, max_aggr_size," +
                "gmt_create, gmt_modified) SELECT ?, ?, count(*), ?, ?, ?, ?, ? FROM config_info";
        return new MapperResult(sql, paramList);
    }

    public MapperResult insertIntoSelectByWhere(MapperContext context) {
        String sql = "INSERT INTO group_capacity (group_id, quota, `usage`, max_size, max_aggr_count, max_aggr_size, " +
                "gmt_create, gmt_modified) SELECT ?, ?, count(*), ?, ?, ?, ?, ? FROM config_info WHERE group_id=? AND" +
                " tenant_id = '" + NamespaceUtil.getNamespaceDefaultId() + "'";
        List<Object> paramList = new ArrayList<>();
        paramList.add(context.getUpdateParameter("groupId"));
        paramList.add(context.getUpdateParameter("quota"));
        paramList.add(context.getUpdateParameter("maxSize"));
        paramList.add(context.getUpdateParameter("maxAggrCount"));
        paramList.add(context.getUpdateParameter("maxAggrSize"));
        paramList.add(context.getUpdateParameter("gmtCreate"));
        paramList.add(context.getUpdateParameter("gmtModified"));
        paramList.add(context.getWhereParameter("groupId"));
        return new MapperResult(sql, paramList);
    }

    public MapperResult incrementUsageByWhereQuotaEqualZero(MapperContext context) {
        return new MapperResult("UPDATE group_capacity SET `usage` = `usage` + 1, gmt_modified = ? WHERE group_id = ?" +
                " AND `usage` < ? AND quota = 0", CollectionUtils.list(new Object[]{context.getUpdateParameter(
                "gmtModified"), context.getWhereParameter("groupId"), context.getWhereParameter("usage")}));
    }

    public MapperResult incrementUsageByWhereQuotaNotEqualZero(MapperContext context) {
        return new MapperResult("UPDATE group_capacity SET `usage` = `usage` + 1, gmt_modified = ? WHERE group_id = ?" +
                " AND `usage` < quota AND quota != 0", CollectionUtils.list(new Object[]{context.getUpdateParameter(
                "gmtModified"), context.getWhereParameter("groupId")}));
    }

    public MapperResult incrementUsageByWhere(MapperContext context) {
        return new MapperResult("UPDATE group_capacity SET `usage` = `usage` + 1, gmt_modified = ? WHERE group_id = " +
                "?", CollectionUtils.list(new Object[]{context.getUpdateParameter("gmtModified"),
                context.getWhereParameter("groupId")}));
    }

    public MapperResult decrementUsageByWhere(MapperContext context) {
        return new MapperResult("UPDATE group_capacity SET `usage` = `usage` - 1, gmt_modified = ? WHERE group_id = ?" +
                " AND `usage` > 0", CollectionUtils.list(new Object[]{context.getUpdateParameter("gmtModified"),
                context.getWhereParameter("groupId")}));
    }

    public MapperResult updateUsage(MapperContext context) {
        return new MapperResult("UPDATE group_capacity SET `usage` = (SELECT count(*) FROM config_info), gmt_modified" +
                " = ? WHERE group_id = ?",
                CollectionUtils.list(new Object[]{context.getUpdateParameter("gmtModified"),
                        context.getWhereParameter("groupId")}));
    }

    public MapperResult updateUsageByWhere(MapperContext context) {
        return new MapperResult("UPDATE group_capacity SET `usage` = (SELECT count(*) FROM config_info WHERE " +
                "group_id=? AND tenant_id = '" + NamespaceUtil.getNamespaceDefaultId() + "'), gmt_modified = ? WHERE " +
                "group_id= ?", CollectionUtils.list(new Object[]{context.getWhereParameter("groupId"),
                context.getUpdateParameter("gmtModified"), context.getWhereParameter("groupId")}));
    }
}
