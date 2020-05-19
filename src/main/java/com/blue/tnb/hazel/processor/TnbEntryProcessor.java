package com.blue.tnb.hazel.processor;

import com.hazelcast.map.AbstractEntryProcessor;

import java.util.List;
import java.util.Map;

public class TnbEntryProcessor extends AbstractEntryProcessor<Long, List<Long>> {
    @Override
    public Object process(Map.Entry<Long, List<Long>> entry) {

        return null;
    }
}
