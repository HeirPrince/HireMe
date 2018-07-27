package com.nassaty.hireme.listeners;

public interface updateListener {
    void isUpdating(Boolean updating);
    void isDone(Boolean done);
    void isFailed(Boolean failed);
}
