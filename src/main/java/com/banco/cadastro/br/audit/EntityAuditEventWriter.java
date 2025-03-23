package com.banco.cadastro.br.audit;

import com.banco.cadastro.br.domain.enumeration.EntityAuditAction;

@FunctionalInterface
public interface EntityAuditEventWriter {
    public void writeAuditEvent(Object target, EntityAuditAction action);
}
