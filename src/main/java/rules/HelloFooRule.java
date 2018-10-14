package rules;

import com.deliveredtechnologies.rulebook.RuleState;
import com.deliveredtechnologies.rulebook.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

@Rule(name = "hello foo rule", order = 2)
public class HelloFooRule {

    @Given("hello")
    private String hello;

    @Given("foo")
    private String world;

    @Result
    private String helloworld;

    @When
    public boolean when() {
        return true;
    }

    @Then
    public RuleState then() {
        helloworld = hello + " " + world;
        return RuleState.NEXT;
    }
}