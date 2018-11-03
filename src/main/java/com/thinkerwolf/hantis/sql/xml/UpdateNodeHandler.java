package com.thinkerwolf.hantis.sql.xml;

import com.thinkerwolf.hantis.common.util.ClassUtils;
import com.thinkerwolf.hantis.common.util.StringUtils;
import com.thinkerwolf.hantis.sql.SqlNode;
import com.thinkerwolf.hantis.sql.UpdateSqlNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class UpdateNodeHandler extends AbstractNodeHandler {

    @Override
    protected SqlNode newSqlNode(Node node) {
        Element el = (Element) node;
        String pType = el.getAttribute("parameterType");
        Class<?> pt = Object.class;
        if (StringUtils.isNotEmpty(pType)) {
            pt = ClassUtils.forName(pType);
        }
        return new UpdateSqlNode(pt);
    }

}
