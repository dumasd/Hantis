package com.thinkerwolf.hantis.sql.xml;

import com.thinkerwolf.hantis.common.util.ClassUtils;
import com.thinkerwolf.hantis.common.util.StringUtils;
import com.thinkerwolf.hantis.sql.SelectSqlNode;
import com.thinkerwolf.hantis.sql.SqlNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Map;

public class SelectNodeHandler extends AbstractNodeHandler {

    @Override
    protected SqlNode newSqlNode(Node node) {
        Element el = (Element) node;
        String pType = el.getAttribute("parameterType");
        String rType = el.getAttribute("returnType");

        Class<?> pt = Object.class;
        if (StringUtils.isNotEmpty(pType)) {
            pt = ClassUtils.forName(pType);
        }

        Class<?> rt = Map.class;
        if (StringUtils.isNotEmpty(rType)) {
            rt = ClassUtils.forName(rType);
        }

        return new SelectSqlNode(pt, rt);
    }

}
