package com.novibe.common.data_sources;

import com.novibe.common.base_structures.HostsLine;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class HostsBlockListsLoader extends ListLoader<String> {

    private static final String[] BLOCK_IPS = {"0.0.0.0", "127.0.0.1", "::1"};
    private static final String[] LOCALHOST_NAME = {"localhost", "ip6-localhost"};

    @Override
    protected String listType() {
        return "Block";
    }

    @Override
    protected Predicate<HostsLine> filterRelatedLines() {
        return line -> isBlockIp(line.ip()) && !isLocalhost(line.domain());
    }

    @Override
    protected String toObject(HostsLine line) {
        return line.domain();
    }

    static boolean isBlockIp(String ip) {
        for (String blockIp : BLOCK_IPS) {
            if (blockIp.equals(ip)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLocalhost(String domain) {
        if (domain == null) return false;
        for (String localhost : LOCALHOST_NAME) {
            if (domain.equals(localhost))
                return true;
        }
        return false;
    }

}
