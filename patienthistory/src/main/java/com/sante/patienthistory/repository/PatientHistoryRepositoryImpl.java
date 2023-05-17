package com.sante.patienthistory.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.UpdateResult;
import com.sante.patienthistory.model.Note;
import com.sante.patienthistory.model.PatientHistory;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class PatientHistoryRepositoryImpl implements PatientHistoryCustomRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public UpdateResult updateNote(PatientHistory patientHistory) {
	Note note = patientHistory.getNotes().first();

	Criteria criteriaPatientHistory = Criteria.where("id").is(patientHistory.getId());
	Criteria criteriaNote = Criteria.where("notes.creationDate").is(note.getCreationDate());
	Criteria criteria = criteriaPatientHistory.andOperator(criteriaNote);
	Query query = new Query(criteria);

	Update update = new Update().set("notes.$.info", note.getInfo());

	UpdateResult result = mongoTemplate.updateFirst(query, update, PatientHistory.class);

	log.info("matchedCount : " + result.getMatchedCount() + ", modifiedCount : " + result.getModifiedCount()
		+ ", upsertedId : " + result.getUpsertedId());

	return result;
    }

    @Override
    public UpdateResult deleteNote(PatientHistory patientHistory) {

	Note note = patientHistory.getNotes().first();

	Criteria criteriaPatientHistory = Criteria.where("id").is(patientHistory.getId());
	Query query = new Query(criteriaPatientHistory);

	Update update = new Update().pull("notes",
		Query.query(Criteria.where("creationDate").is(note.getCreationDate())));

	UpdateResult result = mongoTemplate.updateFirst(query, update, PatientHistory.class);

	log.info("matchedCount : " + result.getMatchedCount() + ", modifiedCount : " + result.getModifiedCount()
		+ ", upsertedId : " + result.getUpsertedId());

	return result;
    }

}
