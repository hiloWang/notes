package structures.hash;

import java.util.Objects;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/20 17:28
 */
public class JavaMain {

    public static void main(String... args) {
        System.out.println(Integer.hashCode(44));
        System.out.println("hashcode".hashCode());
        System.out.println(new Person(27, 1, "ztiany", 99).hashCode());
    }

    private static class Person {

        private final int age;
        private final int sex;
        private final String name;
        private final int grade;

        private Person(int age, int sex, String name, int grade) {
            this.age = age;
            this.sex = sex;
            this.name = name;
            this.grade = grade;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Person)) return false;
            Person person = (Person) o;
            return age == person.age &&
                    sex == person.sex &&
                    grade == person.grade &&
                    Objects.equals(name, person.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(age, sex, name, grade);
        }

    }

}
