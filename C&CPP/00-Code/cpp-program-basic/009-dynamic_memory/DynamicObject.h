/*
 ============================================================================
 
 Author      : Ztiany
 Description : 

 ============================================================================
 */

#ifndef CPLUSPLUS_PROGRAM_DYNAMICOBJECT_H
#define CPLUSPLUS_PROGRAM_DYNAMICOBJECT_H


class DynamicObject1 {
public:
    DynamicObject1(int a) {
        mA = a;
        mB = 100;
    }

private:
    int mA;
    int mB;
public:
    int getTotal() {
        return mA + mB;
    }
};

class DynamicObject2 {
public:
    DynamicObject2(int a, int b) {
        mA = a;
        mB = b;
    }

private:
    int mA;
    int mB;
public:
    int getTotal() {
        return mA + mB;
    }
};


#endif //CPLUSPLUS_PROGRAM_DYNAMICOBJECT_H
