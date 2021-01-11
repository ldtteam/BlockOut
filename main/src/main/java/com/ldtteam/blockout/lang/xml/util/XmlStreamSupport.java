package com.ldtteam.blockout.lang.xml.util;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class XmlStreamSupport
{

    public static Stream<Node> streamNodeList(final NodeList list)
    {
        return IntStream.range(0, list.getLength()).mapToObj(list::item);
    }
}
