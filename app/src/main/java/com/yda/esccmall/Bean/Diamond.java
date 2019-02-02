package com.yda.esccmall.Bean;

public class Diamond {

    private int index;
    private int X;
    private int Y;

    private DiamondBean model;


    public Diamond() {
    }

    public Diamond(int index, int x, int y,DiamondBean model) {
        this.index = index;
        X = x;
        Y = y;
        this.model=model;
    }

    public static class DiamondBean {

        private int id;
        private int userid;
        private String user_name;
        private String value;
        private String remain;
        private String thief_max_lv;
        private String thief_min_lv;
        private String min_remain;
        private int cache_mini;
        private int state;
        private String donetime;
        private String addtime;
        private String endtime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getRemain() {
            return remain;
        }

        public void setRemain(String remain) {
            this.remain = remain;
        }

        public String getThief_max_lv() {
            return thief_max_lv;
        }

        public void setThief_max_lv(String thief_max_lv) {
            this.thief_max_lv = thief_max_lv;
        }

        public String getThief_min_lv() {
            return thief_min_lv;
        }

        public void setThief_min_lv(String thief_min_lv) {
            this.thief_min_lv = thief_min_lv;
        }

        public String getMin_remain() {
            return min_remain;
        }

        public void setMin_remain(String min_remain) {
            this.min_remain = min_remain;
        }

        public int getCache_mini() {
            return cache_mini;
        }

        public void setCache_mini(int cache_mini) {
            this.cache_mini = cache_mini;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getDonetime() {
            return donetime;
        }

        public void setDonetime(String donetime) {
            this.donetime = donetime;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public DiamondBean getModel() {
        return model;
    }

    public void setModel(DiamondBean model) {
        this.model = model;
    }
}
