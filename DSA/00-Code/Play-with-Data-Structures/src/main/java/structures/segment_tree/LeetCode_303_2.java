package structures.segment_tree;

/**
 * 对于静态不可变数组，使用处理处理的方式 https://leetcode-cn.com/problems/range-sum-query-immutable/description/
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/16 23:45
 */
public class LeetCode_303_2 {

    private int[] sum;

    public LeetCode_303_2(int[] nums) {
        //预处理，sum中已经存储了各区间元素之和
        //sum[i]存储前 i 个元素之和，sum[0] = 0
        //sum[i]存储 nums [0,i-1]的和
        //sum[0] 表示的是 0 个元素之和
        //sum[1] 表示的是 1 个元素之和
        sum = new int[nums.length + 1];
        sum[0] = 0;
        for (int i = 1; i < sum.length; i++) {
            sum[i] = sum[i - 1] + nums[i - 1];
        }
    }

    public int sumRange(int i, int j) {
        //i  <= j
        return sum[j + 1]/*0-j 之和*/ - sum[i]/*0- (i-1)之和*/;
    }

}
