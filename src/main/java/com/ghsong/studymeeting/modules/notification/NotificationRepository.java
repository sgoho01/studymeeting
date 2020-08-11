package com.ghsong.studymeeting.modules.notification;

import com.ghsong.studymeeting.modules.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Long countByAccountAndChecked(Account account, boolean checked);
}
