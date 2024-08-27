package com.example.weatherui.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WindChillData {

    @SerializedName("response")
    public Response response;

    public static class Response {
        @SerializedName("header")
        public Header header;

        @SerializedName("body")
        public Body body;
    }

    public static class Header {
        @SerializedName("resultCode")
        public String resultCode;

        @SerializedName("resultMsg")
        public String resultMsg;
    }

    public static class Body {
        @SerializedName("dataType")
        public String dataType;

        @SerializedName("items")
        public Items items;

        @SerializedName("numOfRows")
        public int numOfRows;

        @SerializedName("pageNo")
        public int pageNo;

        @SerializedName("totalCount")
        public int totalCount;
    }

    public static class Items {
        @SerializedName("item")
        public List<Item> itemList;
    }

    public static class Item {
        @SerializedName("code")
        public String code;

        @SerializedName("areaNo")
        public String areaNo;

        @SerializedName("date")
        public String date;

        @SerializedName("h1")
        public String h1;
        @SerializedName("h2")
        public String h2;
        @SerializedName("h3")
        public String h3;
        @SerializedName("h4")
        public String h4;
        @SerializedName("h5")
        public String h5;
        @SerializedName("h6")
        public String h6;
        @SerializedName("h7")
        public String h7;
        @SerializedName("h8")
        public String h8;
        @SerializedName("h9")
        public String h9;
        @SerializedName("h10")
        public String h10;
        @SerializedName("h11")
        public String h11;
        @SerializedName("h12")
        public String h12;
        @SerializedName("h13")
        public String h13;
        @SerializedName("h14")
        public String h14;
        @SerializedName("h15")
        public String h15;
        @SerializedName("h16")
        public String h16;
        @SerializedName("h17")
        public String h17;
        @SerializedName("h18")
        public String h18;
        @SerializedName("h19")
        public String h19;
        @SerializedName("h20")
        public String h20;
        @SerializedName("h21")
        public String h21;
        @SerializedName("h22")
        public String h22;
        @SerializedName("h23")
        public String h23;
        @SerializedName("h24")
        public String h24;
        @SerializedName("h25")
        public String h25;
        @SerializedName("h26")
        public String h26;
        @SerializedName("h27")
        public String h27;
        @SerializedName("h28")
        public String h28;
        @SerializedName("h29")
        public String h29;
        @SerializedName("h30")
        public String h30;
        @SerializedName("h31")
        public String h31;
        @SerializedName("h32")
        public String h32;
        @SerializedName("h33")
        public String h33;
        @SerializedName("h34")
        public String h34;
        @SerializedName("h35")
        public String h35;
        @SerializedName("h36")
        public String h36;
        @SerializedName("h37")
        public String h37;
        @SerializedName("h38")
        public String h38;
        @SerializedName("h39")
        public String h39;
        @SerializedName("h40")
        public String h40;
        @SerializedName("h41")
        public String h41;
        @SerializedName("h42")
        public String h42;
        @SerializedName("h43")
        public String h43;
        @SerializedName("h44")
        public String h44;
        @SerializedName("h45")
        public String h45;
        @SerializedName("h46")
        public String h46;
        @SerializedName("h47")
        public String h47;
        @SerializedName("h48")
        public String h48;
        @SerializedName("h49")
        public String h49;
        @SerializedName("h50")
        public String h50;
        @SerializedName("h51")
        public String h51;
        @SerializedName("h52")
        public String h52;
        @SerializedName("h53")
        public String h53;
        @SerializedName("h54")
        public String h54;
        @SerializedName("h55")
        public String h55;
        @SerializedName("h56")
        public String h56;
        @SerializedName("h57")
        public String h57;
        @SerializedName("h58")
        public String h58;
        @SerializedName("h59")
        public String h59;
        @SerializedName("h60")
        public String h60;
        @SerializedName("h61")
        public String h61;
        @SerializedName("h62")
        public String h62;
        @SerializedName("h63")
        public String h63;
        @SerializedName("h64")
        public String h64;
        @SerializedName("h65")
        public String h65;
        @SerializedName("h66")
        public String h66;
        @SerializedName("h67")
        public String h67;
        @SerializedName("h68")
        public String h68;
        @SerializedName("h69")
        public String h69;
        @SerializedName("h70")
        public String h70;
        @SerializedName("h71")
        public String h71;
        @SerializedName("h72")
        public String h72;
        @SerializedName("h73")
        public String h73;
        @SerializedName("h74")
        public String h74;
        @SerializedName("h75")
        public String h75;
        @SerializedName("h76")
        public String h76;
        @SerializedName("h77")
        public String h77;
        @SerializedName("h78")
        public String h78;
    }
}

