package com.interviewinsights.interviewinsights.dto.gemini.response;

import java.util.List;

public class ExtractedQuestionsResponse {

    private List<String> coding;
    private List<String> technical;
    private List<String> hr;
    private List<String> aptitude;
    private List<String> gd;

    public List<String> getCoding() {
        return coding;
    }

    public void setCoding(List<String> coding) {
        this.coding = coding;
    }

    public List<String> getTechnical() {
        return technical;
    }

    public void setTechnical(List<String> technical) {
        this.technical = technical;
    }

    public List<String> getHr() {
        return hr;
    }

    public void setHr(List<String> hr) {
        this.hr = hr;
    }

    public List<String> getAptitude() {
        return aptitude;
    }

    public void setAptitude(List<String> aptitude) {
        this.aptitude = aptitude;
    }

    public List<String> getGd() {
        return gd;
    }

    public void setGd(List<String> gd) {
        this.gd = gd;
    }
}