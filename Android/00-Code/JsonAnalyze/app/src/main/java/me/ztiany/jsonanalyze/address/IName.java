package me.ztiany.jsonanalyze.address;

import java.util.List;

interface IName {

    AddressToken getAddressToken();

    List<IName> getChildren();

    class AddressToken {

        static final int COUNTRY = 1;
        static final int PROVINCE = 2;
        static final int CITY = 3;
        static final int AREA = 4;

        private int mIdentifying;
        private String mName;

        int getIdentifying() {
            return mIdentifying;
        }

        public void setIdentifying(int identifying) {
            mIdentifying = identifying;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }
    }

}
