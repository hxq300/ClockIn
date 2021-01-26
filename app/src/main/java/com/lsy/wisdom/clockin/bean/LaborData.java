package com.lsy.wisdom.clockin.bean;

/**
 * Created by lsy on 2020/3/26
 * todo : 劳务人员
 */
public class LaborData {

    private String message;
    private Labor data;
    private String code;

    public static class Labor {

        private int id;
        private int staff_age;
        private int history_integral;
        private int end_integral;
        private int section_id;
        private int sub_id;
        private int worktype_id;
        private int station_id;
        private int count;
        private int position_id;
        private String staff_name;
        private String staff_img;
        private String staff_sex;
        private String staff_nation;
        private String staff_card;
        private String staff_address;
        private String staff_province;
        private String staff_phone;
        private String sos_name;
        private String sos_ship;
        private String sos_phone;
        private String entry_time;
        private String password;
        private String state;
        private String status;
        private String type;
        private String train;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStaff_age() {
            return staff_age;
        }

        public void setStaff_age(int staff_age) {
            this.staff_age = staff_age;
        }

        public int getHistory_integral() {
            return history_integral;
        }

        public void setHistory_integral(int history_integral) {
            this.history_integral = history_integral;
        }

        public int getEnd_integral() {
            return end_integral;
        }

        public void setEnd_integral(int end_integral) {
            this.end_integral = end_integral;
        }

        public int getSection_id() {
            return section_id;
        }

        public void setSection_id(int section_id) {
            this.section_id = section_id;
        }

        public int getSub_id() {
            return sub_id;
        }

        public void setSub_id(int sub_id) {
            this.sub_id = sub_id;
        }

        public int getWorktype_id() {
            return worktype_id;
        }

        public void setWorktype_id(int worktype_id) {
            this.worktype_id = worktype_id;
        }

        public int getStation_id() {
            return station_id;
        }

        public void setStation_id(int station_id) {
            this.station_id = station_id;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getPosition_id() {
            return position_id;
        }

        public void setPosition_id(int position_id) {
            this.position_id = position_id;
        }

        public String getStaff_name() {
            return staff_name;
        }

        public void setStaff_name(String staff_name) {
            this.staff_name = staff_name;
        }

        public String getStaff_img() {
            return staff_img;
        }

        public void setStaff_img(String staff_img) {
            this.staff_img = staff_img;
        }

        public String getStaff_sex() {
            return staff_sex;
        }

        public void setStaff_sex(String staff_sex) {
            this.staff_sex = staff_sex;
        }

        public String getStaff_nation() {
            return staff_nation;
        }

        public void setStaff_nation(String staff_nation) {
            this.staff_nation = staff_nation;
        }

        public String getStaff_card() {
            return staff_card;
        }

        public void setStaff_card(String staff_card) {
            this.staff_card = staff_card;
        }

        public String getStaff_address() {
            return staff_address;
        }

        public void setStaff_address(String staff_address) {
            this.staff_address = staff_address;
        }

        public String getStaff_province() {
            return staff_province;
        }

        public void setStaff_province(String staff_province) {
            this.staff_province = staff_province;
        }

        public String getStaff_phone() {
            return staff_phone;
        }

        public void setStaff_phone(String staff_phone) {
            this.staff_phone = staff_phone;
        }

        public String getSos_name() {
            return sos_name;
        }

        public void setSos_name(String sos_name) {
            this.sos_name = sos_name;
        }

        public String getSos_ship() {
            return sos_ship;
        }

        public void setSos_ship(String sos_ship) {
            this.sos_ship = sos_ship;
        }

        public String getSos_phone() {
            return sos_phone;
        }

        public void setSos_phone(String sos_phone) {
            this.sos_phone = sos_phone;
        }

        public String getEntry_time() {
            return entry_time;
        }

        public void setEntry_time(String entry_time) {
            this.entry_time = entry_time;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTrain() {
            return train;
        }

        public void setTrain(String train) {
            this.train = train;
        }

        //        "id":3,
//                "staff_age":24,
//                "history_integral":100,
//                "end_integral":90,
//                "section_id":1,
//                "sub_id":1,
//                "worktype_id":1,
//                "station_id":2,
//                "count":0,
//                "position_id":2,
//                "staff_name":"ssw",
//                "staff_img":"c08a16d1-5d88-4213-b259-1038fe620e92.jpg",
//                "staff_sex":"男",
//                "staff_nation":"汉",
//                "staff_card":"341100000000000000",
//                "staff_address":"河南省西平县",
//                "staff_province":"河南省",
//                "staff_phone":"13100000000",
//                "sos_name":"石头1",
//                "sos_ship":"亲戚",
//                "sos_phone":"13000000000",
//                "entry_time":"2020-03-16",
//                "password":"123456",
//                "state":"1",
//                "status":"1",
//                "type":"管理员",
//                "train":"0"
    }

}
