package com.nowcoder;

import java.util.*;

/**
 * Created by seu on 2017/5/23.
 */
public class Main {
    // 质数筛法返回n以内的素数列表
    //具体操作为不断将从1开始2的倍数、3的倍数、4的倍数置0，如此一来，就不许要额外去判断一个数是不是质数
    //且总的判断次数为n/2+n/3+...n/n=n(1/2+1/3+.../1/n)=nlogn
    private static int[] getPrimes(int n) {
        int[] a = new int[n];
        for(int i = 2; i < n; i ++) {
            a[i] = i;
        }
        // 筛法
        for(int i = 2; i < n; i ++) {
            if (a[i] != 0) {
                for(int j = i * 2; j < n; j = j + i) {
                    a[j] = 0;
                }
            }
        }
        int count = 0;
        for(int i = 2; i < n; i++) {
            if (a[i] != 0) {
                count ++;
            }
        }
        if (count > 0) {
            int[] primes  = new int[count];
            int j = 0;
            for (int i = 2; i < n; i ++) {
                if(a[i] != 0) {
                    primes[j] = a[i];
                    j ++;
                }
            }
            return primes;
        }
        return null;
    }

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        while(sc.hasNext()){
            int n=sc.nextInt();
            int[] cur=getPrimes(n);
            if(cur!=null){
                for(int i:cur){
                    System.out.print(i+" ");
                }
            }
            System.out.println();
        }
    }
}
