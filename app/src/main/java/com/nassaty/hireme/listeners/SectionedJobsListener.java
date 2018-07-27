package com.nassaty.hireme.listeners;

import com.nassaty.hireme.model.Job;

import java.util.List;

public interface SectionedJobsListener {
    void mine(List<Job> jobs);
    void others(List<Job> jobs);
    void expensive(List<Job> jobs);
}
