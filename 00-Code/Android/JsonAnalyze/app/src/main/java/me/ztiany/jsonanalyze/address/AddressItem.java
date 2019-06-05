package me.ztiany.jsonanalyze.address;

import java.util.ArrayList;
import java.util.List;

import me.ztiany.jsonanalyze.utils.Checker;

class AddressItem implements IName {

    private AddressToken name;
    private List<AddressItem> children;

    @Override
    public AddressToken getAddressToken() {
        return name;
    }

    @Override
    public List<IName> getChildren() {
        if (Checker.isEmpty(children)) {
            return null;
        }
        return new ArrayList<>(children);
    }


}