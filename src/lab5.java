import java.util.Scanner;
public class lab5 {
    public static int[] twoSums(int[] nums, int target) {

        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {

                if (nums[i] + nums[j] == target) {
                    return new int[] { i, j };
                }
            }
        }


        return new int[] {};
    }

    // question 2 SINGLE number

    public static int singleNumber(int[] nums) {
        int result = 0;

        for (int i = 0; i < nums.length; i++) {
            result = result ^ nums[i];
        }

        return result;
    }


    public static void main(String[] args) {

        // Test Two Sum
        int[] nums1 = {2, 7, 11, 15};
        int target = 9;

        int[] result = twoSums(nums1, target);
        System.out.println("Two Sum Result: [" + result[0] + ", " + result[1] + "]");

        // Test Single Number ----
        int[] nums2 = {4, 1, 2, 1, 2};

        System.out.println("Single Number Result: " + singleNumber(nums2));
    }
}


