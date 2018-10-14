package sai1.schema;

import com.google.common.collect.ImmutableList;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * An IOUState schema.
 */
public class IdentitySchemaV1 extends MappedSchema {

    public IdentitySchemaV1() {
        super(IdentitySchema.class, 1, ImmutableList.of(IdentityStateEntity.class));
    }

    @Entity
    @Table(name = "identity_states")
    public static class IdentityStateEntity extends PersistentState {
        @Column(name = "sourceParty")
        private final String sourceParty;

        @Column(name = "targetParty")
        private final String targetParty;

        @Lob
        @Column(name = "identityJsonData")
        private String identityJsonData;

        public IdentityStateEntity(String sourceParty, String targetParty, String identityJsonData) {
            this.sourceParty = sourceParty;
            this.targetParty = targetParty;
            this.identityJsonData = identityJsonData;
        }

        // Default constructor required by hibernate.
        public IdentityStateEntity() {
            this.sourceParty = null;
            this.targetParty = null;
            this.identityJsonData = null;
        }

        public String getSourceParty() {
            return sourceParty;
        }

        public String getTargetParty() {
            return targetParty;
        }

        public String getIdentityJsonData() {
            return identityJsonData;
        }

        public void setIdentityJsonData(String identityJsonData) {
            this.identityJsonData = identityJsonData;
        }
    }
}