package structures.segment_tree;

/**
 * 使用线段树：https://leetcode-cn.com/problems/range-sum-query-immutable/description/，
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/16 23:45
 */
public class LeetCode_303_1 {

    private IntSegmentTree mSegmentTree;

    public LeetCode_303_1(int[] nums) {
        if (nums.length <= 0) {
            return;
        }
        mSegmentTree = new IntSegmentTree(nums, (left, right) -> left + right);
    }

    public int sumRange(int i, int j) {
        if (mSegmentTree == null) {
            return 0;
        }
        return mSegmentTree.query(i, j);
    }

}
