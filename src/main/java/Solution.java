import java.util.*;

public class Solution {
    public long numberOfSubarrays(int[] nums) {
        SegmentTree st = new SegmentTree(nums);
        Map<Integer, List<Integer>> map = new HashMap<>();
        long count = 0;
        for (int i = 0; i < nums.length; i++) {
            count++;
            if(map.containsKey(nums[i])){
                List<Integer> ind = map.get(nums[i]);

                int l = 0;
                int r = ind.size() - 1;
                int mid;
                int index = -1;
                while (l <= r){
                    mid = l + (r - l) / 2;
                    if(st.getMaxInRange(ind.get(mid), i) > nums[i]){
                        l = mid + 1;
                    }else{
                        index = mid;
                        r = mid - 1;
                    }
                }
                if(index != - 1){
                    count += ind.size() - index;
                }

            }
            List<Integer> indexes = map.getOrDefault(nums[i], new ArrayList<>());
            indexes.add(i);
            map.put(nums[i], indexes);
        }



        return count;
    }
}
class SegmentTree {
    int length;
    int[] segmentTree;
    int[] nums;
    int n;

    public SegmentTree(int[] nums) {
        this.nums = nums;
        n = nums.length;
        if ((n != 1) && ((n & (n - 1)) == 0)) {
            length = n * 2 - 1;
        } else {
            int power = 1;
            while (power < n) {
                power *= 2;
            }
            length = power * 2 - 1;
        }
        segmentTree = new int[length];
        Arrays.fill(segmentTree, Integer.MIN_VALUE);
        buildTree(0, n - 1, 0);
    }

    public void buildTree(int low, int high, int position) {
        if (low == high) {
            segmentTree[position] = nums[low];
            return;
        }
        int mid = low + (high - low) / 2;

        buildTree(low, mid, 2 * position + 1);
        buildTree(mid + 1, high, 2 * position + 2);
        segmentTree[position] = Math.max(segmentTree[2 * position + 1], segmentTree[2 * position + 2]);
    }

    public void update(int index, int val) {
        updateTree(0, n - 1, 0, index, val);
    }

    public void updateTree(int low, int high, int position, int index, int val) {
        if (index < low || index > high) {
            return;
        }

        if (low == high) {
            nums[index] = val;
            segmentTree[position] = val;
            return;
        }

        int mid = low + (high - low) / 2;
        updateTree(low, mid, 2 * position + 1, index, val);
        updateTree(mid + 1, high, 2 * position + 2, index, val);

        segmentTree[position] = Math.max(segmentTree[2 * position + 1], segmentTree[2 * position + 2]);
    }

    public int getMaxValue() {
        return segmentTree[0];
    }

    public int getMaxInRange(int queryLow, int queryHigh) {
        return getMaxInRangeHelper(0, n - 1, 0, queryLow, queryHigh);
    }

    private int getMaxInRangeHelper(int low, int high, int position, int queryLow, int queryHigh) {
        if (low > queryHigh || high < queryLow) {
            return Integer.MIN_VALUE;
        }

        if (low >= queryLow && high <= queryHigh) {
            return segmentTree[position];
        }

        int mid = low + (high - low) / 2;
        int leftMax = getMaxInRangeHelper(low, mid, 2 * position + 1, queryLow, queryHigh);
        int rightMax = getMaxInRangeHelper(mid + 1, high, 2 * position + 2, queryLow, queryHigh);

        return Math.max(leftMax, rightMax);
    }
}