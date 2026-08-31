package com.example.keelungsights;

public class SightTest {
    public static void main(String[] args) {
        Sight sight = new Sight();

        sight.setSightName("泰安瀑布");
        sight.setZone("七堵區");

        System.out.println(sight.getSightName());
        System.out.println((sight.getZone()));
        System.out.println((sight));
    }
}
