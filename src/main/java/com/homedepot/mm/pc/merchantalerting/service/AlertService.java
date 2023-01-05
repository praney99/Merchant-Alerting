package com.homedepot.mm.pc.merchantalerting.service;

import com.homedepot.mm.pc.merchantalerting.client.RespMatrixClient;
import com.homedepot.mm.pc.merchantalerting.configuration.ClientConfig;
import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;
import com.homedepot.mm.pc.merchantalerting.model.Alert;
import com.homedepot.mm.pc.merchantalerting.model.DCS;
import com.homedepot.mm.pc.merchantalerting.model.UserAlert;
import com.homedepot.mm.pc.merchantalerting.repository.AlertRepository;
import com.homedepot.mm.pc.merchantalerting.repository.UserAlertRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class AlertService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlertService.class);

    private RespMatrixClient respMatrixClient;
    private final AlertRepository alertRepository;
    private final UserAlertRepository userAlertRepository;

    private static final String dcsRegexPattern = "(\\w+)-(\\w+)-(\\w+)";

    WebClient webClient;

    @Autowired
    public AlertService(AlertRepository alertRepository, UserAlertRepository userAlertRepository) {
        this.alertRepository = alertRepository;
        this.userAlertRepository = userAlertRepository;
    }

    public Alert createAlertByLdap(CreateAlertRequest request, String ldap) {
        return createAlertWithLdapAssociations(request, List.of(ldap));
    }

    @Transactional
    public Alert createAlertWithLdapAssociations(CreateAlertRequest request, List<String> ldapAssociations) {
        Alert alert = request.toAlert();
        alert.setCreateBy(request.getSystemSource());
        alert.setCreated(new Timestamp(System.currentTimeMillis()));

        Alert persistedAlert = alertRepository.save(alert);

        List<UserAlert> userAlerts = new ArrayList<>();
        for (String ldap : ldapAssociations) {
            UserAlert ua = new UserAlert(ldap, persistedAlert.getId());
            userAlerts.add(ua);
        }
        userAlertRepository.saveAll(userAlerts);

        return persistedAlert;
    }

    @Transactional
    public void deleteAlert(UUID alertId) {
        alertRepository.deleteById(alertId);
    }

    public Optional<Alert> getAlert(UUID uuid) {
        return alertRepository.findById(uuid);
    }

    public List<Alert> getAlertsByLdap(String ldap) {
        return alertRepository.findAlertsByLdap(ldap);
    }

//    public Mono<List<DCS>> generateAlertByDCS(List<String> subclasses) {
//        Mono<List<DCS>> userList = Flux.fromIterable(subclasses)
//                .distinct()
//                .filter(subclass -> Pattern.matches(dcsRegexPattern, subclass))
//                .flatMap(subclass -> {
//                    String[] dcs = subclass.split("-");
//                    return respMatrixClient.getUsersByDcs(dcs[0], dcs[1], dcs[2]);
//                })
//                .onErrorContinue((e, o) -> LOGGER.error(e.getMessage()))
//                .distinct()
//                .collectList();
//
//        return userList;
//    }

}
