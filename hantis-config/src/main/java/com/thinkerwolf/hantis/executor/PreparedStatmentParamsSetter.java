package com.thinkerwolf.hantis.executor;

import com.thinkerwolf.hantis.common.Param;
import com.thinkerwolf.hantis.type.JDBCType;
import com.thinkerwolf.hantis.type.TypeHandler;
import com.thinkerwolf.hantis.type.TypeHandlerRegistry;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class PreparedStatmentParamsSetter {

    private TypeHandlerRegistry registry;

    public void setParams(PreparedStatement ps, List<Param> params) throws SQLException {

        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                Param param = params.get(i);
                TypeHandler<?> handler;
                if (param.getType() != JDBCType.UNKONWN) {
                    handler = registry.getHandler(param.getType());
                } else {
                    handler = registry.getHandler(param.getValue().getClass());
                }
                handler.setParameter(ps, i + 1, param.getValue(), param.getType());
            }
        }


    }


}
