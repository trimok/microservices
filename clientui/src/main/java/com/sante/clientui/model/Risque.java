package com.sante.clientui.model;

public enum Risque {
    AUCUN_RISQUE("AUCUN RISQUE"),
    RISQUE_LIMITE("RISQUE LIMITE"),
    DANGER("DANGER"),
    APPARITION_PRECOCE("APPARITION PRECOCE");

    private String detail;

    Risque(String detail) {
	this.detail = detail;
    }

    public String getDetail() {
	return detail;
    }
}
