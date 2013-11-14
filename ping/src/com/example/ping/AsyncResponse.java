package com.example.ping;

public interface AsyncResponse {
    void processFinish(Long output);
    void processUppdate(Long output);
}