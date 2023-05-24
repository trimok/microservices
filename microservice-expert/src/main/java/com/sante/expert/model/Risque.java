package com.sante.expert.model;

/**
 * @author trimok
 *
 *         Risque sous forme d'enum
 *
 */
public enum Risque {
    AUCUN_RISQUE("AUCUN RISQUE"),
    RISQUE_LIMITE("RISQUE LIMITE"),
    DANGER("DANGER"),
    APPARITION_PRECOCE("APPARITION PRECOCE");

    /**
     * la version textuelle du risque
     */
    private String detail;

    Risque(String detail) {
	this.detail = detail;
    }

    public String getDetail() {
	return detail;
    }
}
