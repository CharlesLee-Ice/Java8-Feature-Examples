package com.example.liqilin.java8feature;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class MainActivity extends AppCompatActivity {

    List<Person> mPersonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        test();
    }

    private void test() {
        mPersonList = new ArrayList<>();
        mPersonList.add(new Person("Sam", LocalDate.now().minusYears(10), Person.Sex.MALE, "address1"));
        mPersonList.add(new Person("Lily", LocalDate.now().minusYears(8), Person.Sex.FEMALE, "address2"));
        mPersonList.add(new Person("Jone", LocalDate.now().minusYears(15), Person.Sex.MALE, "address3"));

        /*******************************************************************************************************************************
         *
         *  Lambda Expressions
         *  lambda expressions are anonymous methods
         *
         *******************************************************************************************************************************/

        /********************* 1 *********************/
        test1(new Predicate<Person>() {
            @Override
            public boolean test(Person person) {
                return person.getAge() > 10;
            }
        });

        test1((Person p) -> p.getAge() > 10);

        test1(p -> p.getAge() > 10);

        /********************* 2 *********************/
        test2(new Predicate<Person>() {
            @Override
            public boolean test(Person person) {
                return person.getAge() > 0;
            }
        }, new Consumer<Person>() {
            @Override
            public void accept(Person person) {
                person.printPerson();
            }
        });

        test2(p -> p.getAge() > 0, p -> p.printPerson());

        /********************* 3 *********************/
        test3(new Predicate<Person>() {
            @Override
            public boolean test(Person person) {
                return person.getAge() > 0;
            }
        }, new Function<Person, String>() {
            @Override
            public String apply(Person person) {
                return person.getEmailAddress();
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String s) {
                Log.i("liqilin", s);
            }
        });

        test3(p -> p.getAge() > 0, p -> p.getEmailAddress(), email -> Log.i("liqilin", email));

        /********************* 4 *********************/
        mPersonList.stream().filter(person -> person.getAge() > 10).map(person -> person.getEmailAddress()).forEach(email -> Log.i("liqilin", email));


        /********************* 5 *********************/
        FirstLevel firstLevel = new FirstLevel();
        firstLevel.methodInFirstLevel(23);


        /*******************************************************************************************************************************
         *
         *  Method References
         *
         * Reference to a static method	ContainingClass::staticMethodName
         * Reference to an instance method of a particular object	containingObject::instanceMethodName
         * Reference to an instance method of an arbitrary object of a particular type	ContainingType::methodName
         * Reference to a constructor	ClassName::new
         *
         *******************************************************************************************************************************/

        Person[] personArray = mPersonList.toArray(new Person[mPersonList.size()]);

        Arrays.sort(personArray, new PersonAgeComparator());

        Arrays.sort(personArray, (a, b) -> a.getBirthday().compareTo(b.getBirthday()));

        //Reference to a static method
        Arrays.sort(personArray, Person::compareByAge);

        //Reference to an instance method of a particular object
        Arrays.sort(personArray, new Person()::compareByAge2);

        String[] stringArray = { "Barbara", "James", "Mary", "John",
                "Patricia", "Robert", "Michael", "Linda" };

        //Reference to an Instance Method of an Arbitrary Object of a Particular Type
        Arrays.sort(stringArray, String::compareToIgnoreCase);

        Set<Person> transformed = transferElements(mPersonList, new Supplier<Set<Person>>() {
            @Override
            public Set<Person> get() {
                return new HashSet<>();
            }
        });

        Set<Person> rosterSetLambda = transferElements(mPersonList, () -> { return new HashSet<>(); });

        //Reference to a constructor
        Set<Person> rosterSet = transferElements(mPersonList, HashSet::new);
    }

    private void test1(Predicate<Person> tester) {
        for (Person person : mPersonList) {
            if (tester.test(person)) {
                person.printPerson();
            }
        }
    }

    private void test2(Predicate<Person> tester, Consumer<Person> consumer) {
        for (Person person : mPersonList) {
            if (tester.test(person)) {
                consumer.accept(person);
            }
        }
    }

    private void test3(Predicate<Person> tester, Function<Person, String> function, Consumer<String> consumer) {
        for (Person person : mPersonList) {
            if (tester.test(person)) {
                consumer.accept(function.apply(person));
            }
        }
    }

    class FirstLevel {

        public int x = 1;

        void methodInFirstLevel(int x) {

            // The following statement causes the compiler to generate
            // the error "local variables referenced from a lambda expression
            // must be final or effectively final" in statement A:
            //
            // x = 99;

            /***
             * Lambda expressions are lexically scoped. This means that they do not inherit any names from a supertype or introduce a new level of scoping.
             * Declarations in a lambda expression are interpreted just as they are in the enclosing environment.
             *
             * If you substitute the parameter x in place of y in the declaration of the lambda expression myConsumer, then the compiler generates an error:
             * The compiler generates the error "variable x is already defined in method methodInFirstLevel(int)" because the lambda expression does not introduce a new level of scoping.
             */
            Consumer<Integer> myConsumer = (y) ->
            {
                Log.i("liqilin", "x = " + x); // x = 23
                Log.i("liqilin", "y = " + y); // y = 23
                Log.i("liqilin", "this.x = " + this.x); // x = 1
            };

            myConsumer.accept(x);

        }
    }

    class PersonAgeComparator implements Comparator<Person> {
        @Override
        public int compare(Person person, Person t1) {
            return person.getBirthday().compareTo(t1.getBirthday());
        }
    }

//    public static <T, SOURCE extends Collection<T>, DEST extends Collection<T>>
//    DEST transform(SOURCE source, Supplier<DEST> factory) {
//        DEST result = factory.get();
//        for (T t : source) {
//            result.add(t);
//        }
//        return result;
//    }

    public static <T, SOURCE extends Collection<T>, DEST extends Collection<T>>
    DEST transferElements(
            SOURCE sourceCollection,
            Supplier<DEST> collectionFactory) {

        DEST result = collectionFactory.get();
        for (T t : sourceCollection) {
            result.add(t);
        }
        return result;
    }
}
