package com.ghsong.studymeeting.event;

import com.ghsong.studymeeting.domain.Account;
import com.ghsong.studymeeting.domain.Event;
import com.ghsong.studymeeting.domain.Study;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NamedEntityGraph;
import java.util.List;

@Transactional(readOnly = true)
public interface EventRepository extends JpaRepository<Event, Long> {


    @EntityGraph(value = "Events.withEnrollment", type = EntityGraph.EntityGraphType.LOAD)
    List<Event> findByStudyOrderByStartDateTime(Study study);

}
