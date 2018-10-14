package rules;

import com.deliveredtechnologies.rulebook.FactMap;
import com.deliveredtechnologies.rulebook.NameValueReferableMap;
import com.deliveredtechnologies.rulebook.model.RuleBook;
import com.deliveredtechnologies.rulebook.model.runner.RuleBookRunner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringRules {
    @Bean
    public RuleBook ruleBook() {
        RuleBook ruleBook = new RuleBookRunner("rules");
        return ruleBook;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringRules.class);
        RuleBook ruleBook = context.getBean(RuleBook.class);
        NameValueReferableMap<String> facts = new FactMap<>();
        facts.setValue("hello", "Hello ");
        facts.setValue("world", "World");
        facts.setValue("foo", "Foo");
        ruleBook.run(facts);
        ruleBook.getResult().ifPresent(System.out::println);
    }
}
