package com.hyn.sort;

public class QuickSort {

    public static void main(String[] args) {
        int[] sortArray = new int[]{2, 5, 4, 1, 2, 3, 6, 78, 4, 3, 1, 3, 56, 213, 7, 4, 2};
        quickSort(sortArray,0,sortArray.length-1);
        for (int i = 0; i < sortArray.length; i++) {
            System.out.println(sortArray[i]);
        }
    }

    public static void quickSort(int[] arr, int low, int high) {
        int i, j, base, t;
        if (low > high) {
            return;
        }

        //左边哨兵的索引
        i = low;
        //右边哨兵的索引
        j = high;
        //temp就是基准位,开始以最左边为基准位
        base = arr[low];

        while (i < j) {
            //先看右边，依次往左递减
            //1:先从右往左找一个小于 基准位的数
            //2:当右边的哨兵位置所在的数>基准位的数时
            //3:继续从右往左找（同时 j 索引-1）
            //4:找到后会跳出while循环
            while (base <= arr[j] && i < j) {
                j--;
            }

            //再看左边，依次往右递增
            //步骤和上面类似
            while (base >= arr[i] && i < j) {
                i++;
            }

            //如果满足条件则交换
            if (i < j) {

                //z、y 都是临时参数，用于存放 左右哨兵 所在位置的数据
                int z = arr[i];
                int y = arr[j];

                //左右哨兵 交换数据（互相持有对方的数据）
                arr[i] = y;
                arr[j] = z;

            }
        }
        //这时 跳出了 “while (i<j) {}” 循环
        //说明 i=j 左右在同一位置
        //最后将基准为与i和j相等位置的数字交换
        arr[low] = arr[i];
        arr[i] = base;


        //i=j
        //这时  左半数组<(i或j所在索引的数)<右半数组
        //也就是说(i或j所在索引的数)已经确定排序位置， 所以就不用再排序了，
        //只要用相同的方法 分别处理  左右数组就可以了

        //递归调用左半数组
        quickSort(arr, low, i - 1);
        //递归调用右半数组
        quickSort(arr, i + 1, high);
    }
}
