package sai1.model;

import com.google.common.collect.ImmutableList;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;
import net.corda.core.schemas.QueryableState;
import org.jetbrains.annotations.NotNull;
import sai1.schema.IdentitySchemaV1;

import java.util.List;

public class IdentityState implements QueryableState {
    private final Party sourceParty;
    private final Party targetParty;
    private final String identityJsonData;

    public IdentityState(Party sourceParty, Party targetParty, String identityJsonData) {
        this.sourceParty = sourceParty;
        this.targetParty = targetParty;
        this.identityJsonData = identityJsonData;
    }

    public Party getSourceParty() {
        return sourceParty;
    }

    public Party getTargetParty() {
        return targetParty;
    }

    public String getIdentityJsonData() {
        return identityJsonData;
    }

    @NotNull
    @Override
    public List<AbstractParty> getParticipants() {
        return ImmutableList.of(sourceParty, targetParty);
    }

    @NotNull
    @Override
    public PersistentState generateMappedObject(MappedSchema schema) {
        if (schema instanceof IdentitySchemaV1) {
            return new IdentitySchemaV1.IdentityStateEntity(
                    this.sourceParty.toString(),
                    this.targetParty.toString(),
                    this.identityJsonData);
        } else {
            throw new IllegalArgumentException("Unrecognised schema $schema");
        }
    }

    @NotNull
    @Override
    public Iterable<MappedSchema> supportedSchemas() {
        return ImmutableList.of(new IdentitySchemaV1());
    }
}
