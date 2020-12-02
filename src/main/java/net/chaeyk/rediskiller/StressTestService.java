package net.chaeyk.rediskiller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class StressTestService implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${stress.data-count:500}")
    private int dataCount;

    @Value("${stress.thread-count:1000}")
    private int threadCount;

    private final MemberRepository memberRepository;

    private List<Thread> threads = new ArrayList<>();

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        prepareData();
        executeThread();
    }

    private void prepareData() {
        log.info("prepare data");
        if (memberRepository.count() < dataCount) {
            for (long memberId = 0; memberId < dataCount; memberId++) {
                final long newMemberId = memberId;
                memberRepository.findById(memberId)
                        .orElseGet(() -> {
                            MemberEntity member = new MemberEntity();
                            member.setMemberId(newMemberId);
                            member.setNickname("nickname" + newMemberId);
                            return memberRepository.saveAndFlush(member);
                        });
            }
        }
        log.info("prepare done.");
    }

    private void executeThread() {
        Random random = new Random();
        for (int i = 0; i < threadCount; i++) {
            log.info("start stress thread {}", i);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    long memberId = random.nextInt(dataCount);
                    try {
                        while (true) {
                            Optional<MemberEntity> memberOpt = memberRepository.findById(memberId);
                            if (!memberOpt.isPresent()) {
                                log.error("member not found: {}", memberId);
                            }

                            memberId = (memberId + random.nextInt(20)) % dataCount;
                        }
                    } catch (Exception e) {
                        // just stop
                    }
                }
            });
            thread.start();
            threads.add(thread);
        }
    }
}
