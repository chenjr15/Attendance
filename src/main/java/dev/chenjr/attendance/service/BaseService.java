package dev.chenjr.attendance.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseService {
    private final Logger logger;

    public BaseService() {
        this.logger= LoggerFactory.getLogger(this.getClass());
    }


    protected Logger getLogger() {
        return logger;
    }
}
