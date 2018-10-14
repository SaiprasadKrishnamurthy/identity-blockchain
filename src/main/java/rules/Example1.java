package rules;

import com.deliveredtechnologies.rulebook.FactMap;
import com.deliveredtechnologies.rulebook.NameValueReferableMap;
import com.deliveredtechnologies.rulebook.lang.RuleBookBuilder;
import com.deliveredtechnologies.rulebook.model.RuleBook;

import java.io.File;

public class Example1 {
    public static void main(String[] args) {
        RuleBook ruleBook = RuleBookBuilder.create()
                .addRule(rule -> rule.withFactType(String.class)
                        .when(f -> f.containsKey("hello"))
                        .using("hello")
                        .then(System.out::print))
                .addRule(rule -> rule.withFactType(String.class)
                        .when(f -> f.containsKey("world"))
                        .using("world")
                        .then(System.out::println))
                .build();

        NameValueReferableMap factMap = new FactMap();
        factMap.setValue("hello", "Hello ");
        factMap.setValue("world", " World");
        ruleBook.run(factMap);
    }

}
