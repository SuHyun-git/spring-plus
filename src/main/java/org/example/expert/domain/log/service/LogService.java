package org.example.expert.domain.log.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.log.entity.Log;
import org.example.expert.domain.log.repository.LogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    // transaction을 분리하여 log는 먼저 저장되도록 하였습니다.
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveLog(LocalDateTime now, String menegerSave) {
        Log log = new Log(now, menegerSave);
        logRepository.save(log);
    }
}
