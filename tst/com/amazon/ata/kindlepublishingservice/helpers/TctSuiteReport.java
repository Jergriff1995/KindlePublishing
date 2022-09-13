package com.amazon.ata.kindlepublishingservice.helpers;

import java.util.List;

public class TctSuiteReport {

    private String tctSuiteId;
    private List<TctResult> tctResultList;

    public TctSuiteReport(String tctSuiteId, List<TctResult> tctResultList) {
        this.tctSuiteId = tctSuiteId;
        this.tctResultList = tctResultList;
    }

    public String getTctSuiteId() {
        return tctSuiteId;
    }

    public void setTctSuiteId(String tctSuiteId) {
        this.tctSuiteId = tctSuiteId;
    }

    public List<TctResult> getTctResultList() {
        return tctResultList;
    }

    public void setTctResultList(List<TctResult> tctResultList) {
        this.tctResultList = tctResultList;
    }
}
