package com.tecsup.audit;

import com.tecsup.model.Auditoria;
import com.tecsup.repository.AuditoriaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

// guarda la bitacora en una transaccion propia para no interferir
// con la transaccion de la operacion que se esta registrando
@Component
public class AuditoriaRecorder {

    @Autowired
    private AuditoriaRepository repository;

    // registra el bean en el listener estatico de JPA
    @PostConstruct
    public void init() {
        AuditoriaListener.setRecorder(this);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrar(Auditoria auditoria) {
        repository.save(auditoria);
    }
}
