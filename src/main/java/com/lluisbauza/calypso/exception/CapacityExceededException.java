package com.lluisbauza.calypso.exception;

import lombok.Getter;

@Getter
public class CapacityExceededException extends RuntimeException {

    private final Long maxCapacity;

    public CapacityExceededException(Long maxCapacity) {
        super("This boat holds up to " + maxCapacity + " passengers.");
        this.maxCapacity = maxCapacity;
    }
}
