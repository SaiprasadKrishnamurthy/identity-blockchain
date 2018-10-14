package rules;

import com.deliveredtechnologies.rulebook.FactMap;
import com.deliveredtechnologies.rulebook.NameValueReferableMap;
import com.deliveredtechnologies.rulebook.RuleState;
import com.deliveredtechnologies.rulebook.annotation.*;
import com.deliveredtechnologies.rulebook.model.runner.RuleBookRunner;

@Rule(name = "hello world rule")
public class HelloWorldRule {

    @Given("hello")
    private String hello;

    @Given("world")
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

    public static void main(String args[]) {
        RuleBookRunner ruleBook = new RuleBookRunner("rules");
        NameValueReferableMap facts = new FactMap();

        facts.setValue("hello", "Hello");
        facts.setValue("world", "World");
        facts.setValue("foo", "Foo");

        ruleBook.run(facts);
        ruleBook.getResult().ifPresent(System.out::println);


    }
}