package com.thinkerwolf.hantis.sql.xml;

import com.thinkerwolf.hantis.sql.SqlNode;
import org.w3c.dom.Node;

public interface NodeHandler {


    SqlNode parse(Node el);

}
