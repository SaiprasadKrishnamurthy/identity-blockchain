package java_bootcamp;

import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/* Our state, defining a shared fact on the ledger.
 * See src/main/kotlin/examples/IAmAState.java and
 * src/main/kotlin/examples/IAmAlsoAState.java for examples. */
public class TokenState implements ContractState {


    private final Party party;
    private final Party party1;
    private final int amount;

    public TokenState(Party party, Party party1, int amount) {
        this.party = party;
        this.party1 = party1;
        this.amount = amount;
    }

    @NotNull
    @Override
    public List<AbstractParty> getParticipants() {
        return ImmutableList.of(party, party1);
    }

    public Party getParty() {
        return party;
    }

    public Party getParty1() {
        return party1;
    }

    public int getAmount() {
        return amount;
    }
}