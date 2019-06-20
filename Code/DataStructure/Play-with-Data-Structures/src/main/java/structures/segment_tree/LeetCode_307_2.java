package structures.segment_tree;

/**
 * 使用线段树：https://leetcode-cn.com/problems/range-sum-query-mutable/description/，
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/17 9:31
 */
public class LeetCode_307_2 {

    private IntSegmentTree mIntSegmentTree;

    public LeetCode_307_2(int[] nums) {
        if (nums.length > 0) {
            mIntSegmentTree = new IntSegmentTree(nums, (left, right) -> left + right);
        }
    }

    public int sumRange(int i, int j) {
        return mIntSegmentTree.query(i, j);
    }

    public void update(int i, int val) {
        mIntSegmentTree.set(i, val);
    }

}
