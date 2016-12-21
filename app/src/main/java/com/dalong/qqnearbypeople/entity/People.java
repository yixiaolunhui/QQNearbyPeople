package com.dalong.qqnearbypeople.entity;

/**
 * 附近的人
 * Created by dalong on 2016/12/20.
 */

public class People {
    //名字
    private String name;
    //年龄
    private String age;
    //头像id
    private int portraitId;
    //false为男，true为女
    private boolean sex;
    //距离
    private float distance;

    public int getPortraitId() {
        return portraitId;
    }

    public void setPortraitId(int portraitId) {
        this.portraitId = portraitId;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }
}
