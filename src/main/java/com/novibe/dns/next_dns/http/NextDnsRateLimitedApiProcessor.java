package com.novibe.dns.next_dns.http;

import com.novibe.common.exception.DnsHttpError;
import com.novibe.common.util.Log;
import com.novibe.dns.next_dns.http.dto.response.NextDnsResponse;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.function.Function;

import static java.util.Optional.ofNullable;

@UtilityClass
public class NextDnsRateLimitedApiProcessor {


    @SneakyThrows
    public <D, R extends NextDnsResponse<?>> void callApi(List<D> requestList, Function<D, R> request) {
        int waitSeconds = 60;
        Queue<D> requestQueue = new ArrayDeque<>(requestList);
        int successCounter = 0;
        int waveCounter = 0;
        while (!requestQueue.isEmpty()) {
            D requestDto = requestQueue.poll();
            try {
                R response = request.apply(requestDto);
                if (ofNullable(response).map(r -> r.getErrors()).isPresent()) {
                    Log.fail("Failed request: " + response.getErrors());
                } else {
                    Log.progress("Current success progress: " + ++successCounter + "/" + requestList.size());
                    waveCounter++;
                }
                Thread.sleep(1000);
            } catch (DnsHttpError e) {
                if (e.getCode() == 524 || e.getCode() == 429) {
                    requestQueue.add(requestDto);
                    Log.common("Sending speed: %s requests per second"
                            .formatted((double) waveCounter / 60));
                    Log.common("Code %s. Api rate limit has reached".formatted(e.getCode()));
                    runResetWaitTimer(waitSeconds);
                    Log.io("Continue...");
                    waveCounter = 0;
                } else {
                    Log.fail(e.toString());
                    System.exit(1);
                }
            }
        }
        Log.common("\nCompleted");
    }

    @SneakyThrows
    private void runResetWaitTimer(int seconds) {
        for (int timer = seconds; timer > 0; timer--) {
            Thread.sleep(Duration.of(1, ChronoUnit.SECONDS));
            Log.progress("Waiting for reset: " + timer + " seconds");
        }
    }

}
