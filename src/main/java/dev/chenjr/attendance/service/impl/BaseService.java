package dev.chenjr.attendance.service.impl;

import dev.chenjr.attendance.service.IService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseService implements IService {
    private final Logger logger;

    public BaseService() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }


    @Override
    public Logger getLogger() {
        return logger;
    }
}
