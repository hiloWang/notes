/*
 ============================================================================
 
 Author      : Ztiany
 Description : 

 ============================================================================
 */


#ifndef PLAY_WITH_ALGORITHMS_STUDENT_H
#define PLAY_WITH_ALGORITHMS_STUDENT_H

#include <string>
#include <iostream>
#include <ostream>

class Student {
public:
    std::string name;
    int score;

    Student(const std::string &name, int score) {
        this->name = name;
        this->score = score;
        std::cout << "Student constructor run" << std::endl;
    }

    Student(const Student &ref) {
        this->name = ref.name;
        this->score = ref.score;
        std::cout << "Student copy constructor run" << std::endl;
    }

    // 重载小于运算法,定义Student之间的比较方式
    //      如果分数相等，则按照名字的字母序排序
    //      如果分数不等，则分数高的靠前
    bool operator<(const Student &otherStudent) {
        return score != otherStudent.score ?
               score < otherStudent.score : name < otherStudent.name;
    }

    // 重载大于运算法,定义Student之间的比较方式
    //      如果分数相等，则按照名字的字母序排序
    //      如果分数不等，则分数高的靠前
    bool operator>(const Student &otherStudent) {
        return score != otherStudent.score ?
               score > otherStudent.score : name > otherStudent.name;
    }

    // 重载<<符号, 定义Student实例的打印输出方式
    friend std::ostream &operator<<(std::ostream &os, const Student &student) {
        os << "Student: " << student.name << " " << student.score << std::endl;
        return os;
    }

};

#endif //PLAY_WITH_ALGORITHMS_STUDENT_H
