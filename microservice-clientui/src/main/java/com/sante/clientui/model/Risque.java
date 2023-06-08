package com.sante.clientui.model;

/**
 * @author trimok
 *
 *         Enum Risque
 */
public enum Risque {
    /**
     * AUCUN_RISQUE
     */
    AUCUN_RISQUE("AUCUN RISQUE"),
    /**
     * RISQUE_LIMITE
     */
    RISQUE_LIMITE("RISQUE LIMITE"),
    /**
     * DANGER
     */
    DANGER("DANGER"),
    /**
     * APPARITION
     */
    APPARITION_PRECOCE("APPARITION PRECOCE");

    /**
     * String expression of the risk
     */
    private String detail;

    Risque(String detail) {
	this.detail = detail;
    }

    public String getDetail() {
	return detail;
    }
}
