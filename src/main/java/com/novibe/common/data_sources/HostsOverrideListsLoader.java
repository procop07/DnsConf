package com.novibe.common.data_sources;

import com.novibe.common.base_structures.HostsLine;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;

import static java.util.Objects.nonNull;

@Service
public class HostsOverrideListsLoader extends ListLoader<HostsOverrideListsLoader.BypassRoute> {

    public record BypassRoute(String ip, String website) {
    }

    @Override
    protected String listType() {
        return "Override";
    }

    @Override
    protected Predicate<HostsLine> filterRelatedLines() {
        return line -> !HostsBlockListsLoader.isBlockIp(line.ip()) && nonNull(line.domain()) && !HostsBlockListsLoader.isLocalhost(line.domain());

    }

    @Override
    protected BypassRoute toObject(HostsLine line) {
        return new BypassRoute(line.ip(), line.domain());

    }

}
