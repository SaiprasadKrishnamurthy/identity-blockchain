package sai1.model;

import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class IdentityState implements ContractState {
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
}
