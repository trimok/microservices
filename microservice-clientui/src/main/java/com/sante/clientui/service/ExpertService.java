package com.sante.clientui.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import com.sante.clientui.dao.PatientDao;
import com.sante.clientui.model.Risque;

@Service
// @FeignClient(name = "expert", url = "${expert.url}")
@FeignClient(name = "microservice-expert")
public interface ExpertService {

    @PostMapping("/expert")
    public Risque getRisque(PatientDao patientDao);
}