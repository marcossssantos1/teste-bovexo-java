package com.agro.feedmanagement.exception;

public class FeedRecordNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	public FeedRecordNotFoundException(String message) {
        super(message);
    }
}