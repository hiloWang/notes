package structures.segment_tree;

/**
 * 使用预处理解决：https://leetcode-cn.com/problems/range-sum-query-mutable/description/，
 * 由于需要更新，较慢。
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/17 9:31
 */
public class LeetCode_307_1 {

    private int[] sum;
    private int[] data;

    public LeetCode_307_1(int[] nums) {
        //预处理，sum中已经存储了各区间元素之和
        //sum[i]存储前 i 个元素之和，sum[0] = 0
        //sum[i]存储 nums [0,i-1]的和
        //sum[0] 表示的是 0 个元素之和
        //sum[1] 表示的是 1 个元素之和
        data = new int[nums.length];
        System.arraycopy(nums, 0, data, 0, data.length);
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

    public void update(int i, int val) {
        data[i] = val;
        for (int k = i+1; k< sum.length;k++) {
            sum[k] = sum[k - 1] + data[k - 1];
        }
    }

}
