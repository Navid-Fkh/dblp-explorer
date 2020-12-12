package com.university.narm.explorer;

import java.io.IOException;
import java.util.List;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        Explorer explorer = new Explorer("/run/media/navid/AnotherExtraSpace/dblp_papers_v11.txt", "navid");
//        Explorer explorer = new Explorer("E:\\dblp_papers_v11.txt", "navid");

        int n = 10;

        for (int i = 1; i <= n + 1; i++) {
            List<Paper> nextTire = explorer.nextTire();

            System.out.println("Tier " + i + " ===========================================================\n");

            if (nextTire != null) {
                nextTire.forEach(p -> System.out.println(p.toString()));
            } else {
                System.out.println("Empty tire...");
                break;
            }

        }
    }
}
