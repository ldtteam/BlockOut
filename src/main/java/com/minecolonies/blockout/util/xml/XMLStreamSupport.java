package com.minecolonies.blockout.util.xml;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class XMLStreamSupport
{

    private XMLStreamSupport()
    {
        throw new IllegalArgumentException("Utility Class");
    }

    public static Stream<Node> streamChildren(@NotNull final Node node)
    {
        if (!node.hasChildNodes())
        {
            return (new ArrayList<Node>()).stream();
        }

        return stream(node.getChildNodes());
    }

    public static Stream<Node> stream(@NotNull final NodeList nodeList)
    {
        return StreamSupport.stream(new NodeListSpliterator(nodeList), false);
    }

    private static class NodeListSpliterator implements Spliterator<Node>
    {
        @NotNull
        private final NodeList nodeList;

        private int index = 0;

        public NodeListSpliterator(@NotNull final NodeList nodeList)
        {
            this.nodeList = nodeList;
        }

        /**
         * If a remaining element exists, performs the given action on it,
         * returning {@code true}; else returns {@code false}.  If this
         * Spliterator is {@link #ORDERED} the action is performed on the
         * next element in encounter order.  Exceptions thrown by the
         * action are relayed to the caller.
         *
         * @param action The action
         * @return {@code false} if no remaining elements existed
         * upon entry to this method, else {@code true}.
         *
         * @throws NullPointerException if the specified action is null
         */
        @Override
        public boolean tryAdvance(final Consumer<? super Node> action)
        {
            if (index < nodeList.getLength())
            {
                action.accept(nodeList.item(index++));
            }

            return !(index >= nodeList.getLength());
        }

        /**
         * If this spliterator can be partitioned, returns a Spliterator
         * covering elements, that will, upon return from this method, not
         * be covered by this Spliterator.
         * <p>
         * <p>If this Spliterator is {@link #ORDERED}, the returned Spliterator
         * must cover a strict prefix of the elements.
         * <p>
         * <p>Unless this Spliterator covers an infinite number of elements,
         * repeated calls to {@code trySplit()} must eventually return {@code null}.
         * Upon non-null return:
         * <ul>
         * <li>the value reported for {@code estimateSize()} before splitting,
         * must, after splitting, be greater than or equal to {@code estimateSize()}
         * for this and the returned Spliterator; and</li>
         * <li>if this Spliterator is {@code SUBSIZED}, then {@code estimateSize()}
         * for this spliterator before splitting must be equal to the sum of
         * {@code estimateSize()} for this and the returned Spliterator after
         * splitting.</li>
         * </ul>
         * <p>
         * <p>This method may return {@code null} for any reason,
         * including emptiness, inability to split after traversal has
         * commenced, data structure constraints, and efficiency
         * considerations.
         *
         * @return a {@code Spliterator} covering some portion of the
         * elements, or {@code null} if this spliterator cannot be split
         */
        @Override
        public Spliterator<Node> trySplit()
        {
            return null;
        }

        /**
         * Returns an estimate of the number of elements that would be
         * encountered by a {@link #forEachRemaining} traversal, or returns {@link
         * Long#MAX_VALUE} if infinite, unknown, or too expensive to compute.
         * <p>
         * <p>If this Spliterator is {@link #SIZED} and has not yet been partially
         * traversed or split, or this Spliterator is {@link #SUBSIZED} and has
         * not yet been partially traversed, this estimate must be an accurate
         * count of elements that would be encountered by a complete traversal.
         * Otherwise, this estimate may be arbitrarily inaccurate, but must decrease
         * as specified across invocations of {@link #trySplit}.
         *
         * @return the estimated size, or {@code Long.MAX_VALUE} if infinite,
         * unknown, or too expensive to compute.
         */
        @Override
        public long estimateSize()
        {
            return nodeList.getLength() - (index + 1);
        }

        /**
         * Returns a set of characteristics of this Spliterator and its
         * elements. The result is represented as ORed values from {@link
         * #ORDERED}, {@link #DISTINCT}, {@link #SORTED}, {@link #SIZED},
         * {@link #NONNULL}, {@link #IMMUTABLE}, {@link #CONCURRENT},
         * {@link #SUBSIZED}.  Repeated calls to {@code characteristics()} on
         * a given spliterator, prior to or in-between calls to {@code trySplit},
         * should always return the same result.
         * <p>
         * <p>If a Spliterator reports an inconsistent set of
         * characteristics (either those returned from a single invocation
         * or across multiple invocations), no guarantees can be made
         * about any computation using this Spliterator.
         *
         * @return a representation of characteristics
         */
        @Override
        public int characteristics()
        {
            return Spliterator.ORDERED | Spliterator.SORTED | Spliterator.NONNULL | Spliterator.IMMUTABLE | Spliterator.DISTINCT;
        }
    }
}