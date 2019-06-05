package structures.stack;

/**
 * https://leetcode-cn.com/problems/valid-parentheses/description/
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.7.22 22:17
 */
public class LeetCode_20 {

    public static void main(String... args) {
        LeetCode_20 leetCode_20 = new LeetCode_20();
        System.out.println(leetCode_20.isValid("[][][]()(){}{}{}"));
        System.out.println(leetCode_20.isValid("[()][][]()(){}{}{}"));
        System.out.println(leetCode_20.isValid("[][][]()(){}{}{[}}"));
        System.out.println(leetCode_20.isValid("[][()][]()(){}{}{[]}"));
    }


    public boolean isValid(String s) {

        if (s == null) {
            return false;
        }

        if (s.isEmpty()) {
            return true;
        }

        ArrayStack<Character> mStack = new ArrayStack<>();

        if (s.length() % 2 != 0) {
            return false;
        }

        int length = s.length();
        char temp;

        for (int i = 0; i < length; i++) {
            temp = s.charAt(i);

            if (mStack.isEmpty()) {
                if (!(temp == '(' || temp == '[' || temp == '{')) {
                    return false;
                }
                mStack.push(temp);
            } else {
                //如果有匹配到，就可以出栈了
                if (check(mStack.peek(), temp)) {
                    mStack.pop();
                } else {
                    //没有匹配到就先入栈
                    mStack.push(temp);
                }
            }
        }

        return mStack.isEmpty();
    }

    private boolean check(char peek, char temp) {
        return (peek == '(' && temp == ')')
                || (peek == '[' && temp == ']')
                || (peek == '{' && temp == '}');
    }

}
